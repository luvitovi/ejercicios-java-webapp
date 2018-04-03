package ec.gob.salinas.demo.modelo.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the inter_usuario_rol database table.
 * 
 */
@Entity
@Table(name="inter_usuario_rol", schema="seguridad")
@AdditionalCriteria("this.estado IS NULL")
@NoArgsConstructor
public class InterUsuarioRol implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter @Setter private Integer id;

	@Getter @Setter private String estado;

	@Column(name="fecha_ingresa")
	@Getter @Setter private Timestamp fechaIngresa;

	@Column(name="usario_ingresa")
	@Getter @Setter private String usarioIngresa;

	@ManyToOne
	@JoinColumn(name="id_rol")
	@Getter @Setter private Rol rol;

	@ManyToOne
	@JoinColumn(name="id_usuario")
	@Getter @Setter private Usuario usuario;
	
	public String toString() {
		return rol.toString();
	}

}