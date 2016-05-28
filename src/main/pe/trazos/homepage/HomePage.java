package pe.trazos.homepage;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.trazos.componentes.WebPageBase;
import pe.trazos.dominio.Participacion;
import pe.trazos.dominio.Partido;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomePage extends WebPageBase {

	protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private static final Logger log = LoggerFactory.getLogger(HomePage.class);

	private ModeloCompetencia competencia;
	private ModeloFecha fecha;
	private Form partidoForm;
	private RepeatingView partidoRepetidor;
	private WebMarkupContainer tablaExterior;
	private Component tablaPanel;

	public HomePage() {
		this(new PageParameters());
	}

	public HomePage(PageParameters parameters) {
		super(parameters);
		init();
		initPagina();
	}

	protected void agregarIntro() throws RuntimeException {
		add(new Label("intro", competencia.getNombreCompetencia()));
	}

	private void agregarPanelesVacios() {
		tablaExterior = new WebMarkupContainer("exterior-tabla");
		tablaExterior.setOutputMarkupId(true);
		add(tablaExterior);
		tablaPanel = new Label("panel-tabla", "hola");
		tablaExterior.add(tablaPanel);
	}

	protected void agregarPartidos() {
		// nombre de la fecha
		add(new Label("nombre-fecha", new PropertyModel(competencia, "nombre")));
		// formulario
		partidoForm = new FormPartidos("form-partidos", this);
		partidoForm.setOutputMarkupId(true);
		add(partidoForm);
		// partidoRepetidor para los partidos
		partidoRepetidor = new RepeatingView("repeater");
		partidoForm.add(partidoRepetidor);
		// contenedor para el botón
		WebMarkupContainer wmc = new WebMarkupContainer("botonActualizar");
		wmc.setOutputMarkupId(true);
		add(wmc);
		/* botón para grabar
		partidoForm.add(new AjaxSubmitLink("save", partidoForm) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				log.info("save.onsubmit");
				target.add(tablaExterior);
			}
		});
		*/
	}

	protected void agregarUnEquipo(String unId, Partido unPartido, Participacion unaPartic, WebMarkupContainer unContainer) {
		Fragment fragCelda = new Fragment("equipo" + unId, "equipo" + unId, this);
		fragCelda.add(new ContextImage("imagen", unaPartic.getEquipo().getLogo()));
		fragCelda.add(new Label("equipo", unaPartic.getEquipo().getNombre()));
		unContainer.add(fragCelda);
	}

	protected void agregarUnaEstadistica(String unId, Partido unPartido, Participacion unaParticipacion, Fragment unContainer) {
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

	private void crearTablaPosiciones() {
		Component nuevoPanel = new PanelTablaPosiciones("panel-tabla", competencia);
		tablaPanel.replaceWith(nuevoPanel);
		tablaPanel = nuevoPanel;
	}

	protected void crearUnPartido(Partido unPartido, RepeatingView unRepetidor) {
		// container para el partido
		WebMarkupContainer partidoExterior = new WebMarkupContainer(unRepetidor.newChildId());
		partidoExterior.setOutputMarkupId(true);
		unRepetidor.add(partidoExterior);
		// fragmento con datos del partido
		Fragment fragPartido = new Fragment("listItem", "partido", this);
		partidoExterior.add(fragPartido);
		fragPartido.add(new Label("fecha", dateFormat.format(unPartido.getFechaPartido())));
		WebMarkupContainer fragEquipos = new WebMarkupContainer("filaPartido");
		fragPartido.add(fragEquipos);
		agregarUnEquipo("Local", unPartido, unPartido.getLocal(), fragEquipos);
		agregarUnEquipo("Visitante", unPartido, unPartido.getVisita(), fragEquipos);
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
	}

	@Override
	protected void doLogin(String unUsuario, String unToken, AjaxRequestTarget unTarget) {
		if (!getSesion().isSignedIn()) {
			if (getSesion().signIn(unUsuario, unToken)) {
				// tabla de posiciones
				crearTablaPosiciones();
				// elementos a refrescar
				unTarget.add(tablaExterior);
			}
		}
	}

	@Override
	protected void doLogout(AjaxRequestTarget unTarget) {
		getSesion().signOut();
		// tabla de posiciones
		crearTablaPosiciones();
		// partidos
		//crearPartidos();
		// elementos a refrescar
		unTarget.add(tablaExterior);
	}

	protected void formSubmit() {
		log.info("formSubmit");
	}

	private void init() {
		competencia = new ModeloCompetencia();
		fecha = new ModeloFecha(competencia.getObject().getFechaSiguiente(new Date()));
	}

	private void initPagina() throws RuntimeException {
		agregarIntro();
		agregarPartidos();
		agregarPanelesVacios();
	}

}
