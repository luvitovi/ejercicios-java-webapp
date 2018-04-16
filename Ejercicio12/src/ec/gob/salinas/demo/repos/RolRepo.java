package ec.gob.salinas.demo.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ec.gob.salinas.demo.modelo.pojo.Rol;

@Repository
public interface RolRepo extends JpaRepository<Rol, Integer> {
	
}
