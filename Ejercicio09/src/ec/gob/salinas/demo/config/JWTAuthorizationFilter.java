package ec.gob.salinas.demo.config;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;

/**
 * Valida la vigencia del token.
 * @author Luis
 *
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	
	public JWTAuthorizationFilter(AuthenticationManager authManager) {
		super(authManager);
	}

	/**
	 * Verifica que el token este vigente.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest req,
								   HttpServletResponse res, 
								   FilterChain chain) throws IOException, ServletException {
		// Obtiene el encabezado (token)
		String header = req.getHeader(Security.HEADER_AUTHORIZACION_KEY);
		if (header == null || !header.startsWith(Security.TOKEN_BEARER_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}
		
		// Obtiene la autenticación
		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

		// Si obtiene la autenticación define el usuario como autenticado y permite la acción
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		// Continúa ejecutando el resto de filtros.
		chain.doFilter(req, res);
	}

	/**
	 * Obtiene la autenticación del token, si el token está expirado o no es correcto genera un error.
	 * @param request
	 * @return
	 */
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		
		// Obtiene el valor del header denominado: Autorization con lo que obtiene el token.
		String token = request.getHeader(Security.HEADER_AUTHORIZACION_KEY);
		
		if (token != null) {
			
			// Se recupera el usuario desde el token.
			// el método: parseClaimsJws verifica la validez del token.
			String user = Jwts.parser()
							  .setSigningKey(Security.SUPER_SECRET_KEY)
							  .parseClaimsJws(token.replace(Security.TOKEN_BEARER_PREFIX, ""))
							  .getBody()
							  .getSubject();

			// Si recupera el usuario, retorna un objeto para la actualización.
			if (user != null) {

				// Aqui es donde se debe recuperar el token desde la base para obtener el resto
				// de privilegios o determinar si no ha sido "desactivado".

				return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
			}
			return null;
		}
		return null;
	}
	
}