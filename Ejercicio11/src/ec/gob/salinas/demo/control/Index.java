package ec.gob.salinas.demo.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;

import ec.gob.salinas.demo.model.SaldoMensual;
import ec.gob.salinas.demo.util.GraficoEstadistico;
import lombok.Getter;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class Index {
	
	@Getter private String htmlGrafico;	
	private GraficoEstadistico grafico;
	
	private List<SaldoMensual> saldosMensuales;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		
		saldosMensuales = getSaldos();

		grafico = new GraficoEstadistico(saldosMensuales, 
										"Saldos Mensuales", "mes", "saldo", 
										GraficoEstadistico.BAR_CHART);
		htmlGrafico = grafico.getCodigoHtml();
		grafico.actualizaGrafico();

	}
	
	/**
	 * Recupera los saldos.
	 * @return
	 */
	private List<SaldoMensual> getSaldos() {
		List<SaldoMensual> saldos = new ArrayList<SaldoMensual>();
		saldos.add(new SaldoMensual("Enero", ((double)getNumeroAleatorio(100, 1000)/10)));
		saldos.add(new SaldoMensual("Febrero", ((double)getNumeroAleatorio(100, 1000)/10)));
		saldos.add(new SaldoMensual("Abril", ((double)getNumeroAleatorio(100, 1000)/10)));
		saldos.add(new SaldoMensual("Mayo", ((double)getNumeroAleatorio(100, 1000)/10)));
		saldos.add(new SaldoMensual("Junio", ((double)getNumeroAleatorio(100, 1000)/10)));
		return saldos;
	}

	@Command
	public void actualizaGrafico() {
		// Actualiza el grafico Estadistico.
		saldosMensuales = getSaldos();
		grafico.setFuenteDatos(saldosMensuales);
		grafico.actualizaGrafico();
	}
	
	
	private static int getNumeroAleatorio(int limiteInferior, int limiteSuperior) {
		Random random = new Random();
		int retorno = 0; 
		retorno = random.nextInt(limiteSuperior - limiteInferior);
		retorno += limiteInferior; 
		return retorno;
	}
	
}
