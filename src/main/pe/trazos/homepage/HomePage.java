package pe.trazos.homepage;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
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
	private WebMarkupContainer fechaExterior;
	private Component fechaPanel;
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

	public void actualizar(AjaxRequestTarget unTarget) {
		// tabla de posiciones
		crearTablaPosiciones();
		// partidos
		crearFecha();
		// elementos a refrescar
		unTarget.add(tablaExterior);
		unTarget.add(fechaExterior);
	}

	protected void agregarIntro() throws RuntimeException {
		add(new Label("intro", competencia.getNombreCompetencia()));
	}

	private void agregarPanelFecha() {
		fechaExterior = new WebMarkupContainer("exterior-fecha");
		fechaExterior.setOutputMarkupId(true);
		add(fechaExterior);
		fechaPanel = new Label("panel-fecha", "hola");
		fechaExterior.add(fechaPanel);
	}

	private void agregarPanelTabla() {
		tablaExterior = new WebMarkupContainer("exterior-tabla");
		tablaExterior.setOutputMarkupId(true);
		add(tablaExterior);
		tablaPanel = new Label("panel-tabla", "hola");
		tablaExterior.add(tablaPanel);
	}

	private void agregarUnaEstadistica(String unId, Partido unPartido, Participacion unaParticipacion, Fragment unContainer) {
	}

	private void crearFecha() {
		Component nuevoPanel = new PanelFecha("panel-fecha", fecha, this);
		fechaPanel.replaceWith(nuevoPanel);
		fechaPanel = nuevoPanel;
	}

	private void crearTablaPosiciones() {
		Component nuevoPanel = new PanelTablaPosiciones("panel-tabla", competencia);
		tablaPanel.replaceWith(nuevoPanel);
		tablaPanel = nuevoPanel;
	}

	@Override
	protected void doLogin(String unUsuario, String unToken, AjaxRequestTarget unTarget) {
		if (!getSesion().isSignedIn()) {
			if (getSesion().signIn(unUsuario, unToken)) {
				actualizar(unTarget);
			}
		}
	}

	@Override
	protected void doLogout(AjaxRequestTarget unTarget) {
		getSesion().signOut();
		actualizar(unTarget);
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
		agregarPanelTabla();
		agregarPanelFecha();
		// hack para forzar a wicket a descartar el objeto interno del modelo y volverlo a leer en las llamadas ajax
		// http://apache-wicket.1842946.n4.nabble.com/Ajax-and-LoadableDetachableModel-td1885858.html
		// la otra opción es usar eager fetching
		competencia.detach();
	}

}
