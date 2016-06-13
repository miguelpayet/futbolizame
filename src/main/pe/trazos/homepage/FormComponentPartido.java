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
import pe.trazos.dominio.Participacion;
import pe.trazos.dominio.Partido;
import pe.trazos.dominio.Posicionable;
import pe.trazos.dominio.Pronostico;

public class FormComponentPartido extends FormComponentPanel {

	private PanelFecha panelFecha;
	private Partido partido;

	public FormComponentPartido(String id, IModel<Partido> unPartido, PanelFecha unPanel) {
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
		DaoPronostico dp = new DaoPronostico();
		Posicionable p = dp.get(SesionWeb.get().getVisitante(), unaParticipacion);
		if (p == null) {
			p = new Pronostico().setParticipacion(unaParticipacion).setVisitante(SesionWeb.get().getVisitante());
		}
		panelFecha.addParticipante(p);
		add(new TextField<String>("goles-" + unSufijo, new PropertyModel<>(p, "goles")));
	}

	private void agregarTitulo() {
		//add(new Label("fecha", new Model<>(partido.getFechaPartido())));
		add(new Label("ubicacion", "por definir"));
		add(new Label("hora", "por definir"));
	}

	private void agregarVisita() {
		agregarPosicionable(partido.getVisita(), "visita");
	}

}
