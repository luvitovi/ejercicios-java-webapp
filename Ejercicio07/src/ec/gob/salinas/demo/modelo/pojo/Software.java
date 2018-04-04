package ec.gob.salinas.demo.modelo.pojo;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the software database table.
 * 
 */
@Entity
@NamedQuery(name="Software.findAll", query="SELECT s FROM Software s")
public class Software implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String estado;

	@Column(name="fecha_ingresa")
	private Timestamp fechaIngresa;

	@Column(name="nombre_software")
	private String nombreSoftware;

	@Column(name="usuario_ingresa")
	private String usuarioIngresa;

	//bi-directional many-to-one association to InterSucursalSoftware
	@OneToMany(mappedBy="software")
	private List<InterSucursalSoftware> interSucursalSoftwares;

	//bi-directional many-to-one association to Menu
	@OneToMany(mappedBy="software")
	private List<Menu> menus;

	public Software() {
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

	public String getNombreSoftware() {
		return this.nombreSoftware;
	}

	public void setNombreSoftware(String nombreSoftware) {
		this.nombreSoftware = nombreSoftware;
	}

	public String getUsuarioIngresa() {
		return this.usuarioIngresa;
	}

	public void setUsuarioIngresa(String usuarioIngresa) {
		this.usuarioIngresa = usuarioIngresa;
	}

	public List<InterSucursalSoftware> getInterSucursalSoftwares() {
		return this.interSucursalSoftwares;
	}

	public void setInterSucursalSoftwares(List<InterSucursalSoftware> interSucursalSoftwares) {
		this.interSucursalSoftwares = interSucursalSoftwares;
	}

	public InterSucursalSoftware addInterSucursalSoftware(InterSucursalSoftware interSucursalSoftware) {
		getInterSucursalSoftwares().add(interSucursalSoftware);
		interSucursalSoftware.setSoftware(this);

		return interSucursalSoftware;
	}

	public InterSucursalSoftware removeInterSucursalSoftware(InterSucursalSoftware interSucursalSoftware) {
		getInterSucursalSoftwares().remove(interSucursalSoftware);
		interSucursalSoftware.setSoftware(null);

		return interSucursalSoftware;
	}

	public List<Menu> getMenus() {
		return this.menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	public Menu addMenus(Menu menus) {
		getMenus().add(menus);
		menus.setSoftware(this);

		return menus;
	}

	public Menu removeMenus(Menu menus) {
		getMenus().remove(menus);
		menus.setSoftware(null);

		return menus;
	}

}