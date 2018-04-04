package ec.gob.salinas.demo.modelo.pojo;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the empresa database table.
 * 
 */
@Entity
@NamedQuery(name="Empresa.findAll", query="SELECT e FROM Empresa e")
public class Empresa implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="contribuyente_especial")
	private String contribuyenteEspecial;

	private String direccion;

	private String email;

	private String estado;

	@Column(name="fecha_ingresa")
	private Timestamp fechaIngresa;

	@Column(name="nombre_empresa")
	private String nombreEmpresa;

	@Column(name="numero_contribuyente_especial")
	private String numeroContribuyenteEspecial;

	private String ruc;

	private String telefono;

	@Column(name="usuario_ingresa")
	private String usuarioIngresa;

	//bi-directional many-to-one association to AmbienteFacturacionElectronica
	@ManyToOne
	@JoinColumn(name="id_ambiente_facturacion_electronica")
	private AmbienteFacturacionElectronica ambienteFacturacionElectronica;

	//bi-directional many-to-one association to Sucursal
	@OneToMany(mappedBy="empresa")
	private List<Sucursal> sucursals;

	public Empresa() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContribuyenteEspecial() {
		return this.contribuyenteEspecial;
	}

	public void setContribuyenteEspecial(String contribuyenteEspecial) {
		this.contribuyenteEspecial = contribuyenteEspecial;
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

	public Timestamp getFechaIngresa() {
		return this.fechaIngresa;
	}

	public void setFechaIngresa(Timestamp fechaIngresa) {
		this.fechaIngresa = fechaIngresa;
	}

	public String getNombreEmpresa() {
		return this.nombreEmpresa;
	}

	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}

	public String getNumeroContribuyenteEspecial() {
		return this.numeroContribuyenteEspecial;
	}

	public void setNumeroContribuyenteEspecial(String numeroContribuyenteEspecial) {
		this.numeroContribuyenteEspecial = numeroContribuyenteEspecial;
	}

	public String getRuc() {
		return this.ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
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

	public AmbienteFacturacionElectronica getAmbienteFacturacionElectronica() {
		return this.ambienteFacturacionElectronica;
	}

	public void setAmbienteFacturacionElectronica(AmbienteFacturacionElectronica ambienteFacturacionElectronica) {
		this.ambienteFacturacionElectronica = ambienteFacturacionElectronica;
	}

	public List<Sucursal> getSucursals() {
		return this.sucursals;
	}

	public void setSucursals(List<Sucursal> sucursals) {
		this.sucursals = sucursals;
	}

	public Sucursal addSucursal(Sucursal sucursal) {
		getSucursals().add(sucursal);
		sucursal.setEmpresa(this);

		return sucursal;
	}

	public Sucursal removeSucursal(Sucursal sucursal) {
		getSucursals().remove(sucursal);
		sucursal.setEmpresa(null);

		return sucursal;
	}

}