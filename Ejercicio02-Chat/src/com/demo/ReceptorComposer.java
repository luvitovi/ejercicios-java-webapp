package com.demo;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;

/**
 * Controlador del formulario de recepcion
 * @author Luis
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class ReceptorComposer extends SelectorComposer {
	
	// Cola de eventos
    private EventQueue eq;
    
    // Enlace a objetos del formulario.
    @Wire
    private Textbox txtRecepcion;
    
    // Constructor del formulario.
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        
        // Se conecta a la cola "chateo-ejemplo".
        eq = EventQueues.lookup("chateo-ejemplo", EventQueues.APPLICATION, true);
        
        // Se subscribe para recibir los mensajes a medida que llegan
        eq.subscribe(new EventListener() {
        	
        		// Cuando llega un mensaje se dispara el evento.
            public void onEvent(Event event) throws Exception {
                String value = (String)event.getData();
                
                // Muestra el mensaje en pantalla.
                txtRecepcion.setValue(txtRecepcion.getValue() + value + "\n");
            }
        });
    }
}