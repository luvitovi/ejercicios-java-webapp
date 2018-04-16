package ec.gob.salinas.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import ec.gob.salinas.demo.repos.UsuarioRepo;
import ec.gob.salinas.demo.service.CustomAuthenticationProvider;
import ec.gob.salinas.demo.util.CookieUtil;

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

	public static final String PARAMETRO_USUARIO = "username";
	public static final String PARAMETRO_PASSWORD = "password";
	public static final String SUCCESS_URL = "/Ejercicio13-Client2/";

	@Autowired
	private UsuarioRepo usuarioRepo;

	@Bean
	public AuthenticationProvider authenticationProvider() {
		return new CustomAuthenticationProvider();
	}

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/logout", "/login", "/login.zul", 
							 "/timeout.zul", "/img/**", "/css/**", "/js/**",  
							 "/zk", "/zkau", "/zk/**", "/zkau/**")
					.permitAll()
				.antMatchers("/index.zul")
					.authenticated()
				.antMatchers("/**")
					.denyAll()
			.and()
				.formLogin()
					.loginPage("/login.zul")
					.loginProcessingUrl("/login")
					.failureUrl("/login.zul?login_error=1")
					.usernameParameter(PARAMETRO_USUARIO)
					.passwordParameter(PARAMETRO_PASSWORD)
			.and()
				.logout()
					.logoutSuccessUrl("/index.zul")
					.logoutUrl("/logout")
					.invalidateHttpSession(true)
					.deleteCookies(CookieUtil.COOKIE_NAME)
			.and()
				.csrf()
					.disable()
				.headers()
					.frameOptions()
						.sameOrigin()
			.and()
		.addFilter(new JWTAuthenticationFilter(authenticationManager(), usuarioRepo)) // Filtro para la autenticacion.
		.addFilter(new JWTAuthorizationFilter(authenticationManager(), usuarioRepo)); // Filtro para la verificación de la vigencia del token.
	}


}
