package pe.trazos.dao;

import org.hibernate.SQLQuery;
import pe.trazos.dominio.Equipo;
import pe.trazos.dominio.Partido;

import java.util.List;

public class DaoEquipo extends Dao<Equipo> {

	public DaoEquipo() {
		super(Equipo.class);
	}

	public List<Partido> getPartidosComunes(Equipo unEquipo, Equipo otroEquipo) {
		String sql = "select * from partido where idpartido in (select idpartido from participacion p where idequipo='%s' or idequipo='%s' group by idpartido having count(1) = 2)";
		SQLQuery sqlQuery = session.createSQLQuery(String.format(sql, unEquipo.getId(), otroEquipo.getId())).addEntity(Partido.class);
		return (List<Partido>) sqlQuery.list();
	}

}
