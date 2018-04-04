package ec.gob.salinas.demo.modelo.pojo;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the inter_rol_menu database table.
 * 
 */
@Entity
@Table(name="inter_rol_menu")
@NamedQuery(name="InterRolMenu.findAll", query="SELECT i FROM InterRolMenu i")
public class InterRolMenu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String estado;

	@Column(name="fecha_ingresa")
	private Timestamp fechaIngresa;

	@Column(name="usuario_ingresa")
	private String usuarioIngresa;

	//bi-directional many-to-one association to Menu
	@ManyToOne
	@JoinColumn(name="id_menu")
	private Menu menu;

	//bi-directional many-to-one association to Rol
	@ManyToOne
	@JoinColumn(name="id_rol")
	private Rol rol;

	public InterRolMenu() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Timestamp getFechaIngresa() {
		return this.fechaIngresa;
	}

	public void setFechaIngresa(Timestamp fechaIngresa) {
		this.fechaIngresa = fechaIngresa;
	}

	public String getUsuarioIngresa() {
		return this.usuarioIngresa;
	}

	public void setUsuarioIngresa(String usuarioIngresa) {
		this.usuarioIngresa = usuarioIngresa;
	}

	public Menu getMenu() {
		return this.menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Rol getRol() {
		return this.rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

}