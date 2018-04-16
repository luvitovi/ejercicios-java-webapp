package ec.gob.salinas.demo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ec.gob.salinas.demo.modelo.pojo.InterUsuarioRol;

@Repository
public interface InterUsuarioRolRepo extends JpaRepository<InterUsuarioRol, Integer> {
	
}
