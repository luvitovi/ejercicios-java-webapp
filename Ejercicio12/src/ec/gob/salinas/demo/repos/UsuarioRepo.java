package ec.gob.salinas.demo.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ec.gob.salinas.demo.modelo.pojo.Usuario;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario, Integer> {

	@Query("SELECT u "
			+ "FROM Usuario AS u "
			+ "WHERE u.nombreUsuario = ?1 "
			)
	Usuario getUsuarioPorNombre(String nombreUsuario);

	@Query("SELECT u "
			+ "FROM Usuario AS u "
			+ "WHERE u.token = ?1 "
			)
	Usuario getUsuarioPorToken(String token);

	@Query("SELECT u.nombreUsuario "
			+ "FROM Usuario AS u "
			+ "WHERE u.estado = ?1 "
			)
	List<String> getUsuariosPorEstado(String estado);
	
	//TODO: Verificar el campo clave de este metodo.
	Usuario findFirstByNombreUsuarioAndPassword(String nombreUsuario, String clave);

	@Query("SELECT u "
			+ "FROM Usuario AS u "
			+ "WHERE u.nombreUsuario LIKE ?1 "
			+ "ORDER BY u.nombreUsuario ")
	List<Usuario> getUsuarios(String textoBuscar);
	

	List<Usuario> findByNombreUsuarioContainsIgnoreCase(String nombreUsuario);
	
}
