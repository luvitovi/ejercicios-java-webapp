package ec.gob.salinas.demo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Configuración de la aplicación.
 * 
 * @author Luis
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"ec.gob.salinas.demo.service", "ec.gob.salinas.demo.ws"})
public class Application {

}
