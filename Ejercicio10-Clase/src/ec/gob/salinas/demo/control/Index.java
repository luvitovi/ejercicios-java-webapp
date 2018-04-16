package ec.gob.salinas.demo.control;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class Index {

	@Wire
	private Gmaps gmap;
	
	private List<SitioTuristico> sitios;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);
		
		sitios = getSitiosTuristicos();
		
		inicializaMapa();
		
	}

	/**
	 * Consulta a la base de datos.
	 * @return
	 */
	private List<SitioTuristico> getSitiosTuristicos() {
		List<SitioTuristico> retorno = new ArrayList<SitioTuristico>();
		
		retorno.add(new SitioTuristico("Iglesia Central", -2.220, -80.955));
		retorno.add(new SitioTuristico("Malecon", -2.214, -80.920));
		retorno.add(new SitioTuristico("Santa Rosa", -2.190, -80.955));
		retorno.add(new SitioTuristico("Anconcito", -2.214, -80.458));
		retorno.add(new SitioTuristico("Punta Carnero", -2.022, -80.682));
		
		return retorno;
	}

	/**
	 * Inicializar el mapa.
	 */
	private void inicializaMapa() {

		// Definimos el mapa
		gmap.setVersion("3.26");
		gmap.setProtocol("https");
		gmap.setLat(-2.214);
		gmap.setLng(-80.955);
		gmap.setZoom(17);
		
		for (SitioTuristico sitioTuristico : sitios) {
			Gmarker gm = new Gmarker();
			gm.setLat(sitioTuristico.getLat());
			gm.setLng(sitioTuristico.getLng());
			gm.setContent("<h2>" + sitioTuristico.getNombre() + "</h2>");
			gm.setOpen(true);
			gm.setDraggingEnabled(true);
			
			gmap.appendChild(gm);
		}

	}
	
	
	
	@AllArgsConstructor
	public class SitioTuristico {
		@Setter @Getter private String nombre;
		@Setter @Getter private double lat;
		@Setter @Getter private double lng;
	}
	
}
