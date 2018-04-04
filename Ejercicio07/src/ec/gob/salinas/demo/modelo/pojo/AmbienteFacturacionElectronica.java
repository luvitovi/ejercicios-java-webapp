package ec.gob.salinas.demo.modelo.pojo;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the ambiente_facturacion_electronica database table.
 * 
 */
@Entity
@Table(name="ambiente_facturacion_electronica")
@NamedQuery(name="AmbienteFacturacionElectronica.findAll", query="SELECT a FROM AmbienteFacturacionElectronica a")
public class AmbienteFacturacionElectronica implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="codigo_alterno")
	private Integer codigoAlterno;

	private String estado;

	@Column(name="fecha_ingrea")
	private Timestamp fechaIngrea;

	@Column(name="link_autorizacion_comprobante")
	private String linkAutorizacionComprobante;

	@Column(name="link_recepcion_comprobante")
	private String linkRecepcionComprobante;

	@Column(name="nombre_ambiente")
	private String nombreAmbiente;

	@Column(name="usuario_ingresa")
	private String usuarioIngresa;

	//bi-directional many-to-one association to Empresa
	@OneToMany(mappedBy="ambienteFacturacionElectronica")
	private List<Empresa> empresas;

	public AmbienteFacturacionElectronica() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCodigoAlterno() {
		return this.codigoAlterno;
	}

	public void setCodigoAlterno(Integer codigoAlterno) {
		this.codigoAlterno = codigoAlterno;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Timestamp getFechaIngrea() {
		return this.fechaIngrea;
	}

	public void setFechaIngrea(Timestamp fechaIngrea) {
		this.fechaIngrea = fechaIngrea;
	}

	public String getLinkAutorizacionComprobante() {
		return this.linkAutorizacionComprobante;
	}

	public void setLinkAutorizacionComprobante(String linkAutorizacionComprobante) {
		this.linkAutorizacionComprobante = linkAutorizacionComprobante;
	}

	public String getLinkRecepcionComprobante() {
		return this.linkRecepcionComprobante;
	}

	public void setLinkRecepcionComprobante(String linkRecepcionComprobante) {
		this.linkRecepcionComprobante = linkRecepcionComprobante;
	}

	public String getNombreAmbiente() {
		return this.nombreAmbiente;
	}

	public void setNombreAmbiente(String nombreAmbiente) {
		this.nombreAmbiente = nombreAmbiente;
	}

	public String getUsuarioIngresa() {
		return this.usuarioIngresa;
	}

	public void setUsuarioIngresa(String usuarioIngresa) {
		this.usuarioIngresa = usuarioIngresa;
	}

	public List<Empresa> getEmpresas() {
		return this.empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public Empresa addEmpresa(Empresa empresa) {
		getEmpresas().add(empresa);
		empresa.setAmbienteFacturacionElectronica(this);

		return empresa;
	}

	public Empresa removeEmpresa(Empresa empresa) {
		getEmpresas().remove(empresa);
		empresa.setAmbienteFacturacionElectronica(null);

		return empresa;
	}

}