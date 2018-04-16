package ec.gob.salinas.demo.ws;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ec.gob.salinas.demo.modelo.pojo.Usuario;
import ec.gob.salinas.demo.repos.UsuarioRepo;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	private UsuarioRepo usuarioRepo;

	/**
	 * Ejemplo de uso del método GET para obtener un objeto.
	 * @param idUsuario
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Usuario obtieneUsuario(@PathVariable("id") Integer idUsuario) {
		return usuarioRepo.getOne(idUsuario);
	}

	/**
	 * Ejemplo de uso del método GET para obtener una lista.
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public List<Usuario> obtieneUsuarios() {
		return usuarioRepo.findAll();
	}

	/**
	 * Ejemplo de uso del método PUT
	 * @param usuario
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public ResponseEntity<?> actualizaUsuario(@RequestBody Usuario usuario) {
		if (usuario.getId() == null) {
			return ResponseEntity.notFound().build();
		}
		Usuario usuarioAux = usuarioRepo.getOne(usuario.getId());
		if (usuarioAux == null) {
			return ResponseEntity.notFound().build();
		}
		usuarioRepo.save(usuario);
		return ResponseEntity.ok().build();
	}

	/**
	 * Ejemplo de uso del método POST
	 * @param usuario
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<?> grabaUsuario(@RequestBody Usuario usuario) {
		usuarioRepo.save(usuario);
		return ResponseEntity.ok().build();
	}

	/**
	 * Ejemplo de uso del método DELETE
	 * @param idUsuario
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> borraUsuario(@PathVariable("id") Integer idUsuario) {
		Usuario usuario = usuarioRepo.getOne(idUsuario);
		if (usuario != null) {
			usuario.setEstado("X");
			usuarioRepo.save(usuario);
			return ResponseEntity.ok().build();
		}else{
			return ResponseEntity.notFound().build();
		}
	}

}
