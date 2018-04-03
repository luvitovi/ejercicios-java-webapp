package com.demo.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;

import com.demo.modelo.Persona;

/**
 * Clase de control del formulario persona.zul
 * @author Luis
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class PersonaControl extends SelectorComposer {
	
	// Enlace a los objetos del formulario.
	@Wire 
	Label numero;
	
	@Wire 
	Textbox nombre;
	
	@Wire 
	Textbox email;

	@Wire 
	Datebox fecNacimiento;

	@Wire 
	Listbox pais;

	@Wire 
	Textbox biografia;
	
	Persona persona;
	
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		ListModelList<String> countryModel = new ListModelList<String>(gatPaises());
		pais.setModel(countryModel);
		
		cargaDatosFormulario(1);
		
	}
	
	public Persona getPersona() {
		return persona;
	}

	/**
	 * Carga los datos en el formulario de una persona dada.
	 * @param i
	 */
	private void cargaDatosFormulario(int idPersona) {

		persona = getDatosPersona();
		
//		numero.setValue(Integer.toString(persona.getId()));
//		nombre.setValue(persona.getNombre());
//		email.setValue(persona.getEmail());
//		fecNacimiento.setValue(persona.getFechaNacimiento());
//		biografia.setValue(persona.getBiografia());
//
//		((ListModelList)pais.getModel()).addToSelection(persona.getPais());
		
	}

	/**
	 * Obtiene los datos de la persona.
	 * @return
	 */
	private Persona getDatosPersona() {
		Persona persona = new Persona(); 
		persona.setId(1);
		persona.setNombre("Juan Perez");
		persona.setEmail("jperez@demo.com");
		persona.setPais("Ecuador");
		persona.setFechaNacimiento(new Date());
		persona.setBiografia(null);
		return persona;
	}

	/**
	 * Escucha el evento "onClick" del objeto "grabar"
	 */
	@Listen("onClick=#grabar")
	public void grabar(){
		Clients.showNotification("Proceso Ejecutado con exito.");
	}

	/**
	 * Escucha el evento "onClick" del objeto "recargar"
	 */
	@Listen("onClick=#recargar")
	public void recargar(){
		Clients.showNotification("Actualizacion exitosa.");
	}

	
	/**
	 * Retorna una lista de paises, se deberia recuperar de una tabla.
	 * @return
	 */
	private List<String> gatPaises() {

		List<String> paises = new ArrayList<String>();
		
		paises.add("Ecuador");
		paises.add("Colombia");
		paises.add("Perú");
		paises.add("Venezuela");
		paises.add("Chile");
		
		return paises;
	}

	
}
