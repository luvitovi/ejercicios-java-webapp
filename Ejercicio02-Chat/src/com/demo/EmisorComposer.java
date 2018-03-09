package com.demo;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;

/**
 * Controlador del formulario de envio.
 * @author Luis
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class EmisorComposer extends SelectorComposer {
	
	// Cola de eventos
	private EventQueue eq;
    
    // Autoenlace con los objetos del formulario
    @Wire
    Textbox txtEnvio;

    @Wire
    Button btn;

    // Constructor del formulario.
	public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
    }
     
    // Escucha el evento "onClick" del boton
    @Listen("onClick = button#btn; onOK = window#wndEmisor")
    public void enviaMensaje() {
    	
    	// Se conecta a la cola "chateo-ejemplo"
        eq = EventQueues.lookup("chateo-ejemplo", EventQueues.APPLICATION, true);
        
        // Envia un mensaje a la cola.
        eq.publish(new Event("onButtonClick", btn, txtEnvio.getValue()));
    }
}