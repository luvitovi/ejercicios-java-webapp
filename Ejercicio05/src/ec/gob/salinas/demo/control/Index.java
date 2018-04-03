package ec.gob.salinas.demo.control;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Include;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menubar;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;

import ec.gob.salinas.demo.modelo.UserDetailsHolder;
import ec.gob.salinas.demo.modelo.pojo.Opcion;
import ec.gob.salinas.demo.modelo.pojo.Usuario;
import ec.gob.salinas.demo.util.SecurityUtil;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
@SuppressWarnings({ "unchecked", "rawtypes" })
public class Index {

	private static final String ATRIBUTO_OPCION = "__OPCION__";
	private static final String ATRIBUTO_TAB = "__TAB__";

	@Wire
	private Menubar mnMenu;
	
	@Wire
	private Tabbox tbAreaTrabajo;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);
		
		creaMenu();
		
	}
	
	private void creaMenu() {
		List<Opcion> opciones = getOpcionesMenu();

		for (Opcion menu : opciones) {
			if (menu.getOpciones() == null || menu.getOpciones().size() == 0) {
				mnMenu.appendChild(getMenuItem(menu));
			}else{
				mnMenu.appendChild(getMenu(menu));
			}
		}

		
	}

	private Menu getMenu(Opcion opcion) {
		Menu retorno = new Menu();

		if (opcion.getUrlImagen() != null) {
			retorno.setIconSclass(opcion.getUrlImagen());
		}
		retorno.setLabel(opcion.getNombreMenu());
		retorno.setAttribute(ATRIBUTO_OPCION, opcion);
	
		retorno.appendChild(new Menupopup());
		for (Opcion subOpcion : opcion.getOpciones()) {
			if (subOpcion.getOpciones() == null || subOpcion.getOpciones().size() == 0) {
				retorno.getMenupopup().appendChild(getMenuItem(subOpcion));
			}else{
				retorno.getMenupopup().appendChild(getMenu(subOpcion));
			}
		}
		return retorno;
	}

	private Menuitem getMenuItem(Opcion opcion) {
		Menuitem retorno = new Menuitem();
		
		if (opcion.getUrlImagen() != null) {
			retorno.setIconSclass(opcion.getUrlImagen());
		}
		retorno.setLabel(opcion.getNombreMenu());
		retorno.setAttribute(ATRIBUTO_OPCION, opcion);
		
		retorno.addEventListener("onClick", new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				Menuitem menuItem = (Menuitem) event.getTarget();
				Opcion opcion;
				opcion = (Opcion) menuItem.getAttribute(ATRIBUTO_OPCION);
				if (opcion.getUrlMenu() != null) {
					cargaAreaTrabajo(opcion.getUrlMenu(), opcion.getNombreMenu(), opcion.getUrlImagen());
				}
			}
		});
		
		return retorno;
	}

	private void cargaAreaTrabajo(String formulario, String titulo, String icono) {
		// Si la cadena comienza con HTTP inicia el formulario en una nueva ventana.
		if (formulario == null || formulario.isEmpty() || "#".equals(formulario)) {
			return;
		}
		if (formulario.trim().toLowerCase().substring(0, 4).equals("http")) {
			Executions.getCurrent().sendRedirect(formulario, "_blank");
		}else{
			
			// Busca si el formulario ya ha sido cargado.
			
			boolean encontrado = false;
			
			for (Component cmp1 : tbAreaTrabajo.getTabpanels().getChildren()) {
				Tabpanel tp = (Tabpanel) cmp1;
				for (Component cmp2 : tp.getChildren()) {
					if (cmp2 instanceof Include) {
						Include inc = (Include) cmp2;
						if (inc.getSrc().equals(formulario)) {
							Tab tb = (Tab) tp.getAttribute(ATRIBUTO_TAB);
							tb.setSelected(true);
							encontrado = true;
							break;
						}
					}
				} 
				if (encontrado) {
					break;
				}
			}
			
			if (!encontrado) {
				Tab tab = new Tab();
				Tabpanel tabpanel = new Tabpanel();
				Include include = new Include();
				
				tab.setLabel(titulo);
				if (icono != null) {
					tab.setIconSclass(icono);
				}
				tab.setClosable(true);
				tab.setSelected(true);
				include.setSrc(formulario);
				tabpanel.getChildren().add(include);
				tabpanel.setAttribute(ATRIBUTO_TAB, tab);
				tabpanel.setSclass("p-2");
				
				tbAreaTrabajo.getTabs().appendChild(tab);
				tbAreaTrabajo.getTabpanels().appendChild(tabpanel);
			}
			
		}
	}	
	
	
	/**
	 * Simula la obtención de las opciones de menu desde la base.
	 * @return
	 */
	private List<Opcion> getOpcionesMenu() {
		List<Opcion> retorno = new ArrayList<Opcion>();
		retorno.addAll(getMenuDefecto());
		if (SecurityUtil.isAnyGranted("ROLE_ADMINISTRADORES")) {
			retorno.addAll(getMenuAdministrador());
		}
		if (SecurityUtil.isAnyGranted("ROLE_USUARIOS")) {
			retorno.addAll(getMenuUsuario());
		}
		return retorno;
	}

	private List<Opcion> getMenuUsuario() {
		List<Opcion> retorno = new ArrayList<Opcion>();
		Opcion opcionPadre = new Opcion();
		opcionPadre.setNombreMenu("Operaciones");
		opcionPadre.setOpciones(new ArrayList<Opcion>());
		
		Opcion menuIngreso1 = new Opcion();
		menuIngreso1.setNombreMenu("Operaciones 1");
		menuIngreso1.setUrlMenu("/usr/operaciones1.zul");
		menuIngreso1.setMenuPadre(opcionPadre);
		Opcion menuIngreso2 = new Opcion();
		menuIngreso2.setNombreMenu("Operaciones 2");
		menuIngreso2.setUrlMenu("/usr/operaciones2.zul");
		menuIngreso2.setMenuPadre(opcionPadre);
		
		opcionPadre.getOpciones().add(menuIngreso1);
		opcionPadre.getOpciones().add(menuIngreso2);
		
		retorno.add(opcionPadre);
		
		return retorno;
	}

	private List<Opcion> getMenuAdministrador() {
		List<Opcion> retorno = new ArrayList<Opcion>();
		Opcion opcionPadre = new Opcion();
		opcionPadre.setNombreMenu("Administración");
		opcionPadre.setOpciones(new ArrayList<Opcion>());
		
		Opcion menuIngreso1 = new Opcion();
		menuIngreso1.setNombreMenu("Usuarios");
		menuIngreso1.setUrlMenu("/seg/usuarioLista.zul");
		menuIngreso1.setMenuPadre(opcionPadre);
		
		opcionPadre.getOpciones().add(menuIngreso1);
		retorno.add(opcionPadre);
		
		return retorno;
	}

	private List<Opcion> getMenuDefecto() {
		List<Opcion> retorno = new ArrayList<Opcion>();
		Opcion opcionPadre = new Opcion();
		opcionPadre.setNombreMenu("Opciones por Defecto");
		opcionPadre.setOpciones(new ArrayList<Opcion>());
		
		Opcion menuIngreso1 = new Opcion();
		menuIngreso1.setNombreMenu("Defecto 1");
		menuIngreso1.setUrlMenu("/def/defecto1.zul");
		menuIngreso1.setMenuPadre(opcionPadre);
		Opcion menuIngreso2 = new Opcion();
		menuIngreso2.setNombreMenu("Defecto 2");
		menuIngreso2.setUrlMenu("/def/defecto2.zul");
		menuIngreso2.setMenuPadre(opcionPadre);
		
		opcionPadre.getOpciones().add(menuIngreso1);
		opcionPadre.getOpciones().add(menuIngreso2);
		retorno.add(opcionPadre);
		
		return retorno;
	}

	/**
	 * Retorna el usuario conectado!
	 * @return
	 */
	public Usuario getUsuario() {
		Usuario retorno = null;
		if (SecurityContextHolder.getContext() != null && 
			SecurityContextHolder.getContext().getAuthentication() != null && 
			SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetailsHolder) {
			retorno = ((UserDetailsHolder) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsuario();
		}

		return retorno;
		
	}

	public List<GrantedAuthority> getPrivilegios() {
		List<GrantedAuthority> retorno = new ArrayList<GrantedAuthority>();
		if (SecurityContextHolder.getContext() != null && 
			SecurityContextHolder.getContext().getAuthentication() != null && 
			SecurityContextHolder.getContext().getAuthentication().getAuthorities() != null ) {
			retorno = (List<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		}

		return retorno;
		
	}

	public boolean isAdministrator() {
		
//		for (GrantedAuthority privilegio : getPrivilegios()) {
//			if (privilegio.getAuthority().equals("ROLE_ADMINISTRADORES")) {
//				return true;
//			}
//		}
				
		if (SecurityUtil.isAnyGranted("ROLE_ADMINISTRADORES")) {
			return true;
		}
		return false;
	}

}
