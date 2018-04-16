package ec.gob.salinas.demo.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ec.gob.salinas.demo.modelo.UserDetailsHolder;
import ec.gob.salinas.demo.modelo.pojo.InterUsuarioRol;
import ec.gob.salinas.demo.modelo.pojo.Usuario;
import ec.gob.salinas.demo.repos.UsuarioRepo;
import ec.gob.salinas.demo.util.CookieUtil;
import ec.gob.salinas.demo.util.TokenUtil;

/**
 * Permite la autenticación de un usuario y genera el token.
 * @author Luis
 *
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	// Estos objeto viene desde la clase Security.
	private UsuarioRepo usuarioRepo;
	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UsuarioRepo usuarioRepo) {
		this.authenticationManager = authenticationManager;
		this.usuarioRepo = usuarioRepo;
	}

	/**
	 * Recupera el usuario que viene desde el cliente e intenta autenticarlo.
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, 
											   HttpServletResponse response) throws AuthenticationException {
		
		String username = request.getParameter(Security.PARAMETRO_USUARIO);
		String password = request.getParameter(Security.PARAMETRO_PASSWORD);

		// Obtiene el token
		
		String token = TokenUtil.autenticar(username, password);
		
		if (token == null) {
			return null;
		}

        // Obtiene las credenciales locales.
		Authentication authAux = null;
		Usuario usuario = usuarioRepo.getUsuarioPorNombre(username);
		
		if (usuario != null) {
			authAux = new UsernamePasswordAuthenticationToken(new UserDetailsHolder(usuario, getGrantedAuthorities(usuario)), 
															 usuario.getPassword(), 
															 getGrantedAuthorities(usuario));
			authenticationManager.authenticate(authAux);
			
			// Crea el cookie
			CookieUtil.create(response, CookieUtil.COOKIE_NAME, 
							  token.replace(Security.TOKEN_BEARER_PREFIX + " ",  ""), 
							  false, 
							  -1, 
							  CookieUtil.MI_DOMINIO);
			
			return authAux;
		}else{
			return null;
		}	

	}

	/**
	 * Si se autentica correctamente, genera el token y lo retorna al cliente.
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, 
										   HttpServletResponse response, FilterChain chain,
										   Authentication auth) throws IOException, ServletException {
		// Registra la autenticacion
		response.sendRedirect(Security.SUCCESS_URL);
	}

	/**
	 * Recupera los roles del usuario en la aplicacion actual.
	 * @param usuario
	 * @return
	 */
	private List<GrantedAuthority> getGrantedAuthorities(Usuario usuario){
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		if (usuario.getInterUsuarioRols() != null) {
			for(InterUsuarioRol privilegio : usuario.getInterUsuarioRols()){
				authorities.add(new SimpleGrantedAuthority(privilegio.getRol().getNombreRol()));
			}
		}

		return authorities;
	}

}