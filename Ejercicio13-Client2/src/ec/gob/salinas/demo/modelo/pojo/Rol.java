package ec.gob.salinas.demo.modelo.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the rol database table.
 * 
 */
@Entity
@Table(schema="seguridad")
@AdditionalCriteria("this.estado IS NULL")
@NoArgsConstructor
public class Rol implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private Integer id;

	@Getter @Setter private String estado;

	@Column(name="fecha_ingresa")
	@Getter @Setter private Timestamp fechaIngresa;

	@Column(name="nombre_rol")
	@Getter @Setter private String nombreRol;

	@Column(name="usuario_ingresa")
	@Getter @Setter private String usuarioIngresa;

	@OneToMany(mappedBy="rol")
	@Getter @Setter private List<InterRolMenu> interRolMenus;

	@OneToMany(mappedBy="rol")
	@Getter @Setter private List<InterSucursalSoftware> interSucursalSoftwares;

	@OneToMany(mappedBy="rol")
	@Getter @Setter private List<InterUsuarioRol> interUsuarioRols;
	
	public String toString() {
		return getNombreRol();
	}

}