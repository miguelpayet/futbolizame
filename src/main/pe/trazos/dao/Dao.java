package pe.trazos.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import pe.trazos.dominio.IObjetoDominio;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public abstract class Dao<T extends IObjetoDominio> {

	private static HashMap<Class, Class<? extends Dao>> listaDaos;

	private Class claseEntidad;
	protected Session session;

	Dao(Class unaClase) {
		this.session = getSession();
		this.claseEntidad = unaClase;
	}

	@SuppressWarnings("unchecked")
	public T get(Integer id) {
		return (T) getSession().get(claseEntidad, id);
	}

	@SuppressWarnings("unchecked")
	public T get(String unId) {
		return (T) getSession().get(claseEntidad, unId);
	}

	public Class getClaseEntidad() {
		return claseEntidad;
	}

	@SuppressWarnings("unchecked")
	T getPorCampo(String campo, String nombre) {
		Criteria criteria = session.createCriteria(claseEntidad);
		criteria.add(Restrictions.eq(campo, nombre));
		return (T) criteria.uniqueResult(); // TODO: excepción si hay más de 1
	}

	public Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

	public void grabar(T objeto) {
		getSession().saveOrUpdate(objeto);
	}

	@SuppressWarnings("unchecked")
	public List<T> listar(String campoOrden) {
		Criteria criteria = session.createCriteria(claseEntidad);
		if (campoOrden != null) {
			if (!campoOrden.equals("")) {
				criteria.addOrder(Order.asc(campoOrden));
			}
		}
		return Collections.checkedList(criteria.list(), claseEntidad);
	}

	public List<T> listar() {
		return listar("");
	}

	@SuppressWarnings("unchecked")
	public List<T> listarPorCampo(String unCampo, String unValor) {
		Criteria criteria = session.createCriteria(claseEntidad);
		criteria.add(Restrictions.eq(unCampo, unValor));
		return Collections.checkedList(criteria.list(), claseEntidad);
	}

	@SuppressWarnings("unchecked")
	public List<T> listarPorCodigo(String unNombre) {
		Criteria criteria = session.createCriteria(claseEntidad);
		criteria.add(Restrictions.eq("codigo", unNombre));
		return Collections.checkedList(criteria.list(), claseEntidad);
	}

}
