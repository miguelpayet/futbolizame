package pe.trazos.homepage;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.trazos.dao.DaoPronostico;
import pe.trazos.dao.DaoVisitante;
import pe.trazos.dominio.Participacion;
import pe.trazos.dominio.Partido;
import pe.trazos.dominio.Pronostico;
import pe.trazos.dominio.Visitante;

import java.util.Map;

public class HomePageAutentificado extends HomePage {

	private static final Logger log = LoggerFactory.getLogger(HomePageAutentificado.class);

	Map<Participacion, Pronostico> pronosticos;

	public HomePageAutentificado() {
		this(new PageParameters());
		agregarTabla();
		agregarPartidos();
	}

	public HomePageAutentificado(PageParameters parameters) {
		super(parameters);
		if (!getSesion().isSignedIn()) { // esto está por si acaso no lo redireccione más abajo
			doLogout();
		}
	}

	protected void agregarDatosPartido(Partido unPartido) {
		// div para todo el partido
		WebMarkupContainer wmcPartido = new WebMarkupContainer(repetidor.newChildId());
		repetidor.add(wmcPartido);
		// div para el fragmento del partido
		Fragment fragPartido = new Fragment("listItem", "partido", this);
		fragPartido.add(new Label("fecha", dateFormat.format(unPartido.getFechaPartido())));
		wmcPartido.add(fragPartido);
		WebMarkupContainer fragEquipos = new WebMarkupContainer("filaPartido");
		fragPartido.add(fragEquipos);
		agregarUnEquipo("Local", unPartido, unPartido.getLocal(), fragEquipos);
		agregarUnEquipo("Visitante", unPartido, unPartido.getVisita(), fragEquipos);
	}

	private void agregarGoles(Partido unPartido) {
		WebMarkupContainer wmcGoles = new WebMarkupContainer(repetidor.newChildId());
		repetidor.add(wmcGoles);
		Fragment fragGoles = new Fragment("listItem", "goles", this);
		wmcGoles.add(fragGoles);
		fragGoles.add(new ContextImage("pelota", "/images/goles.png"));
		// conseguir el pronostico referido al partido y al equipo local
		Pronostico pLocal = getPronostico(unPartido.getLocal());
		// conectar eso al editor
		fragGoles.add(new TextField<>("golesLocal", new PropertyModel<>(pLocal, "goles")));
		agregarUnaEstadistica("Local", unPartido, unPartido.getLocal(), fragGoles);
		// conseguir el pronostico referido al partido y al equipo visitante
		Pronostico pVisita = getPronostico(unPartido.getVisita());
		fragGoles.add(new TextField<>("golesVisita", new PropertyModel<>(pVisita, "goles")));
		agregarUnaEstadistica("Visitante", unPartido, unPartido.getVisita(), fragGoles);
	}

	protected void agregarUnPartido(Partido unPartido) {
		agregarDatosPartido(unPartido);
		agregarGoles(unPartido);
	}

	@Override
	protected void agregarUnaEstadistica(String unId, Partido unPartido, Participacion unaParticipacion, Fragment unContainer) {
		unContainer.add(new Label("stats" + unId, ""));
		unContainer.add(new Label("cantStats" + unId, ""));
	}

	@Override
	protected void calcularPosiciones() throws RuntimeException {
		if (getSesion().isSignedIn() && getSesion().getUserId() != null) {
			// obtener la lista de pronósticos del usuario para la fecha
			DaoVisitante dv = new DaoVisitante();
			Visitante v = dv.get(getSesion().getUserId());
			if (v == null) {
				throw new RuntimeException("no obtuvo visitante");
			}
			DaoPronostico dp = new DaoPronostico();
			pronosticos = dp.obtenerPronosticosFecha(v, fecha);
			// validar la lista
			if (pronosticos == null || pronosticos.size() == 0) {
				throw new RuntimeException("no obtuvo pronósticos");
			}
			// calcular posiciones de la tabla con estos pronosticos
			competencia.crearPosiciones(pronosticos);
		} else {
			// no debería estar en esta página
			doLogout();
		}
	}

	protected void formSubmit() {
		log.info("formSubmit");
		// grabar los pronosticos (de la fecha)
		DaoPronostico dp = new DaoPronostico();
		pronosticos.values().forEach(dp::grabar);
		// actualizar la tabla
		competencia.crearPosiciones(pronosticos);
	}

	private Pronostico getPronostico(Participacion unaParticipacion) {
		return pronosticos.get(unaParticipacion);
	}

}
