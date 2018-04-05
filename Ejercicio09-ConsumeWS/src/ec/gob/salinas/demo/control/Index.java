package ec.gob.salinas.demo.control;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.util.Clients;

import ec.gob.salinas.demo.modelo.pojo.Usuario;
import lombok.Getter;
import lombok.Setter;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class Index {
	
	private static final String URL_LOGIN = "/login";
	private static final String URL_USUARIOS = "/ws/usuarios/";

	@Getter @Setter private String url;
	@Getter @Setter private String token;

	@Getter @Setter private Integer idUsuario;
	@Getter @Setter private Usuario usuarioAutenticacion;
	@Getter @Setter private Usuario usuario;
	@Getter @Setter private Usuario usuarioCrear;
	@Getter @Setter private List<Usuario> usuarios;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		usuarioAutenticacion = new Usuario();
		usuarioCrear = new Usuario();
	}

	@Command
	@NotifyChange({"token"})
	public void autenticar() {

		if (usuarioAutenticacion.getNombreUsuario() == null || 
			usuarioAutenticacion.getPassword() == null) {
			Clients.showNotification("Ingrese las credenciales.");
			return;
		}

		// Objeto para consumir servicios web.
		RestTemplate restTemplate = new RestTemplate();

		// Define que el encabezamiento es JSON
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		// Creamos el requerimiento
		HttpEntity<Usuario> requerimiento = new HttpEntity<Usuario>(usuarioAutenticacion, headers);

		// Enviamos la peticion.
		ResponseEntity<String> respuesta = restTemplate.exchange(url + URL_LOGIN, 
																HttpMethod.POST, 
																requerimiento, 
																String.class);
		if (respuesta == null) {
			Clients.showNotification("Error en la autenticacion.");
			return;
		}

		token = respuesta.getHeaders().getFirst("authorization");
		if (token == null) {
			Clients.showNotification("Error no se encontro la autorizacion.");
			return;
		}

		Clients.showNotification("Proceso ejecutado con éxito");

	}

	/**
	 * Ejemplo de consumo de web services por el metodo Get.
	 */
	@Command
	@NotifyChange({"usuario"})
	public void buscarUsuario() {

		if (idUsuario == 0) {
			Clients.showNotification("Ingrese el ID del Usuario.");
			return;
		}

		// Objeto para consumir servicios web.
		RestTemplate restTemplate = new RestTemplate();

		// Define que el encabezamiento es JSON
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);

		// Creamos el requerimiento
		HttpEntity<?> requerimiento = new HttpEntity<>(headers);

		// Enviamos la peticion.
		ResponseEntity<Usuario> respuesta = restTemplate.exchange(url + URL_USUARIOS + idUsuario, 
																 HttpMethod.GET, 
																 requerimiento, 
																 Usuario.class);
		if (respuesta == null) {
			Clients.showNotification("Error en la llamada al servicio web.");
			return;
		}

		usuario = respuesta.getBody();
		if (usuario == null) {
			Clients.showNotification("Error no se encontro el usuario.");
			return;
		}

	}

	/**
	 * Ejemplo de consumo de web services por el metodo Get.
	 */
	@Command
	@NotifyChange({"usuarios"})
	public void buscarUsuarios() {
		
		// Objeto para consumir servicios web.
		RestTemplate restTemplate = new RestTemplate();

		// Define que el encabezamiento es JSON
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);

		// Creamos el requerimiento
		HttpEntity<?> requerimiento = new HttpEntity<>(headers);

		// Enviamos la peticion.
		ResponseEntity<Usuario[]> respuesta = restTemplate.exchange(url + URL_USUARIOS, 
																 HttpMethod.GET, 
																 requerimiento, 
																 Usuario[].class);
		if (respuesta == null) {
			Clients.showNotification("Error en la llamada al servicio web.", "error",null, null, 0);
			return;
		}

		if (respuesta.getBody() == null) {
			Clients.showNotification("Error no se encontro datos.", "error",null, null, 0);
			return;
		}
		
		usuarios = Arrays.asList(respuesta.getBody());

		Clients.showNotification("Proceso ejecutado con éxito");

	}

	/**
	 * Ejemplo de consumo de web services por el metodo Post.
	 */
	@Command
	public void crearUsuario() {

		if (usuarioCrear.getNombreUsuario() == null || 
			usuarioCrear.getPassword() == null) {
			Clients.showNotification("Ingrese los datos del Usuario.", "error",null, null, 0);
			return;
		}

		// Objeto para consumir servicios web.
		RestTemplate restTemplate = new RestTemplate();

		// Define que el encabezamiento es JSON
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);

		// Creamos el requerimiento
		HttpEntity<Usuario> requerimiento = new HttpEntity<Usuario>(usuarioCrear, headers);

		// Enviamos la peticion.
		ResponseEntity<String> respuesta = restTemplate.exchange(url + URL_USUARIOS, 
																 HttpMethod.POST, 
																 requerimiento, 
																 String.class);
		if (respuesta == null) {
			Clients.showNotification("Error en la llamada al servicio web.", "error",null, null, 0);
			return;
		}

		Clients.showNotification("Proceso ejecutado con éxito");

	}

	/**
	 * Ejemplo de consumo de web services por el metodo Put.
	 */
	@Command
	public void actualizarUsuario() {

		if (usuario == null) {
			Clients.showNotification("Seleccione el Usuario.", "error",null, null, 0);
			return;
		}

		// Objeto para consumir servicios web.
		RestTemplate restTemplate = new RestTemplate();

		// Define que el encabezamiento es JSON
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);

		// Creamos el requerimiento
		HttpEntity<Usuario> requerimiento = new HttpEntity<Usuario>(usuario, headers);

		// Enviamos la peticion.
		ResponseEntity<String> respuesta = restTemplate.exchange(url + URL_USUARIOS, 
																 HttpMethod.PUT, 
																 requerimiento, 
																 String.class);
		if (respuesta == null) {
			Clients.showNotification("Error en la llamada al servicio web.", "error",null, null, 0);
			return;
		}

		Clients.showNotification("Proceso ejecutado con éxito");

	}

	/**
	 * Ejemplo de consumo de web services por el metodo Delete.
	 */
	@Command
	@NotifyChange({"usuario"})
	public void borrarUsuario() {

		if (idUsuario == 0) {
			Clients.showNotification("Ingrese el ID del Usuario.", "error",null, null, 0);
			return;
		}

		// Objeto para consumir servicios web.
		RestTemplate restTemplate = new RestTemplate();

		// Define que el encabezamiento es JSON
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);

		// Creamos el requerimiento
		HttpEntity<?> requerimiento = new HttpEntity<>(headers);

		// Enviamos la peticion.
		ResponseEntity<String> respuesta = restTemplate.exchange(url + URL_USUARIOS + idUsuario, 
																 HttpMethod.DELETE, 
																 requerimiento, 
																 String.class);
		if (respuesta == null) {
			Clients.showNotification("Error en la llamada al servicio web.", "error",null, null, 0);
			return;
		}
		
		Clients.showNotification("Proceso ejecutado con éxito");
		
	}

}
