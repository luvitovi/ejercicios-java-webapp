package ec.gob.salinas.demo.ws;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ec.gob.salinas.demo.modelo.pojo.Usuario;
import ec.gob.salinas.demo.repos.UsuarioRepo;

@RestController
@RequestMapping("/rentas/")
public class RentasController {

	/**
	 * Recupera una tasa
	 * @param idUsuario
	 * @return
	 */
	@Secured({"ROLE_RENTAS", "ROLE_EMISION"})
	@RequestMapping(value = "/emision/tasa/{id}", method = RequestMethod.GET)
	public TituloCredito obtieneTasa(@PathVariable("id") Integer idUsuario) {
		return usuarioRepo.getOne(idUsuario);
	}

	
}
