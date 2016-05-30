package pe.trazos.homepage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import pe.trazos.auth.SesionFacebook;
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
		add(new ContextImage("imagen-local", partido.getLocal().getEquipo().getLogo()));
		add(new Label("nombre-local", new Model<>(partido.getLocal().getEquipo().getNombre())));
		Posicionable p;
		if (SesionFacebook.get().isSignedIn()) {
			DaoPronostico dp = new DaoPronostico();
			PronosticoPK pk = new PronosticoPK();
			pk.setVisitante(SesionFacebook.get().getVisitante());
			pk.setParticipacion(partido.getLocal());
			p = dp.get(pk);
			panelFecha.addParticipante(p);
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
			if (p == null) {
				p = new Pronostico().setParticipacion(partido.getVisita()).setVisitante(SesionFacebook.get().getVisitante());
			}
			panelFecha.addParticipante(p);
		} else {
			p = partido.getVisita();
		}
		add(new TextField<String>("goles-visita", new PropertyModel<>(p, "goles")));
	}

	private void agregarPosicionable(Participacion unaParticipacion, String unSufijo) {
		add(new ContextImage("imagen-visita", partido.getVisita().getEquipo().getLogo()));
		add(new Label("nombre-visita", new Model<>(partido.getVisita().getEquipo().getNombre())));
		Posicionable p;
		if (SesionFacebook.get().isSignedIn()) {
			DaoPronostico dp = new DaoPronostico();
			PronosticoPK pk = new PronosticoPK();
			pk.setVisitante(SesionFacebook.get().getVisitante());
			pk.setParticipacion(partido.getVisita());
			p = dp.get(pk);
			if (p == null) {
				p = new Pronostico().setParticipacion(partido.getVisita()).setVisitante(SesionFacebook.get().getVisitante());
			}
			panelFecha.addParticipante(p);
		} else {
			p = partido.getVisita();
		}
		add(new TextField<String>("goles-visita", new PropertyModel<>(p, "goles")));
	}

}
