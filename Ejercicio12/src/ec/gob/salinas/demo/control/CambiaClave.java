package ec.gob.salinas.demo.control;

import java.sql.Timestamp;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;

import ec.gob.salinas.demo.modelo.pojo.Usuario;
import ec.gob.salinas.demo.repos.UsuarioRepo;
import lombok.Getter;
import lombok.Setter;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class CambiaClave {
	
	@WireVariable
	private UsuarioRepo usuarioRepo;
	
	@Getter @Setter private Usuario usuario;
	@Getter @Setter private String nombreUsuario;
	@Getter @Setter private String clave;
	@Getter @Setter private String confirmacionClave;
	@Getter @Setter public boolean habilitarCambio = false;

	@Init
	public void init(@QueryParam("token") String token, @ContextParam(ContextType.VIEW) Component view) {
		if (token == null || token.isEmpty()) {
			Clients.showNotification("Debe especificar el token.");
			return;
		}
		
		usuario = usuarioRepo.getUsuarioPorToken(token);
		if (usuario == null) {
			Clients.showNotification("No se encontró el token especificado.");
			return;
		}
		
		if (usuario.getFechaExpiracionToken().before(new Timestamp(System.currentTimeMillis()))) {
			Clients.showNotification("El token se encuentra vencido.");
			return;
		}
		
		habilitarCambio = true;
		
	}
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);
	}
	

	@Command
	public void aceptar(@ContextParam(ContextType.VIEW) Component view) {
		
		if (clave == null || clave.isEmpty()) {
			Clients.showNotification("Debe ingresar una clave.");
			return;
		}
		if (!clave.equals(confirmacionClave)) {
			Clients.showNotification("La clave no es igual a su confirmación.");
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
									
									BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
									usuario.setPassword(bCryptPasswordEncoder.encode(clave));
									usuario.setToken(null);
									usuario.setFechaExpiracionToken(null);
									
									usuarioRepo.save(usuario);
									
									Clients.showNotification("La clave ha sido cambiada con éxito.");
									
									salir(view);
									
								}
							}
						}); 
		
	}
	
	@Command
	public void salir(@ContextParam(ContextType.VIEW) Component view) {
		Executions.sendRedirect("/");
	}

}
