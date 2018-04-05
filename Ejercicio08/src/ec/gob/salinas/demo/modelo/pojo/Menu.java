package ec.gob.salinas.demo.modelo.pojo;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the menu database table.
 * 
 */
@Entity
@NamedQuery(name="Menu.findAll", query="SELECT m FROM Menu m")
public class Menu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String estado;

	@Column(name="fecha_ingresa")
	private Timestamp fechaIngresa;

	@Column(name="nombre_menu")
	private String nombreMenu;

	@Column(name="opcion_padre")
	private Integer opcionPadre;

	@Column(name="url_imagen")
	private String urlImagen;

	@Column(name="url_menu")
	private String urlMenu;

	@Column(name="usuario_ingresa")
	private String usuarioIngresa;

	//bi-directional many-to-one association to InterRolMenu
	@OneToMany(mappedBy="menu")
	private List<InterRolMenu> interRolMenus;

	//bi-directional many-to-one association to Software
	@ManyToOne
	@JoinColumn(name="id_software")
	private Software software;

	public Menu() {
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

	public String getNombreMenu() {
		return this.nombreMenu;
	}

	public void setNombreMenu(String nombreMenu) {
		this.nombreMenu = nombreMenu;
	}

	public Integer getOpcionPadre() {
		return this.opcionPadre;
	}

	public void setOpcionPadre(Integer opcionPadre) {
		this.opcionPadre = opcionPadre;
	}

	public String getUrlImagen() {
		return this.urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}

	public String getUrlMenu() {
		return this.urlMenu;
	}

	public void setUrlMenu(String urlMenu) {
		this.urlMenu = urlMenu;
	}

	public String getUsuarioIngresa() {
		return this.usuarioIngresa;
	}

	public void setUsuarioIngresa(String usuarioIngresa) {
		this.usuarioIngresa = usuarioIngresa;
	}

	public List<InterRolMenu> getInterRolMenus() {
		return this.interRolMenus;
	}

	public void setInterRolMenus(List<InterRolMenu> interRolMenus) {
		this.interRolMenus = interRolMenus;
	}

	public InterRolMenu addInterRolMenus(InterRolMenu interRolMenus) {
		getInterRolMenus().add(interRolMenus);
		interRolMenus.setMenu(this);

		return interRolMenus;
	}

	public InterRolMenu removeInterRolMenus(InterRolMenu interRolMenus) {
		getInterRolMenus().remove(interRolMenus);
		interRolMenus.setMenu(null);

		return interRolMenus;
	}

	public Software getSoftware() {
		return this.software;
	}

	public void setSoftware(Software software) {
		this.software = software;
	}

}