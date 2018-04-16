package ec.gob.salinas.demo.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import ec.gob.salinas.demo.modelo.pojo.Usuario;

public class TokenUtil {
	
	private static final String URL_WS = "http://localhost:8080/Ejercicio13-Server";
	private static final String URL_LOGIN = "/login";

	public static String autenticar(String nombreUsuario, String password) {
		
		Usuario usuario = new Usuario();
		
		usuario.setNombreUsuario(nombreUsuario);
		usuario.setPassword(password);
		
		// Objeto para consumir servicios web.
		RestTemplate restTemplate = new RestTemplate();

		// Define que el encabezamiento es JSON
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		// Creamos el requerimiento
		HttpEntity<Usuario> requerimiento = new HttpEntity<Usuario>(usuario, headers);

		// Enviamos la peticion.
		ResponseEntity<String> respuesta = restTemplate.exchange(URL_WS + URL_LOGIN, 
																HttpMethod.POST, 
																requerimiento, 
																String.class);
		if (respuesta == null) {
			return null;
		}

		return respuesta.getHeaders().getFirst("authorization");

	}

}
