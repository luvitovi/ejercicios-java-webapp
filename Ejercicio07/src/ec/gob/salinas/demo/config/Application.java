package ec.gob.salinas.demo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de la aplicación.
 * 
 * @author Luis
 *
 */
@Configuration
@ComponentScan(basePackages = "ec.gob.salinas.demo.service")
public class Application {

}
