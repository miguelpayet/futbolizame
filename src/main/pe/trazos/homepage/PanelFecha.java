package pe.trazos.homepage;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.collections.MultiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.trazos.componentes.WebPageBase;
import pe.trazos.dao.HibernateUtil;
import pe.trazos.dominio.Partido;
import pe.trazos.dominio.Posicionable;

import java.util.Date;
import java.util.List;

public class PanelFecha extends Panel {

	private static final Logger log = LoggerFactory.getLogger(PanelFecha.class);

	private ModeloFecha fecha;
	private FeedbackPanel feedback;
	private HomePage homePage;
	private MultiMap<Integer, Posicionable> participantes; // el Integer es el ID del partido
	private Form partidoForm;
	private RepeatingView partidoRepetidor;

	public PanelFecha(String unId, ModeloFecha unaFecha, HomePage unaVentana) {
		super(unId);
		fecha = unaFecha;
		homePage = unaVentana;
		participantes = new MultiMap<>();
		agregarFecha();
		agregarPartidos();
		agregarFeedback();
	}

	public void addParticipante(Posicionable unPosicionable) {
		participantes.addValue(unPosicionable.getPartido().getId(), unPosicionable);
	}

	private void agregarFecha() {
		// nombre de la fecha
		add(new Label("nombre-fecha", new PropertyModel<String>(fecha, "nombre")));
		// fecha de la fecha
		add(new Label("fecha-fecha", new PropertyModel<String>(fecha, "fechaDia")));
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
				if (validarFechaCompleta()) {
					homePage.actualizar(target);
				} else {
					target.add(feedback);
				}
			}
		};
		partidoForm.add(link);
	}

	private void agregarFeedback() {
		feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		partidoForm.add(feedback);
	}

	private void agregarPartidos() {
		participantes = new MultiMap<>();
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
		WebMarkupContainer p;
		if (unPartido.getFechaPartido().before(new Date())) {
			p = new PanelPartido("item-partido", new Model<>(unPartido));
		} else {
			p = new FormComponentPartido("item-partido", new Model<>(unPartido), this);
		}
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
		// grabar partidos futuros
		final Date ahora = new Date();
		for (Partido part : fecha.getObject().getPartidos()) {
			List<Posicionable> pronosticos = participantes.get(part.getId());
			pronosticos.stream().filter(p -> ahora.before(p.getPartido().getFechaPartido())).forEach(p -> {
				log.info("grabando " + p.toString());
				HibernateUtil.getSessionFactory().getCurrentSession().saveOrUpdate(p);
			});
		}

	}

	private boolean validarFechaCompleta() {
		// recorre desde la fecha y pregunta si cada partido está pronosticado
		boolean golesNulos = false;
		boolean faltanPronosticos = false;
		for (Partido part : fecha.getObject().getPartidos()) {
			// validar si los dos participantes tienen un pronóstico que no es null
			List<Posicionable> pronosticos = participantes.get(part.getId());
			faltanPronosticos = (faltanPronosticos || (pronosticos.size() != 2));
			for (Posicionable posi : pronosticos) {
				golesNulos = (golesNulos || (posi.getGoles() == null));
			}
		}
		if (faltanPronosticos) {
			log.error("los pronósticos no están completos");
			error("por alguna razón, no hemos captado todos tus pronósticos.");
		}
		if (golesNulos) {
			// ¿podemos cambiar el estilo de un partido no pronosticado?
			log.warn("hay goles nulos");
			error("no puedes actualizar la tabla si no pronosticas todos los partidos");
		}
		return (!faltanPronosticos && !golesNulos);
	}

}
