package ec.gob.salinas.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.gob.salinas.demo.modelo.UserDetailsHolder;
import ec.gob.salinas.demo.modelo.pojo.InterUsuarioRol;
import ec.gob.salinas.demo.modelo.pojo.Usuario;
import ec.gob.salinas.demo.repos.UsuarioRepo;

/**
 * Servicio para gestión de detalles de usuario en el proceso de login.
 * 
 * @author Luis
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UsuarioRepo usuarioRepo;
     
    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String username)
    		throws UsernameNotFoundException {
    	Usuario usuario = usuarioRepo.getUsuarioPorNombre(username);
    	if(usuario == null){
    		throw new UsernameNotFoundException("Usuario no encontrado.");
    	}
    	return new UserDetailsHolder(usuario, getGrantedAuthorities(usuario));
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
