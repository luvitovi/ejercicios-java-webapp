package ec.gob.salinas.demo.modelo.pojo;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the sucursal database table.
 * 
 */
@Entity
@NamedQuery(name="Sucursal.findAll", query="SELECT s FROM Sucursal s")
public class Sucursal implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String direccion;

	private String email;

	private String estado;

	@Column(name="fecha_modifica")
	private Timestamp fechaModifica;

	@Column(name="nombre_sucursal")
	private String nombreSucursal;

	@Column(name="serie_establecimiento")
	private String serieEstablecimiento;

	private String telefono;

	@Column(name="usuario_ingresa")
	private String usuarioIngresa;

	//bi-directional many-to-one association to InterSucursalSoftware
	@OneToMany(mappedBy="sucursal")
	private List<InterSucursalSoftware> interSucursalSoftwares;

	//bi-directional many-to-one association to InterUsuarioSucursal
	@OneToMany(mappedBy="sucursal")
	private List<InterUsuarioSucursal> interUsuarioSucursals;

	//bi-directional many-to-one association to Empresa
	@ManyToOne
	@JoinColumn(name="id_empresa")
	private Empresa empresa;

	public Sucursal() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Timestamp getFechaModifica() {
		return this.fechaModifica;
	}

	public void setFechaModifica(Timestamp fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getNombreSucursal() {
		return this.nombreSucursal;
	}

	public void setNombreSucursal(String nombreSucursal) {
		this.nombreSucursal = nombreSucursal;
	}

	public String getSerieEstablecimiento() {
		return this.serieEstablecimiento;
	}

	public void setSerieEstablecimiento(String serieEstablecimiento) {
		this.serieEstablecimiento = serieEstablecimiento;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
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
		interSucursalSoftware.setSucursal(this);

		return interSucursalSoftware;
	}

	public InterSucursalSoftware removeInterSucursalSoftware(InterSucursalSoftware interSucursalSoftware) {
		getInterSucursalSoftwares().remove(interSucursalSoftware);
		interSucursalSoftware.setSucursal(null);

		return interSucursalSoftware;
	}

	public List<InterUsuarioSucursal> getInterUsuarioSucursals() {
		return this.interUsuarioSucursals;
	}

	public void setInterUsuarioSucursals(List<InterUsuarioSucursal> interUsuarioSucursals) {
		this.interUsuarioSucursals = interUsuarioSucursals;
	}

	public InterUsuarioSucursal addInterUsuarioSucursal(InterUsuarioSucursal interUsuarioSucursal) {
		getInterUsuarioSucursals().add(interUsuarioSucursal);
		interUsuarioSucursal.setSucursal(this);

		return interUsuarioSucursal;
	}

	public InterUsuarioSucursal removeInterUsuarioSucursal(InterUsuarioSucursal interUsuarioSucursal) {
		getInterUsuarioSucursals().remove(interUsuarioSucursal);
		interUsuarioSucursal.setSucursal(null);

		return interUsuarioSucursal;
	}

	public Empresa getEmpresa() {
		return this.empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

}