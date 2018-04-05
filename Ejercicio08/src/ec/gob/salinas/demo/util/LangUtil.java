package ec.gob.salinas.demo.util;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.zkoss.lang.Library;
import org.zkoss.util.resource.Labels;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;

/**
 * Utilidades para gestion de lenguaje.
 * @author Luis
 *
 */
public class LangUtil {

	/**
	 * Retorna la cadena caracter del lenguaje de la aplicacion
	 * en la forma: lenguaje_PAIS
	 * Ejemplo: es_EC (Español, Ecuador)
	 * @return
	 */
	public static String getLanguageString() {
		Locale localidadSesion;
		Locale localidadRequerimiento;
		String localidadAplicacion;
		String retorno = "";
		// Si se definió en la sesion, retorna lo definido en la sesion
		if (Sessions.getCurrent().getAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE) != null) {
			localidadSesion = (Locale) Sessions.getCurrent().getAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE);
			retorno = localidadSesion.getLanguage();
			if (!localidadSesion.getCountry().isEmpty()) {
				retorno +=  "_" + localidadSesion.getCountry();
			}
		}else if (Library.getProperty(Attributes.PREFERRED_LOCALE) != null) {
			// Se se definio en la aplicacion.
			localidadAplicacion = Library.getProperty(Attributes.PREFERRED_LOCALE);
			retorno = localidadAplicacion;
		}else{
			// Si no se ha definido en los niveles anteriores 
			// retorna la definicion del reqierimiento HTTP.
			HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
			if (request != null) {
				localidadRequerimiento = request.getLocale();
				retorno = localidadRequerimiento.getLanguage();
				if (!localidadRequerimiento.getCountry().isEmpty()) {
					retorno +=  "_" + localidadRequerimiento.getCountry();
				}
			}
		}
		return retorno;
	}
	
	/**
	 * Retorna la cadena caracter del lenguaje de la aplicacion
	 * en la forma: lenguaje_PAIS
	 * Ejemplo: es_EC (Español, Ecuador)
	 * @return
	 */
	public static void setLanguageString(String cadenaLenguaje) {
		
		Locale localidad = null; 
		if (cadenaLenguaje == null) {
			cadenaLenguaje = "";
		}
		
		// Si no pasa una cadena vacia, coloca el lenguaje de la sesion.
		if (!cadenaLenguaje.isEmpty()) {
			localidad = cadenaLenguaje.length() > 2 ? 
					    new Locale(cadenaLenguaje.substring(0,2), cadenaLenguaje.substring(3)) : 
						new Locale(cadenaLenguaje);
			
		}
		
		Sessions.getCurrent().setAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE, localidad);
		Executions.getCurrent().sendRedirect("");
	}
	
	/**
	 * Recarga las etiquetas del lenguaje.
	 */
	public static void reloadLabels() {
		Labels.reset();
		Executions.getCurrent().sendRedirect("");
	}
}
