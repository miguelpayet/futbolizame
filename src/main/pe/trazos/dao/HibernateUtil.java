package pe.trazos.dao;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import pe.trazos.dominio.*;

public class HibernateUtil {

	protected static Configuration configuration;
	private static SessionFactory sessionFactory = null;

	static {
		configuration = new Configuration();
		registrarClases();
		configuration.configure();
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	private static void registrarClases() {
		configuration.addAnnotatedClass(Competencia.class);
		configuration.addAnnotatedClass(Equipo.class);
		configuration.addAnnotatedClass(Fecha.class);
		configuration.addAnnotatedClass(Participacion.class);
		configuration.addAnnotatedClass(Partido.class);
		//configuration.addAnnotatedClass(Posicion.class);
		configuration.addAnnotatedClass(Pronostico.class);
		configuration.addAnnotatedClass(Rol.class);
		configuration.addAnnotatedClass(Visitante.class);
	}

}
