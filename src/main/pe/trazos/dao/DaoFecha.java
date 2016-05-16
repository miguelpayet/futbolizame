package pe.trazos.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import pe.trazos.dominio.Fecha;

import java.util.Date;
import java.util.List;

public class DaoFecha extends Dao<Fecha> {

	public DaoFecha() {
		super(Fecha.class);
	}

	public List<Fecha> getFuturo(Date unaFecha) {
		Criteria criteria = getSession().createCriteria(getClaseEntidad());
		criteria.add(Restrictions.lt("fecha", unaFecha));
		return criteria.list();
	}

}
