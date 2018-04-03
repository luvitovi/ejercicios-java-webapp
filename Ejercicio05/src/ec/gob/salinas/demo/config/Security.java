package ec.gob.salinas.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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
		http.authorizeRequests()
				.antMatchers("/logout", "/login", "/login.zul", 
							 "/timeout.zul", "/img/**", "/css/**", "/js/**",  
							 "/zk", "/zkau", "/zk/**", "/zkau/**")
					.permitAll()
//		        .antMatchers("/adm/**", "/seg/**")
//					.access("hasRole('ADMINISTRADORES')")
//			    .antMatchers("/usr/**")
//					.access("hasRole('USUARIOS')")
				.antMatchers("/index.zul")
					.authenticated()
				.antMatchers("/**")
					.denyAll()
			.and()
				.formLogin()
					.loginPage("/login.zul")
					.loginProcessingUrl("/login")
					.failureUrl("/login.zul?login_error=1")
					.usernameParameter("username")
					.passwordParameter("password")
			.and()
				.logout()
					.logoutSuccessUrl("/index.zul")
					.logoutUrl("/logout")
					.invalidateHttpSession(true)
			.and()
				.csrf()
					.disable()
				.headers()
					.frameOptions()
						.sameOrigin(); 
	}


}
