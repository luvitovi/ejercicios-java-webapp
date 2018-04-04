package ec.gob.salinas.demo.service;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.gob.salinas.demo.modelo.pojo.Usuario;
import ec.gob.salinas.demo.repos.UsuarioRepo;

@Service
@Transactional
public class UsuarioService {

	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private UsuarioRepo usuarioRepo;
	
	public void refresh(Usuario usuario) {
		if (entityManager.contains(usuario)) {
			entityManager.refresh(usuario);
		}else{
			// Si el em no contiene el objeto, intenta reasociarlo.
			try {
				usuario = entityManager.find(usuario.getClass(), usuario.getId());
				entityManager.refresh(usuario);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void save(Usuario usuario) {
		usuarioRepo.save(usuario);
	}

}
