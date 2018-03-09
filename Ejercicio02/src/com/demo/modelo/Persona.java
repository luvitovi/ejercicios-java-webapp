package com.demo.modelo;

import java.util.Date;

/**
 * Clase que representa una persona.
 * @author Luis
 *
 */
public class Persona {

	// Atributos
	private int id;
	private String nombre; 
	private String email; 
	private Date fechaNacimiento; 
	private String pais; 
	private String biografia;
	
	// Constructor
	public Persona() {
		super();
	}
	
	// Getters y Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getBiografia() {
		return biografia;
	}
	public void setBiografia(String biografia) {
		this.biografia = biografia;
	} 
	
}
