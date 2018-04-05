package ec.gob.salinas.demo.control;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.collections4.map.HashedMap;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Iframe;

import ec.gob.salinas.demo.service.IndexService;
import ec.gob.salinas.demo.util.LangUtil;
import ec.gob.salinas.demo.util.ReportUtil;
import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JRException;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class Index {

	@Wire
	private Iframe frmVisualizador;
	
	@WireVariable
	private IndexService indexService;
	
	@Getter private AMedia reporte;
	@Getter @Setter private String nombreUsuario;
	@Getter @Setter private String lenguajeActual;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);
		lenguajeActual = LangUtil.getLanguageString();
	}

	@Command
	public void ejecutaReportePDF() throws SQLException, JRException, IOException, InterruptedException {
		Map<String, Object> parametros = new HashedMap<String, Object>();
		parametros.put("P_NOMBRE_USUARIO", nombreUsuario);
		parametros.put("P_NOMBRE_INSTITUCION", "EcuDesarrollo");
		parametros.put("P_PATH_LOGO", getPathReal("/img/logo.png"));
		ReportUtil.ejecutaReporte(indexService.getDataSource().getConnection(), 
								 getPathReal("/rep/usuarios.jasper"), 
								 "PDF", 
								 parametros);
		
		Clients.showNotification(Labels.getLabel("app.reporteEjecutadoExitosamente"));
	}

	@Command
	public void ejecutaReporteXLS() throws SQLException, JRException, IOException, InterruptedException {
		Map<String, Object> parametros = new HashedMap<String, Object>();
		parametros.put("P_NOMBRE_USUARIO", nombreUsuario);
		parametros.put("P_NOMBRE_INSTITUCION", "EcuDesarrollo");
		parametros.put("P_PATH_LOGO", getPathReal("/img/logo.png"));
		ReportUtil.ejecutaReporte(indexService.getDataSource().getConnection(), 
								 getPathReal("/rep/usuarios.jasper"), 
								 "XLS", 
								 parametros);

		Clients.showNotification(Labels.getLabel("app.reporteEjecutadoExitosamente"));

	}

	@Command
	@NotifyChange({"reporte"})
	public void visualizaReporte() throws SQLException, JRException, IOException, InterruptedException {
		Map<String, Object> parametros = new HashedMap<String, Object>();
		parametros.put("P_NOMBRE_USUARIO", nombreUsuario);
		parametros.put("P_NOMBRE_INSTITUCION", "EcuDesarrollo");
		parametros.put("P_PATH_LOGO", getPathReal("/img/logo.png"));
		File archivoReporte = ReportUtil.generaReporte(indexService.getDataSource().getConnection(), 
													  getPathReal("/rep/usuarios.jasper"), 
								 					  "PDF", 
								 					  parametros);
		reporte = new AMedia(archivoReporte, "application/pdf", null);

		Clients.showNotification(Labels.getLabel("app.reporteEjecutadoExitosamente"));

	}
	
	/**
	 * Obtiene el nombre real rel reporte
	 * @param reporte
	 * @return
	 */
	private String getPathReal(String reporte) {
		ServletContext servletContext = (ServletContext)Sessions.getCurrent().getWebApp().getServletContext();
		return servletContext.getRealPath(reporte);
	}

	/**
	 * Se ejecuta cuando se selecciona un lenguaje en la interfaz grafica.
	 */
	@Command
	public void seleccionaLenguaje() {
		LangUtil.setLanguageString(lenguajeActual);
	}

}
