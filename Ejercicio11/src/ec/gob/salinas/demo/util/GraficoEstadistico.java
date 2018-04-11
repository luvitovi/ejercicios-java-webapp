package ec.gob.salinas.demo.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.zkoss.zk.ui.util.Clients;

import lombok.Getter;
import lombok.Setter;

/**
 * Clase utilitaria para generar un grafico estadistico.
 * Se debe usar con un elemento de tipo HTML que es donde coloca el codigo generado.
 * Documentacion en: http://www.chartjs.org/docs/
 * @author Luis
 *
 */
@SuppressWarnings("rawtypes")
public class GraficoEstadistico {
	
	public static final String LINE_CHART = "line";
	public static final String BAR_CHART = "bar";
	public static final String RADAR_CHART = "radar";
	public static final String POLAR_AREA_CHART = "polarArea";
	public static final String PIE_CHART = "pie";
	public static final String DOUGHNUT_CHART = "doughnut";
	public static final String BUBBLE_CHART = "bubble";
	
	@Getter @Setter private List fuenteDatos;
	@Getter @Setter private String tituloGrafico;
	@Getter @Setter private String atributoEtiquetas;
	@Getter @Setter private String atributoValores;
	@Getter @Setter private String tipoGrafico;	
	private String chartName;
	
	/**
	 * Inicializa el objeto de graficos. 
	 * Los Tipos de Graficos deben salir de las constantes de la clase: 
	 * GraficoEstadistico.
	 * @param fuenteDatos
	 * @param tituloGrafico
	 * @param atributoEtiquetas
	 * @param atributoValores
	 * @param tipoGrafico
	 */
	public GraficoEstadistico(List fuenteDatos, 
			                  String tituloGrafico, 
			                  String atributoEtiquetas, 
			                  String atributoValores, 
			                  String tipoGrafico) {
		this.fuenteDatos = fuenteDatos;
		this.tituloGrafico = tituloGrafico;
		this.atributoEtiquetas = atributoEtiquetas;
		this.atributoValores = atributoValores;
		this.tipoGrafico = tipoGrafico;

		// Crea el nombre del grafico.
		chartName = UUID.randomUUID().toString();

	}
	
	/**
	 * Retorna el codigo Html para generar el grafico
	 * @return
	 */
	public String getCodigoHtml() {
		StringBuffer codigoHtml = new StringBuffer();
		
		// Espacio para graficar
		codigoHtml.append("<div height=\"100%\" width=\"100%\"><canvas id='" + chartName + "' ></canvas></div>");

		return codigoHtml.toString();

	}
	
	/**
	 * Genera el Codigo JS para actualizar el grafico.
	 * @return
	 */
	public void actualizaGrafico() {
		Clients.evalJavaScript(getCodigoJavaScript());
	}

	/**
	 * Obtiene el codigo JavaScript que se ejecutara en el cliente.
	 * @return
	 */
	private String getCodigoJavaScript() {
		StringBuffer codigoJS = new StringBuffer();
		
		// Codigo javascript para graficar
		codigoJS.append("var grafico = document.getElementById('" + chartName + "');");
		codigoJS.append("if (myChart) {myChart.destroy();}");
		codigoJS.append("var datos = {");
		codigoJS.append(getEtiquetas(fuenteDatos) + ",");
		codigoJS.append("datasets: [");
		codigoJS.append(getDataset());
		codigoJS.append("]};");

		codigoJS.append(getGrafico(tipoGrafico));

		return codigoJS.toString();
	}

	/**
	 * Retorna el codigo para generar el grafico del tipo establecido.
	 * @return
	 */
	private String getGrafico(String tipo) {
		StringBuffer retorno = new StringBuffer();

		// Opciones
		retorno.append("var opciones = {");
		retorno.append("legend: {");
		retorno.append("display: false ");
		retorno.append("},"); 
		if (tipoGrafico != null && !tipoGrafico.isEmpty()) {
			retorno.append("title: {");
			retorno.append("display: true,");
			retorno.append("text: '" + tituloGrafico + "'");
			retorno.append("},"); 
		}

		switch (tipo) {
		case PIE_CHART:
			retorno.append("animation:{");
			retorno.append("animateScale:true");
			retorno.append("}");
			break;
		default:
			retorno.append("scales: {");
			retorno.append("yAxes: [{");
			retorno.append("ticks: {");
			retorno.append("beginAtZero:true");
			retorno.append("}");
			retorno.append("}]");
			retorno.append("}");
			break;
		}

		retorno.append("};");
	
		// Grafico
		retorno.append("var myChart = new Chart(grafico, {");
		retorno.append("    type: '" + tipo + "',");
		retorno.append("    data: datos,");
		retorno.append("    options: opciones ");
		
		retorno.append("});");

		return retorno.toString();
	}

	/**
	 * Retorna el dataset con titulo, etiquetas y colores.
	 * @return
	 */
	private String getDataset() {
		StringBuffer resultado = new StringBuffer();
		
		resultado.append("{");
		resultado.append(getValores(fuenteDatos) + ",");
		resultado.append(getColores(fuenteDatos == null?0:fuenteDatos.size()));
		resultado.append("}");
		
		return resultado.toString();
	}

	/**
	 * Obtiene las etiquetas del grafico.
	 * @param fuenteDatos
	 * @return
	 */
	private String getEtiquetas(List fuenteDatos) {
		StringBuffer resultado = new StringBuffer();
		if (fuenteDatos != null) {
			for (Object item : fuenteDatos) {
				if (resultado.length() > 0) {
					resultado.append(",");
				}
				try {
					Method metodo = item.getClass().getMethod("get" + atributoEtiquetas.substring(0, 1).toUpperCase() + atributoEtiquetas.substring(1));
					String etiqueta = (String) metodo.invoke(item);
					resultado.append("'" + etiqueta + "'");
				} catch (IllegalArgumentException | IllegalAccessException | SecurityException | NoSuchMethodException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return "labels: [" + resultado.toString() + "]"; 
	}
	
	/**
	 * Obtiene la lista de valores del grafico.
	 * @param fuenteDatos
	 * @return
	 */
	private String getValores(List fuenteDatos) {
		StringBuffer resultado = new StringBuffer();
		if (fuenteDatos != null) {
			for (Object item : fuenteDatos) {
				if (resultado.length() > 0) {
					resultado.append(",");
				}
				try {
					Method metodo = item.getClass().getMethod("get" + atributoValores.substring(0, 1).toUpperCase() + atributoValores.substring(1));
					// Primero lo intenta transformar a double, si no puede lo pasa a Long
					if (metodo.getReturnType().getName().equals("double")) {
						double valor = (double) metodo.invoke(item);
						resultado.append("'" + valor + "'");
					}else if (metodo.getReturnType().getName().equals("long")) {
						long valor = (long) metodo.invoke(item);
						resultado.append("'" + valor + "'");
					}else if (metodo.getReturnType().getName().equals("int")) {
						int valor = (int) metodo.invoke(item);
						resultado.append("'" + valor + "'");
					}else{
						resultado.append("'0'");
					}
				} catch (IllegalArgumentException | IllegalAccessException | SecurityException | NoSuchMethodException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return "data: [" + resultado.toString() + "]"; 
	}

	/**
	 * Obtiene los colores del grafico generados aleatoriamente.
	 * @param cantidadItems
	 * @return
	 */
	private String getColores(int cantidadItems) {
		StringBuffer resultado = new StringBuffer();
		StringBuffer resultadoFondo = new StringBuffer();
		StringBuffer resultadoBorde = new StringBuffer();
				
		for (int i = 0; i < cantidadItems; i++) {
			if (resultadoFondo.length() > 0) {
				resultadoFondo.append(",");
				resultadoBorde.append(",");
			}
			int rojo = getNumeroAleatorio(54,255);
			int verde = getNumeroAleatorio(99,206);
			int azul = getNumeroAleatorio(64,255);
			resultadoFondo.append("'rgba(" + rojo + "," + verde + "," + azul + ", 0.2)'");
			resultadoBorde.append("'rgba(" + rojo + "," + verde + "," + azul + ", 1)'");
		}
		
		resultado.append("backgroundColor: [");
		resultado.append(resultadoFondo.toString());
		resultado.append("],borderColor: [");
		resultado.append(resultadoBorde.toString());
		resultado.append("],borderWidth: 1 ");

		return resultado.toString(); 
	}

	private static int getNumeroAleatorio(int limiteInferior, int limiteSuperior) {
		Random random = new Random();
		int retorno = 0; 
		retorno = random.nextInt(limiteSuperior - limiteInferior);
		retorno += limiteInferior; 
		return retorno;
	}

	
}
