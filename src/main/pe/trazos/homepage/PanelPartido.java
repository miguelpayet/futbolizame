package pe.trazos.homepage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import pe.trazos.dominio.Participacion;
import pe.trazos.dominio.Partido;

public class PanelPartido extends Panel {

	private Partido partido;

	public PanelPartido(String id, IModel<Partido> unModelo) {
		super(id, unModelo);
		partido = unModelo.getObject();
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
		add(new Label("goles-" + unSufijo, new Model<>(unaParticipacion.getGoles())));
	}

	private void agregarTitulo() {
		add(new Label("fecha", new Model<>(partido.getFechaPartido())));
	}

	private void agregarVisita() {
		agregarPosicionable(partido.getVisita(), "visita");
	}

}
