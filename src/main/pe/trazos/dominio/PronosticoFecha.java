package pe.trazos.dominio;

import pe.trazos.dao.DaoPronostico;

import java.util.Map;
import java.util.Set;

public class PronosticoFecha {

	private Fecha fecha;
	Map<Participacion, Pronostico> pronosticos;
	private Visitante visitante;

	public PronosticoFecha(Fecha unaFecha, Visitante unVisitante) {
		fecha = unaFecha;
		visitante = unVisitante;
		leerPronosticos();
	}

	public Set<Partido> getPartidos() {
		// no estoy chequeando si tiene pronósticos para todos los partidos de la fecha (en teoría es lo que me da el dao)
		return fecha.getPartidos();
	}

	public Map<Participacion, Pronostico> getPronosticos() {
		return pronosticos;
	}

	private void leerPronosticos() {
		// consigo un pronostico por cada participante de cada partido. si no existe en la bd lo crea.
		DaoPronostico dp = new DaoPronostico();
		pronosticos = dp.obtenerPronosticosFecha(visitante, fecha);
	}

}
