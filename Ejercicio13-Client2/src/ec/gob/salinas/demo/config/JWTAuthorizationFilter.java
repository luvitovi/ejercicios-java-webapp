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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import ec.gob.salinas.demo.modelo.pojo.InterUsuarioRol;
import ec.gob.salinas.demo.modelo.pojo.Usuario;
import ec.gob.salinas.demo.repos.UsuarioRepo;
import ec.gob.salinas.demo.util.CookieUtil;
import io.jsonwebtoken.Jwts;

/**
 * Valida la vigencia del token.
 * @author Luis
 *
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	
	private UsuarioRepo usuarioRepo;
	
	public JWTAuthorizationFilter(AuthenticationManager authManager, UsuarioRepo usuarioRepo) {
		super(authManager);
		this.usuarioRepo = usuarioRepo;
	}

	/**
	 * Verifica que el token este vigente.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest req,
								   HttpServletResponse res, 
								   FilterChain chain) throws IOException, ServletException {
		
		// Obtiene el encabezado (token)
		String header = CookieUtil.getValue(req, CookieUtil.COOKIE_NAME);
		if (header == null) {
			// Si no hay token y sigue autenticado, cierra la sesion.
			if (SecurityContextHolder.getContext().getAuthentication() != null && 
				SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
				SecurityContextHolder.clearContext();
				req.getSession().invalidate();
			}
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
		String token = CookieUtil.getValue(request, CookieUtil.COOKIE_NAME);
		
		if (token != null) {
			
			// Se recupera el usuario desde el token.
			// el método: parseClaimsJws verifica la validez del token.
			String user = Jwts.parser()
							  .setSigningKey(CookieUtil.MI_CLAVE_SUPER_SECRETA)
							  .parseClaimsJws(token.replace(Security.TOKEN_BEARER_PREFIX, ""))
							  .getBody()
							  .getSubject();

			// Si recupera el usuario, retorna un objeto para la actualización.
			if (user != null) {

				// Aqui es donde se debe recuperar el token desde la base para obtener el resto
				// de privilegios o determinar si no ha sido "desactivado".
				
				Usuario usuario = usuarioRepo.getUsuarioPorNombre(user);
				
				return new UsernamePasswordAuthenticationToken(usuario.getNombreUsuario(), null, getGrantedAuthorities(usuario));
			}
			return null;
		}
		return null;
	}

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