package pe.trazos.dominio;

import java.io.Serializable;
import java.util.Comparator;

public class ComparadorFecha implements Comparator<Fecha>, Serializable {

	@Override
	public int compare(Fecha o1, Fecha o2) {
		return o1.getFecha().compareTo(o2.getFecha());
	}

}
