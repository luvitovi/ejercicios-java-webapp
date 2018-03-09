package com.demo.control;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

/**
 * Clase de control del menu.
 * @author Luis
 *
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class Menu extends SelectorComposer {

	// Enlace con los objetos del formulario
	@Wire 
	Grid grMenu;
	
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		
		Rows filas = grMenu.getRows();

		filas.appendChild(getFilaMenu("ZK", "/img/zklogo.png", "http://www.zkoss.org"));
		filas.appendChild(getFilaMenu("ZK Demo", "/img/zkdemo.png", "http://www.zkoss.org"));
		filas.appendChild(getFilaMenu("ZK Doc", "/img/libro.png", "http://www.zkoss.org"));
		filas.appendChild(getFilaMenu("EcuDesarrollo", "/img/logo.png", "http://www.ecudesarrollo.com"));
		
	}
	
	/**
	 * Retorna un objeto de tipo "row" construido en base a los parametros.
	 * @param titulo
	 * @param imagen
	 * @param url
	 * @return
	 */
	private Row getFilaMenu(String titulo, String imagen, String url) {
		
		Row fila = new Row();
		
		Image img = new Image(imagen);
		Label etiqueta = new Label(titulo);
		
		img.setWidth("50px");
		
		fila.appendChild(img);
		fila.appendChild(etiqueta);
		
		// Listener que escuchar el evento
		EventListener<Event> listener = new SerializableEventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Executions.getCurrent().sendRedirect(url);
					}
		};
		
		// Asocia el listener al evento.
		fila.addEventListener(Events.ON_CLICK, listener);
		
		return fila;
	}

}
