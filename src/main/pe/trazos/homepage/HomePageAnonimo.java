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
import pe.trazos.dominio.Participacion;
import pe.trazos.dominio.Partido;
import pe.trazos.dominio.Ratio;

public class HomePageAnonimo extends HomePage {

	private static final Logger log = LoggerFactory.getLogger(HomePageAnonimo.class);

	public HomePageAnonimo() {
		this(new PageParameters());
	}

	public HomePageAnonimo(PageParameters parameters) {
		super(parameters);
		agregarTabla();
		agregarPartidos();
	}

	protected void agregarUnPartido(Partido unPartido) {
		WebMarkupContainer wmcPartido = new WebMarkupContainer(repetidor.newChildId());
		repetidor.add(wmcPartido);
		Fragment fragPartido = new Fragment("listItem", "partido", this);
		wmcPartido.add(fragPartido);
		fragPartido.add(new Label("fecha", dateFormat.format(unPartido.getFechaPartido())));
		WebMarkupContainer fragEquipos = new WebMarkupContainer("filaPartido");
		fragPartido.add(fragEquipos);
		agregarUnEquipo("Local", unPartido, unPartido.getLocal(), fragEquipos);
		agregarUnEquipo("Visitante", unPartido, unPartido.getVisita(), fragEquipos);
		WebMarkupContainer wmcGoles = new WebMarkupContainer(repetidor.newChildId());
		repetidor.add(wmcGoles);
		Fragment fragGoles = new Fragment("listItem", "goles", this);
		wmcGoles.add(fragGoles);
		fragGoles.add(new ContextImage("pelota", "/images/goles.png"));
		fragGoles.add(new TextField<>("golesLocal", new PropertyModel<>(unPartido.getLocal(), "goles")));
		fragGoles.add(new TextField<>("golesVisita", new PropertyModel<>(unPartido.getVisita(), "goles")));
		agregarUnaEstadistica("Local", unPartido, unPartido.getLocal(), fragGoles);
		agregarUnaEstadistica("Visitante", unPartido, unPartido.getVisita(), fragGoles);
	}

	protected void agregarUnaEstadistica(String unId, Partido unPartido, Participacion unaParticipacion, Fragment unContainer) {
		Ratio ratio;
		if (unaParticipacion.getEquipo().equals(unPartido.getLocal().getEquipo())) {
			ratio = unaParticipacion.getEquipo().compararCon(unPartido.getVisita().getEquipo());
		} else {
			ratio = unaParticipacion.getEquipo().compararCon(unPartido.getLocal().getEquipo());
		}
		unContainer.add(new Label("stats" + unId, ratio.toString()));
		unContainer.add(new Label("cantStats" + unId, ratio.getTotal()));
	}

	protected void calcularPosiciones() {
		competencia.crearPosiciones();
	}

	protected void formSubmit() {
		log.info("anonimo.formsubmit");
		competencia.crearPosiciones();
	}

}
