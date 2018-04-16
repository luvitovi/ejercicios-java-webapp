package ec.gob.salinas.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración de la seguridad.
 * 
 * @author Luis
 *
 */
@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {

	// Constantes para configuracion de la seguridad con tokens
	public static final String LOGIN_URL = "/login";
	public static final String HEADER_AUTHORIZACION_KEY = "Authorization";
	public static final String TOKEN_BEARER_PREFIX = "Bearer ";
	public static final String ISSUER_INFO = "http://localhost:8080/";
	public static final long TOKEN_EXPIRATION_TIME = 15000; // 15 segundos (milisegundos)

	@Autowired
	@Qualifier("customUserDetailsService")
	private UserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Desactiva el manejo de cookies.
			.and()
			.authorizeRequests()
				.antMatchers(HttpMethod.POST, LOGIN_URL) // Solo permite acceso al login
					.permitAll()
				.antMatchers(HttpMethod.GET, "/rentas/emision/**")
					.hasRole("RENTAS")
				.anyRequest() // Todo el resto de rutas están bloqueadas a espera de la autenticacion.
					.authenticated()
			.and()
				.csrf()
					.disable()
			.addFilter(new JWTAuthenticationFilter(authenticationManager())) // Filtro para la autenticacion.
			.addFilter(new JWTAuthorizationFilter(authenticationManager())); // Filtro para la verificación de la vigencia del token.
	}

}
