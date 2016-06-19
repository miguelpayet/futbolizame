package pe.trazos.dao;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.trazos.dominio.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaoPronostico extends Dao<Pronostico> {

	private final static Logger log = LoggerFactory.getLogger(DaoPronostico.class);

	public DaoPronostico() {
		super(Pronostico.class);
	}

	public Pronostico get(PronosticoPK unaPk) {
		return getSession().get(Pronostico.class, unaPk);
	}

	public Pronostico get(Visitante unVisitante, Participacion unaParticipacion) {
		PronosticoPK pk = new PronosticoPK();
		pk.setVisitante(unVisitante);
		pk.setParticipacion(unaParticipacion);
		return get(pk);
	}

	public List<Pronostico> get(Visitante unVisitante) {
		Criteria criteria = getSession().createCriteria(getClaseEntidad());
		criteria.add(Restrictions.eq("visitante", unVisitante));
		List<Pronostico> pronosticos = criteria.list();
		return pronosticos;
	}

	/**
	 * obtener una lista de pronósticos para una fecha
	 *
	 * @param unVisitante -> visitante cuyos pronósticos se desea obtener
	 * @param unaFecha -> fecha cuyos pronosticos se obtiene
	 * @return mapa indexado por participación con los pronósticos obtenidos
	 */
	public Map<Participacion, Pronostico> obtenerPronosticosFecha(Visitante unVisitante, Fecha unaFecha) {
		Map<Participacion, Pronostico> pronosticos = new HashMap<>();
		// obtener lista de partidos de la fecha
		for (Partido partido : unaFecha.getPartidos()) {
			for (Participacion partic : partido.getParticipantes().values()) {
				// obtener el pronostico de cada partido según este visitante
				Pronostico pronostico = get(unVisitante, partic);
				// añadirlo a la lista
				pronosticos.put(partic, pronostico);
			}
		}
		// devolver la lista
		return pronosticos;
	}

	/**
	 * obtener una lista de pronósticos válidos (goles no nulos) para una fecha
	 *
	 * @param unaFecha -> fecha cuyos pronosticos se obtiene
	 * @return mapa indexado por participación con los pronósticos obtenidos
	 */
	public Map<Participacion, Pronostico> obtenerValidos(Fecha unaFecha) {
		//Criteria criteria = getSession().createCriteria(getClaseEntidad());
		StringBuilder sb = new StringBuilder();
		sb.append("from Pronostico p ").append("where p.visitante = '").append(unaFecha.getCompetencia().getVisitante().getId()).append("'");
		sb.append("and p.goles is not null ");
		sb.append("and p.participacion in (");
		ArrayList<Participacion> participantes = new ArrayList<>();
		for (Partido partido : unaFecha.getPartidos()) {
			participantes.addAll(partido.getParticipantes().values());
		}
		String[] lista = new String[participantes.size()];
		int i = 0;
		for (Participacion part : participantes) {
			lista[i++] = "'" + part.getId() + "'";
		}
		sb.append(String.join(",", lista));
		sb.append(")");
		log.debug(sb.toString());
		Query query = session.createQuery(sb.toString());
		List<Pronostico> list = query.list();
		Map<Participacion, Pronostico> pronosticos = new HashMap<>();
		for (Pronostico pron : list) {
			pronosticos.put(pron.getParticipacion(), pron);
		}
		return pronosticos;
	}

}