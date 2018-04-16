package ec.gob.salinas.demo.control.seg;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;

import ec.gob.salinas.demo.modelo.pojo.InterUsuarioRol;
import ec.gob.salinas.demo.modelo.pojo.Rol;
import ec.gob.salinas.demo.modelo.pojo.Usuario;
import ec.gob.salinas.demo.repos.InterUsuarioRolRepo;
import ec.gob.salinas.demo.repos.RolRepo;
import ec.gob.salinas.demo.repos.UsuarioRepo;
import lombok.Getter;
import lombok.Setter;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class Edita {
	
	@WireVariable
	private UsuarioRepo usuarioRepo;

	@WireVariable
	private RolRepo rolRepo;

	@WireVariable
	private InterUsuarioRolRepo interUsuarioRolRepo;

	@Getter @Setter private Usuario usuario;
	@Getter @Setter private Rol privilegio;
	@Getter @Setter private InterUsuarioRol privilegioSeleccionado;
	@Getter @Setter private String clave;
	@Getter @Setter private String claveConfirmacion;

	@Getter @Setter private List<InterUsuarioRol> privilegiosPorEliminar;

	@Init
	public void init(@ExecutionArgParam("USUARIO_EDITAR") Usuario usr) {
		if (usr == null) {
			usuario = new Usuario();
		}else{
			// Obtiene una copia para edicion.
			usuario = usuarioRepo.findById(usr.getId()).get();
		}
		privilegiosPorEliminar = new ArrayList<InterUsuarioRol>();
	}
	
	public List<Rol> getPrivilegios() {
		return rolRepo.findAll();
	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);
	}
	
	@Command
	@NotifyChange({"usuario"})
	public void agregaRol() {
		boolean encontro = false;
		if (usuario.getInterUsuarioRols() != null) {
			for (InterUsuarioRol privilegioAsignado : usuario.getInterUsuarioRols()) {
				if (privilegio.getNombreRol().equals(privilegioAsignado.getRol().getNombreRol())) {
					encontro = true;
					break;
				}
			}
		}
		if (!encontro) {
			InterUsuarioRol privilegioNuevo = new InterUsuarioRol();
			privilegioNuevo.setRol(privilegio);
			privilegioNuevo.setUsuario(usuario);
			usuario.getInterUsuarioRols().add(privilegioNuevo);
		}
	}

	@Command
	@NotifyChange({"usuario"})
	public void eliminaRol() {
		privilegioSeleccionado.setEstado("X");
		privilegiosPorEliminar.add(privilegioSeleccionado);
		usuario.getInterUsuarioRols().remove(privilegioSeleccionado);
	}

	@Command
	public void aceptar(@ContextParam(ContextType.VIEW) Component view) {
		
		if (clave != null && !clave.isEmpty()) {
			if (!clave.equals(claveConfirmacion)) {
				Clients.showNotification("Las claves no coinciden!");
				return;
			}
		}
		
		Messagebox.show("Desea grabar los cambios?",
				        "Confirmación",
				        Messagebox.YES | Messagebox.NO,
				        Messagebox.QUESTION,
				        new EventListener<Event>() {
							
							@Override
							public void onEvent(Event event) throws Exception {

								if (event.getName().equals("onYes")) {
									if (privilegiosPorEliminar.size() > 0) {
										for (InterUsuarioRol interUsuarioRol : privilegiosPorEliminar) {
											interUsuarioRolRepo.save(interUsuarioRol);
										}
									}
									if (clave != null && !clave.isEmpty()) {
										BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
										usuario.setPassword(bCryptPasswordEncoder.encode(clave));
									}
									usuarioRepo.save(usuario);
									Clients.showNotification("Transacción ejecutada con exito!");
									BindUtils.postGlobalCommand(null, null, "Lista.buscar", null);
									salir(view);
								}
							}
						}); 
		
	}
	
	@Command
	public void salir(@ContextParam(ContextType.VIEW) Component view) {
		view.detach();
	}

	
}
