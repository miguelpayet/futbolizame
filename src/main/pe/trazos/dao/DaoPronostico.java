package pe.trazos.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import pe.trazos.dominio.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaoPronostico extends Dao<Pronostico> {

	public DaoPronostico() {
		super(Pronostico.class);
	}

	public Pronostico get(PronosticoPK unaPk) {
		return getSession().get(Pronostico.class, unaPk);
	}

	@Deprecated
	public Pronostico get(Visitante unVisitante, Participacion unaParticipacion) {
		Criteria criteria = getSession().createCriteria(getClaseEntidad());
		criteria.add(Restrictions.and(
				Restrictions.eq("visitante", unVisitante),
				Restrictions.eq("participacion", unaParticipacion)
		));
		Pronostico pro = (Pronostico) criteria.uniqueResult();
		if (pro == null) {
			pro = new Pronostico();
			pro.setVisitante(unVisitante);
			pro.setParticipacion(unaParticipacion);
		}
		return pro;
	}

	public List<Pronostico> get(Visitante unVisitante) {
		Criteria criteria = getSession().createCriteria(getClaseEntidad());
		//criteria.add(Restrictions.eq("visitante", unVisitante.getUserId()));
		List<Pronostico> pronosticos = criteria.list();
		return pronosticos;
	}

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
}
