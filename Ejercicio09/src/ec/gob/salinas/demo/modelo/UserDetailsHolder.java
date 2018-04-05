package ec.gob.salinas.demo.modelo;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import ec.gob.salinas.demo.modelo.pojo.Usuario;
import lombok.Getter;
import lombok.Setter;

/**
 * Contiene los datos del usuario logoneado en:
 * SecurityContextHolder.getContext().getAuthentication().getPrincipal()
 * 
 * @author Luis
 *
 */
public class UserDetailsHolder extends User{

	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private Usuario usuario;
	
	public UserDetailsHolder(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
		super(usuario.getNombreUsuario(), usuario.getPassword(), true, true, true, true, authorities);
		this.usuario = usuario;
	}

}
