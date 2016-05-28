package pe.trazos.homepage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import pe.trazos.auth.SesionFacebook;
import pe.trazos.dao.DaoPronostico;
import pe.trazos.dominio.Partido;
import pe.trazos.dominio.Posicionable;
import pe.trazos.dominio.PronosticoPK;

public class PartidoComponentPanel extends FormComponentPanel {

	private Partido partido;

	public PartidoComponentPanel(String id, Partido unPartido) {
		super(id);
		partido = unPartido;
		agregarTitulo();
		agregarLocal();
		agregarVisita();
	}

	private void agregarLocal() {
		add(new ContextImage("imagen-local", partido.getLocal().getEquipo().getLogo()));
		add(new Label("nombre-local", new Model<>(partido.getLocal().getEquipo().getNombre())));
		Posicionable p;
		if (SesionFacebook.get().isSignedIn()) {
			DaoPronostico dp = new DaoPronostico();
			PronosticoPK pk = new PronosticoPK();
			pk.setVisitante(SesionFacebook.get().getVisitante());
			pk.setParticipacion(partido.getLocal());
			p = dp.get(pk);
		} else {
			p = partido.getLocal();
		}
		add(new TextField<String>("goles-local", new PropertyModel<>(p, "goles")));
	}

	private void agregarTitulo() {
		add(new Label("fecha", new Model<>(partido.getFecha().getFecha())));
	}

	private void agregarVisita() {
		add(new ContextImage("imagen-visita", partido.getVisita().getEquipo().getLogo()));
		add(new Label("nombre-visita", new Model<>(partido.getVisita().getEquipo().getNombre())));
		Posicionable p;
		if (SesionFacebook.get().isSignedIn()) {
			DaoPronostico dp = new DaoPronostico();
			PronosticoPK pk = new PronosticoPK();
			pk.setVisitante(SesionFacebook.get().getVisitante());
			pk.setParticipacion(partido.getVisita());
			p = dp.get(pk);
		} else {
			p = partido.getVisita();
		}
		add(new TextField<String>("goles-visita", new PropertyModel<>(p, "goles")));	}

	/*
		WebMarkupContainer wmcGoles = new WebMarkupContainer(partidoRepetidor.newChildId());
		partidoRepetidor.add(wmcGoles);
		Fragment fragGoles = new Fragment("listItem", "goles", this);
		wmcGoles.add(fragGoles);
		fragGoles.add(new ContextImage("pelota", "/images/goles.png"));
		fragGoles.add(new TextField<>("golesLocal", new PropertyModel<>(unPartido.getLocal(), "goles")));
		fragGoles.add(new TextField<>("golesVisita", new PropertyModel<>(unPartido.getVisita(), "goles")));
		agregarUnaEstadistica("Local", unPartido, unPartido.getLocal(), fragGoles);
		agregarUnaEstadistica("Visitante", unPartido, unPartido.getVisita(), fragGoles);
	*/

	/*
			// fragmento con datos del partido
		Fragment fragPartido = new Fragment("listItem", "partido", this);
		partidoExterior.add(fragPartido);
		fragPartido.add(new Label("fecha", dateFormat.format(unPartido.getFechaPartido())));
		WebMarkupContainer fragEquipos = new WebMarkupContainer("filaPartido");
		fragPartido.add(fragEquipos);
		agregarUnEquipo("Local", unPartido, unPartido.getLocal(), fragEquipos);
		agregarUnEquipo("Visitante", unPartido, unPartido.getVisita(), fragEquipos);
	 */
}
