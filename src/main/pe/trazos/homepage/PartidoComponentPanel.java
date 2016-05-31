package pe.trazos.homepage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import pe.trazos.auth.SesionWeb;
import pe.trazos.dao.DaoPronostico;
import pe.trazos.dominio.*;

public class PartidoComponentPanel extends FormComponentPanel {

	private PanelFecha panelFecha;
	private Partido partido;

	public PartidoComponentPanel(String id, IModel<Partido> unPartido, PanelFecha unPanel) {
		super(id, unPartido);
		partido = unPartido.getObject();
		panelFecha = unPanel;
		agregarTitulo();
		agregarLocal();
		agregarVisita();
	}

	private void agregarLocal() {
		agregarPosicionable(partido.getLocal(), "local");
	}

	private void agregarPosicionable(Participacion unaParticipacion, String unSufijo) {
		add(new ContextImage("imagen-" + unSufijo, unaParticipacion.getEquipo().getLogo()));
		add(new Label("nombre-" + unSufijo, new Model<>(unaParticipacion.getEquipo().getNombre())));
		Posicionable p;
		DaoPronostico dp = new DaoPronostico();
		PronosticoPK pk = new PronosticoPK();
		pk.setVisitante(SesionWeb.get().getVisitante());
		pk.setParticipacion(unaParticipacion);
		p = dp.get(pk);
		if (p == null) {
			p = new Pronostico().setParticipacion(unaParticipacion).setVisitante(SesionWeb.get().getVisitante());
		}
		panelFecha.addParticipante(p);
		add(new TextField<String>("goles-" + unSufijo, new PropertyModel<>(p, "goles")));
	}

	private void agregarTitulo() {
		add(new Label("fecha", new Model<>(partido.getFecha().getFecha())));
	}

	private void agregarVisita() {
		agregarPosicionable(partido.getVisita(), "visita");
	}

}
