package pe.trazos.homepage;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.trazos.componentes.WebPageBase;
import pe.trazos.dao.HibernateUtil;
import pe.trazos.dominio.Partido;
import pe.trazos.dominio.Posicionable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PanelFecha extends Panel {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private static final Logger log = LoggerFactory.getLogger(PanelFecha.class);

	private ModeloFecha fecha;
	private HomePage homePage;
	private ArrayList<Posicionable> participantes;
	private Form partidoForm;
	private RepeatingView partidoRepetidor;

	public PanelFecha(String unId, ModeloFecha unaFecha, HomePage unaVentana) {
		super(unId);
		fecha = unaFecha;
		homePage = unaVentana;
		participantes = new ArrayList<>();
		agregarFecha();
		agregarPartidos();
	}

	public void addParticipante(Posicionable unPosicionable) {
		participantes.add(unPosicionable);
	}

	private void agregarFecha() {
		// nombre de la fecha
		add(new Label("nombre-fecha", new PropertyModel(fecha, "nombre")));
		// formulario
		partidoForm = new FormPartidos("form-partidos");
		partidoForm.setOutputMarkupId(true);
		add(partidoForm);
		// partidoRepetidor para los partidos
		partidoRepetidor = new RepeatingView("repeater");
		partidoForm.add(partidoRepetidor);
		// botón para grabar
		AjaxSubmitLink link = new AjaxSubmitLink("boton-actualizar", partidoForm) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				log.info("save.onsubmit");
				grabar();
				homePage.actualizar(target);
			}
		};
		partidoForm.add(link);
	}

	private void agregarPartidos() {
		participantes = new ArrayList<>();
		for (Partido p : fecha.getObject().getPartidos()) {
			log.debug("partido: " + p);
			crearUnPartido(p, partidoRepetidor);
		}
	}

	private void crearPartidos() {
		RepeatingView nuevoRepetidor = new RepeatingView("repeater");
		nuevoRepetidor.setOutputMarkupId(true);
		for (Partido p : fecha.getObject().getPartidos()) {
			crearUnPartido(p, nuevoRepetidor);
		}
		partidoRepetidor.replaceWith(nuevoRepetidor);
		partidoForm.add(nuevoRepetidor);
	}

	protected void crearUnPartido(Partido unPartido, RepeatingView unRepetidor) {
		// container para el partido
		WebMarkupContainer partidoExterior = new WebMarkupContainer(unRepetidor.newChildId());
		partidoExterior.setOutputMarkupId(true);
		unRepetidor.add(partidoExterior);
		// componente de formulario
		PartidoComponentPanel p = new PartidoComponentPanel("item-partido", new Model<>(unPartido), this);
		p.setOutputMarkupId(true);
		partidoExterior.add(p);
		unRepetidor.add(partidoExterior);
	}

	private Component getParentWindow(Component unComponente) {
		log.debug(unComponente.getClass().getName() + " " + unComponente.toString());
		if (unComponente.getParent() == null) {
			return unComponente;
		} else if (unComponente.getParent() instanceof WebPageBase) {
			return unComponente.getParent();
		} else {
			return getParentWindow(unComponente.getParent());
		}
	}

	private void grabar() {
		log.info("grabar");
		for (Posicionable p : participantes) {
			log.info("grabando " + p.toString());
			HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(p);
		}

	}

}
