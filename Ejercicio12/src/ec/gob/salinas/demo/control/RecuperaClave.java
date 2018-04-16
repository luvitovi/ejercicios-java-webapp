package ec.gob.salinas.demo.control;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;

import ec.gob.salinas.demo.modelo.pojo.Usuario;
import ec.gob.salinas.demo.repos.UsuarioRepo;
import ec.gob.salinas.demo.util.MailUtil;
import lombok.Getter;
import lombok.Setter;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class RecuperaClave {
	
	protected static final int MINUTOS_EXPIRACION_TOKEN = 5;

	@WireVariable
	private UsuarioRepo usuarioRepo;
	
	@Getter @Setter private String nombreUsuario;

	@Init
	public void init(@ExecutionArgParam("USUARIO_EDITAR") Usuario usr) {

	}
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);
	}
	

	@Command
	public void aceptar(@ContextParam(ContextType.VIEW) Component view) {
		
		if (nombreUsuario == null || nombreUsuario.isEmpty()) {
			Clients.showNotification("Debe ingresar un usuario.");
			return;
		}
		
		// Busca el usuario.
		
		Usuario usuario = usuarioRepo.getUsuarioPorNombre(nombreUsuario);
		
		if (usuario == null) {
			Clients.showNotification("El usuario " + nombreUsuario + " no se ha encontrado.");
			return;
		}

		if (usuario.getEmail() == null) {
			Clients.showNotification("El usuario " + nombreUsuario + " no tiene email.");
			return;
		}
		
		Messagebox.show("Desea enviar el correo de recuperación?",
				        "Confirmación",
				        Messagebox.YES | Messagebox.NO,
				        Messagebox.QUESTION,
				        new EventListener<Event>() {
							
							@Override
							public void onEvent(Event event) throws Exception {

								if (event.getName().equals("onYes")) {
									
									// Crea un token
									String token = UUID.randomUUID().toString();
									
									// Obtiene la fecha de expiracion
									Calendar fechaHora = Calendar.getInstance();
									fechaHora.setTimeInMillis(System.currentTimeMillis());
									fechaHora.add(Calendar.MINUTE, MINUTOS_EXPIRACION_TOKEN);
									
									usuario.setToken(token);
									usuario.setFechaExpiracionToken(new Timestamp(fechaHora.getTimeInMillis()));
									
									usuarioRepo.save(usuario);
									
									// Ensambla el contenido.
									
									StringBuffer contenido = new StringBuffer();
									String url = "http://localhost:8080/Ejercicio12/cambiaClave.zul?token=" + token;
									
									contenido.append("<p><h1>Recuperacion de clave</h1></p>");
									contenido.append("<p>De click en el siguiente enlace para recuperar la clave:</p>");
									contenido.append("<a href='" + url + "' target='_blank'>Recuperar la clave</a>");
									contenido.append("<p></p>");
									contenido.append("<p>El Administrador</p>");
									
									// Envia el correo.
									
									List<String> destinatarios = new ArrayList<String>();
									List<String> copia = new ArrayList<String>();
									List<String> copiaOculta = new ArrayList<String>();
									
									destinatarios.add(usuario.getEmail());
									
									List<File> archivosAdjuntos = new ArrayList<File>();
									archivosAdjuntos.add(new File(getPathReal("/doc/archivoAdjunto.pdf")));
									
									MailUtil.enviaCorreo(destinatarios, 
														copia, 
														copiaOculta, 
														"Recuperación de Contraseña", 
														contenido.toString(), 
														archivosAdjuntos);

									Clients.showNotification("El correo ha sido enviado con éxito, por favor revise su cuenta de correo.");
									
									salir(view);
									
								}
							}
						}); 
		
	}
	
	@Command
	public void salir(@ContextParam(ContextType.VIEW) Component view) {
		view.detach();
	}

	/**
	 * Obtiene el nombre real rel reporte
	 * @param reporte
	 * @return
	 */
	private String getPathReal(String archivo) {
		ServletContext servletContext = (ServletContext)Sessions.getCurrent().getWebApp().getServletContext();
		return servletContext.getRealPath(archivo);
	}


}
