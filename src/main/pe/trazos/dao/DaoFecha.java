package pe.trazos.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import pe.trazos.dominio.Competencia;
import pe.trazos.dominio.Fecha;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DaoFecha extends Dao<Fecha> {

	public DaoFecha() {
		super(Fecha.class);
	}

	public Set<Fecha> listarAnteriores(Fecha unaFecha) {
		Criteria criteria = getSession().createCriteria(getClaseEntidad());
		criteria.add(Restrictions.eq("competencia", unaFecha.getCompetencia()));
		criteria.add(Restrictions.lt("fecha", unaFecha.getFecha()));
		return new LinkedHashSet<>((List<Fecha>) criteria.list());
	}

	public Set<Fecha> listarFuturas(Competencia unaCompetencia, Date unaFecha) {
		Criteria criteria = getSession().createCriteria(getClaseEntidad());
		criteria.add(Restrictions.eq("competencia", unaCompetencia));
		criteria.add(Restrictions.lt("fecha", unaFecha));
		return new LinkedHashSet<>((List<Fecha>) criteria.list());
	}


}
