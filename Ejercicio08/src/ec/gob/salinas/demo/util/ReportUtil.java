package ec.gob.salinas.demo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import org.zkoss.zul.Filedownload;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

/**
 * Utilidades para gestión de reportes.
 * @author Luis
 *
 */
public class ReportUtil {

	/**
	 * Ejecuta un reporte y lo genera en el formato especificado, para luego enviarlo a la descarga.
	 * 
	 * @param pathReporte EL path del reporte (relativo a la raiz de la aplicación) incluyendo el nombre.
	 * @param formato El formato puede ser: GlobalConstant.FORMATO_PDF o GlobalConstant.FORMATO_XLS
	 * @param parametros Mapa de parametros del tipo: Map<String, Object> 
	 * @throws SQLException 
	 * @throws JRException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void ejecutaReporte(Connection conexionBaseDatos, String pathReporte, 
									 String formato, Map<String, Object> parametros) throws SQLException, JRException, IOException, InterruptedException {
		Filedownload.save(generaReporte(conexionBaseDatos, pathReporte, formato, parametros), formato); 
	}

	/**
	 * Genera un reporte en el formato especificado retornando un objeto File del archivo generado.
	 * 
	 * @param pathReporte EL path del reporte (relativo a la raiz de la aplicación) incluyendo el nombre.
	 * @param formato El formato puede ser: GlobalConstant.FORMATO_PDF o GlobalConstant.FORMATO_XLS
	 * @param parametros Mapa de parametros del tipo: Map<String, Object> 
	 * @throws SQLException 
	 * @throws JRException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static File generaReporte(Connection conexionBaseDatos, 
									String pathReporte, 
									String formato, 
									Map<String, Object> parametros) throws SQLException, JRException, IOException, InterruptedException {

		String nombreArchivo = "/tmp/" + UUID.randomUUID().toString();
		
					
		if (formato.equals("PDF")) {
			nombreArchivo += ".pdf";
			byte[] b = null;
			b = JasperRunManager.runReportToPdf(pathReporte, parametros, conexionBaseDatos);
			FileOutputStream fos = new FileOutputStream(nombreArchivo);
			fos.write(b);
			fos.close();
			// Para asegurar que el reporte este almacenado en el disco.
			fos.flush();
		}else{
			nombreArchivo += ".xls";
	        JasperPrint jasperPrint = JasperFillManager.fillReport(pathReporte, parametros, conexionBaseDatos);
			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(nombreArchivo));
			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			configuration.setOnePagePerSheet(true);
			configuration.setDetectCellType(true);
			configuration.setCollapseRowSpan(false);
			exporter.setConfiguration(configuration);
			exporter.exportReport();
		}
	
		return new File(nombreArchivo); 
			
	}

}
