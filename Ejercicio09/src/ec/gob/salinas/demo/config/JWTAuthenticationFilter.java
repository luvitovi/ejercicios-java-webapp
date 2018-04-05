package ec.gob.salinas.demo.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import ec.gob.salinas.demo.modelo.pojo.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Permite la autenticación de un usuario y genera el token.
 * @author Luis
 *
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	// Este objeto viene desde la clase Security.
	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	/**
	 * Recupera el usuario que viene desde el cliente e intenta autenticarlo.
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, 
											   HttpServletResponse response) throws AuthenticationException {
		try {
			Usuario credenciales = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credenciales.getNombreUsuario(), 
																							credenciales.getPassword(), 
																							new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Si se autentica correctamente, genera el token y lo retorna al cliente.
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		// Si se quiere que un token no expire, no se coloca el atributo "Expiration".
		String token = Jwts.builder().setIssuedAt(new Date())
									.setIssuer(Security.ISSUER_INFO)
									.setSubject(((User)auth.getPrincipal()).getUsername())
									// .setExpiration(new Date(System.currentTimeMillis() + Security.TOKEN_EXPIRATION_TIME))
									.signWith(SignatureAlgorithm.HS512, Security.SUPER_SECRET_KEY)
									.compact();
		
		// Aqui es donde se debe almacenar el token en la base.
		System.out.println("Token generado para:" + auth.getName() + " es:" + token);
		
		response.addHeader(Security.HEADER_AUTHORIZACION_KEY, 
						  Security.TOKEN_BEARER_PREFIX + " " + token);
	}
}