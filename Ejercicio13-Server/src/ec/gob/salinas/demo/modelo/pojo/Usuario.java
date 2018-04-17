package ec.gob.salinas.demo.modelo.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
@Table(schema="seguridad")
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u")
@AdditionalCriteria("this.estado IS NULL")
@NoArgsConstructor
@Cacheable(false)
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private Integer id;

	@Getter @Setter private String bloqueado;

	@Getter @Setter private String estado;

	@Column(name="fecha_ingresa")
	@Getter @Setter private Timestamp fechaIngresa;

	@Column(name="fecha_ultima_sesion")
	@Getter @Setter private Timestamp fechaUltimaSesion;

	@Column(name="id_personas")
	@Getter @Setter private Integer idPersonas;

	@Column(name="ip_ultima_sesion")
	@Getter @Setter private String ipUltimaSesion;

	@Column(name="nombre_usuario")
	@Getter @Setter private String nombreUsuario;

	@Getter @Setter private String password;

	@Getter @Setter private String token;

	@Column(name="usuario_ingresa")
	@Getter @Setter private String usuarioIngresa;

	@JsonIgnore
	@OneToMany(mappedBy="usuario")
	@Getter @Setter private List<InterUsuarioRol> interUsuarioRols;

//	@OneToMany(mappedBy="usuario")
//	@JsonIgnore
//	@Getter @Setter private List<InterUsuarioSucursal> interUsuarioSucursals;

}