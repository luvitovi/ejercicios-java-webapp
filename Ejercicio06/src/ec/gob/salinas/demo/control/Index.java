package ec.gob.salinas.demo.control;

import java.io.IOException;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;

import ec.gob.salinas.demo.util.FileUtil;
import lombok.Getter;
import lombok.Setter;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class Index {

	@Getter @Setter private AImage imagen;
	private String pathArchivo;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);
	}
	
	@Command
	@NotifyChange({"imagen"})
	public void subir(@ContextParam(ContextType.BIND_CONTEXT) BindContext contexto) throws IOException {
		
		// Extrae el evento de carga
		UploadEvent eventoCarga = (UploadEvent) contexto.getTriggerEvent();
		
		// Pasa el archivo a cargar al metodo "cargaArchivo" de la clase "FileUtil"
		pathArchivo = FileUtil.cargaArchivo(eventoCarga.getMedia());
		
		// Actualiza el objeto de pantalla
		imagen = new AImage(pathArchivo);

	}
	
	@Command
	public void descargar() {
		if (pathArchivo != null) {
			FileUtil.descargaArchivo(pathArchivo);
		}

	}
	

}
