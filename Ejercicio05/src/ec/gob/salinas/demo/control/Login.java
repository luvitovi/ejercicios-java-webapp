package ec.gob.salinas.demo.control;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import ec.gob.salinas.demo.repos.UsuarioRepo;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class Login {

	@WireVariable
	private UsuarioRepo usuarioRepo;
		
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);
	}
	
}
