package ec.gob.salinas.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class SaldoMensual {

	@Getter @Setter private String mes;
	@Getter @Setter private double saldo;
	
}
