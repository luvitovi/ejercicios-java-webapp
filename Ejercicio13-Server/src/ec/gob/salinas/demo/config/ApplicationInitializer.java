package ec.gob.salinas.demo.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class ApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	
	@Override
	protected Class < ? > [] getRootConfigClasses() {
		return new Class[] {Application.class, Persistencia.class, Security.class};
	}

	@Override
	protected Class < ? > [] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] {"/ws/*"};
	}

}