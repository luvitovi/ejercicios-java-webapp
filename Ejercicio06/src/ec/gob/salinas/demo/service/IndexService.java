package ec.gob.salinas.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.gob.salinas.demo.modelo.pojo.Usuario;
import ec.gob.salinas.demo.repos.UsuarioRepo;

@Service
@Transactional
public class IndexService {
	
	@Autowired
	private UsuarioRepo usuarioRepo;
	
	public List<String> getNombresUsuario() {
		List<String> nombresUsuario = new ArrayList<String>();
		List<Usuario> usuarios = usuarioRepo.findAll();
		
		for (Usuario usuario : usuarios) {
			nombresUsuario.add(usuario.getNombreUsuario());
		}
		
		return nombresUsuario;
	}
	
}