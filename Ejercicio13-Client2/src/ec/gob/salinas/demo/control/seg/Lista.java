package ec.gob.salinas.demo.control.seg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import ec.gob.salinas.demo.modelo.pojo.Usuario;
import ec.gob.salinas.demo.repos.UsuarioRepo;
import lombok.Getter;
import lombok.Setter;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class Lista {

	@WireVariable
	private UsuarioRepo usuarioRepo;
	
	@Getter @Setter private List<Usuario> usuarios; 
	@Getter @Setter private String textoBuscar;
	@Getter @Setter private Usuario usuarioSeleccionado;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);
	}
	
	@GlobalCommand("Lista.buscar")
	@Command
	@NotifyChange({"usuarios", "usuarioSeleccionado"})
	public void buscar() {
//		usuarios = usuarioRepo.getUsuarios(textoBuscar == null ? "%" : 
//			                               "%" + textoBuscar + "%");
		
		usuarios = usuarioRepo.findByNombreUsuarioContainsIgnoreCase(textoBuscar);
		usuarioSeleccionado = null;
	}
	
	@Command
	public void eliminar() {
		Messagebox.show("Pregunta",
				        "¿Desea eliminar el usuario seleccionado?",
						Messagebox.YES | Messagebox.NO, 
				        Messagebox.QUESTION,
						new EventListener<Event>() {

							@Override
							public void onEvent(Event event) throws Exception {

								if (event.getName().equals("onYes")) {
									usuarioSeleccionado.setEstado("X");
									usuarioRepo.save(usuarioSeleccionado);
									Clients.showNotification("Usuario eliminado con éxito.");
									BindUtils.postGlobalCommand(null, null, "Lista.buscar", null);
								}
								
							}
						});
	}
	
	
	@Command
	public void nuevo() {
		Window formulario = (Window) Executions.createComponents("/seg/usuarioEdita.zul", 
				                                                 null, 
				                                                 null);
		formulario.doModal();
	}


	@Command
	public void editar() {
		Map<String, Object> parametros = new HashMap<String, Object>();
		
		parametros.put("USUARIO_EDITAR", usuarioSeleccionado);
		
		Window formulario = (Window) Executions.createComponents("/seg/usuarioEdita.zul", 
				                                                 null, 
				                                                 parametros);
		formulario.doModal();
	}

}
