package pe.trazos.homepage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.trazos.dominio.Participacion;
import pe.trazos.dominio.Partido;

import java.text.SimpleDateFormat;

public class PanelFecha extends Panel {

	protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private static final Logger log = LoggerFactory.getLogger(HomePage.class);

	private ModeloFecha fecha;
	private Form partidoForm;
	private RepeatingView partidoRepetidor;

	public PanelFecha(String unId, ModeloFecha unaFecha) {
		super(unId);
		fecha = unaFecha;
		agregarFecha();
		agregarPartidos();
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
			}
		};
		add(link);
	}

	private void agregarPartidos() {
		for (Partido p : fecha.getObject().getPartidos()) {
			log.info("partido: " + p);
			crearUnPartido(p, partidoRepetidor);
		}
	}

	protected void agregarUnEquipo(String unId, Partido unPartido, Participacion unaPartic, WebMarkupContainer unContainer) {
		Fragment fragCelda = new Fragment("equipo" + unId, "equipo" + unId, this);
		fragCelda.add(new ContextImage("imagen", unaPartic.getEquipo().getLogo()));
		fragCelda.add(new Label("equipo", unaPartic.getEquipo().getNombre()));
		unContainer.add(fragCelda);
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
		PartidoComponentPanel p = new PartidoComponentPanel("item-partido", unPartido);
		p.setOutputMarkupId(true);
		partidoExterior.add(p);
		unRepetidor.add(partidoExterior);
	}

}
