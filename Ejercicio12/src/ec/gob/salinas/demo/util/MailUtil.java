package ec.gob.salinas.demo.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class MailUtil {
	
	// Estas constantes deberían ser parte de la configuración del sistema.
	private static final Boolean MAIL_SERVER_HABILITADO = true;
	private static final String APP_MAIL_SERVER = "smtp.gmail.com";
	private static final String APP_MAIL_PORT = "465";
	private static final Boolean APP_MAIL_SEGURIDAD_SSL = true;
	private static final String APP_MAIL_USUARIO = "<MiUsuario>";
	private static final String APP_MAIL_CLAVE = "<MiClave>";
	private static final String APP_INSTITUCION_LOGO = null;

	/**
	 * Envia un correo simple a un solo destinatario sin adjuntos ni copias.
	 * @param direccionEmail
	 * @param asunto
	 * @param contenido
	 * @throws Exception
	 */
	public static void enviaCorreo(String direccionEmail, String asunto, String contenido) throws Exception {
		List<String> destinatarios = new ArrayList<String>();
		destinatarios.add(direccionEmail);
		enviaCorreo(destinatarios, null, null, asunto, contenido, null);
	}

	/**
	 * Envía un correo electrónico.
	 * @param destinatarios
	 * @param copias
	 * @param copiasOcultas
	 * @param asunto
	 * @param contenido
	 * @param archivosAdjuntos
	 * @throws EdException
	 */
	public static void enviaCorreo(List<String> destinatarios, 
								   List<String> copias, 
								   List<String> copiasOcultas, 
								   String asunto, 
								   String contenido, 
								   List<File> archivosAdjuntos) throws Exception { 
		
		// Si no esta habilitado el servicio, sale.
		if (!MAIL_SERVER_HABILITADO) {
			throw new Exception("El servicio de envío de mensajes por e-mail no está habilitado."); 
		}
		
		// Verifica la existencia de los parametros.
		if (APP_MAIL_SERVER.isEmpty()) {
			throw new Exception("No se ha definido el parámetro: APP_MAIL_SERVER");
		}
		if (APP_MAIL_PORT.isEmpty()) {
			throw new Exception("No se ha definido el parámetro: APP_MAIL_PORT");
		}
		if (APP_MAIL_SEGURIDAD_SSL == null) {
			throw new Exception("No se ha definido el parámetro: APP_MAIL_SEGURIDAD_SSL");
		}
		if (APP_MAIL_USUARIO.isEmpty()) {
			throw new Exception("No se ha definido el parámetro: APP_MAIL_USUARIO");
		}
		if (APP_MAIL_CLAVE.isEmpty()) {
			throw new Exception("No se ha definido el parámetro: APP_MAIL_CLAVE");
		}
		
		// SI no hay destinatarios genera un error.
		
		if (destinatarios == null || destinatarios.isEmpty()) {
			throw new Exception("No se han definido destinatarios.");
		}

		if (asunto == null || asunto.isEmpty()) {
			throw new Exception("No se ha definido el asunto.");
		}

		if (contenido == null || contenido.isEmpty()) {
			throw new Exception("No se ha definido el contenido.");
		}
		
		// Uso de Apache Commons eMail: https://commons.apache.org/proper/commons-email/ 

		HtmlEmail email = new HtmlEmail();
		try {
			
			// Imagen de cabecera del correo 
			
			String contenidoImagen = "";
			if (APP_INSTITUCION_LOGO != null) {
				File imagenHeader = new File(APP_INSTITUCION_LOGO);
				String cid = email.embed(imagenHeader, "header-email");
				contenidoImagen = "<tr><td><img height=\"150\" src=\"cid:"+cid+"\"></td></tr>";
			}
						
			//inicio contenido html
			String contenidoHtml = "<html><body><table width=\"700\">";
			
			//se anade imagen al contenido del correo
			contenidoHtml = contenidoHtml + contenidoImagen + contenido;
			
			//inicio contenido html			
			contenidoHtml = contenidoHtml + "</table></body></html>";
			
			// Propiedades del server 
			email.setHostName(APP_MAIL_SERVER);
			email.setSmtpPort(Integer.parseInt(APP_MAIL_PORT));
			email.setSSLOnConnect(APP_MAIL_SEGURIDAD_SSL);
			
			// Credenciales de acceso
			email.setAuthenticator(new DefaultAuthenticator(APP_MAIL_USUARIO, APP_MAIL_CLAVE));

			// Correo
			email.setFrom(APP_MAIL_USUARIO);
			
			for (String destinatario : destinatarios) {
				email.addTo(destinatario);
			}

			if (copias != null) {
				for (String copia : copias) {
					email.addCc(copia);
				}
			}

			if (copiasOcultas != null) {
				for (String copiaOculta : copiasOcultas) {
					email.addBcc(copiaOculta);
				}
			}

			email.setSubject(asunto);

			email.setHtmlMsg(contenidoHtml);
			
			// Adjuntos
			if (archivosAdjuntos != null) {
				for (File archivo : archivosAdjuntos) {
					email.attach(archivo);
				}
			}
			
			// Envio
			email.send();

		} catch (EmailException e) {
			e.printStackTrace();
			throw new Exception("No se pudo enviar el correo:" + e.getMessage());
		}
	}

}
