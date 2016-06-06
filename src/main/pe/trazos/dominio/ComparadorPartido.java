package pe.trazos.dominio;

import java.io.Serializable;
import java.util.Comparator;

public class ComparadorPartido implements Comparator<Partido>, Serializable {

	@Override
	public int compare(Partido o1, Partido o2) {
		// primero por fecha y hora
		int resultado = o1.getFechaPartido().compareTo(o2.getFechaPartido());
		// si fecha y hora son iguales, por nombre del local
		if (resultado == 0) {
			resultado = o1.toString().compareTo(o2.toString());
		}
		return resultado;
	}

}
