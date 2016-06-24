package pe.trazos.homepage;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.trazos.componentes.WebPageBase;
import pe.trazos.dominio.Fecha;

public class HomePage extends WebPageBase {

	private static final Logger log = LoggerFactory.getLogger(HomePage.class);

	private ModeloCompetencia competencia;
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
		reemplazarTablaPosiciones();
		// partidos
		reemplazarFecha();
		// elementos a refrescar
		unTarget.add(tablaExterior);
		unTarget.add(fechaExterior);
	}

	private void agregarEnlacesFecha() {
		AjaxLink linkAnterior = new AjaxLink("boton-anterior") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				log.info("fecha anterior");
				competencia.getObject().setFechaAnterior();
				if (competencia.getObject().getFechaActual() != null) {
					reemplazarTablaPosiciones();
					reemplazarFecha();
					target.add(fechaExterior);
					target.add(tablaExterior);
				}
			}
		};
		fechaExterior.add(linkAnterior);
		AjaxLink linkSiguiente = new AjaxLink("boton-siguiente") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				log.info("fecha siguiente");
				competencia.getObject().setFechaSiguiente();
				if (competencia.getObject().getFechaActual() != null) {
					reemplazarTablaPosiciones();
					reemplazarFecha();
					target.add(fechaExterior);
					target.add(tablaExterior);
				}
			}
		};
		fechaExterior.add(linkSiguiente);
	}

	protected void agregarIntro() throws RuntimeException {
		add(new Label("intro", competencia.getNombreCompetencia()));
	}

	private void agregarPanelFecha() {
		fechaExterior = new WebMarkupContainer("exterior-fecha");
		fechaExterior.setOutputMarkupId(true);
		add(fechaExterior);
		fechaPanel = new Label("panel-fecha", "si tienes tiempo de leer esto es porque algo no funcionó correctamente");
		fechaExterior.add(fechaPanel);
	}

	private void agregarPanelTabla() {
		tablaExterior = new WebMarkupContainer("exterior-tabla");
		tablaExterior.setOutputMarkupId(true);
		add(tablaExterior);
		tablaPanel = new Label("panel-tabla", "si tienes tiempo de leer esto es porque algo no funcionó correctamente");
		tablaExterior.add(tablaPanel);
	}

	@Override
	protected void doLogin(String unUsuario, String unToken, AjaxRequestTarget unTarget) {
		// si ya tiene facebook login, no hacer nada
		log.debug("doLogin");
		if (!getSesion().isLoginFacebook()) {
			if (getSesion().loginFacebook(unUsuario, unToken)) {
				actualizar(unTarget);
			}
		}
	}

	@Override
	protected void doLogout(AjaxRequestTarget unTarget) {
		log.debug("doLogout");
		// logout tipo facebook
		getSesion().logoutFacebook();
		// volver a sesion anonima
		getSesion().loginAnonimo();
		actualizar(unTarget);
	}

	private void init() {
		log.debug("init");
		competencia = new ModeloCompetencia();
		competencia.getObject().setFechaProxima();
	}

	private void initPagina() throws RuntimeException {
		agregarIntro();
		agregarPanelTabla();
		agregarPanelFecha();
		agregarEnlacesFecha();
		// hack para forzar a wicket a descartar el objeto interno del modelo y volverlo a leer en las llamadas ajax
		// http://apache-wicket.1842946.n4.nabble.com/Ajax-and-LoadableDetachableModel-td1885858.html
		// la otra opción es usar eager fetching
		competencia.detach();
	}

	private void reemplazarFecha() {
		Fecha fechaActual = competencia.getObject().getFechaActual();
		Component nuevoPanel = new PanelFecha("panel-fecha", new ModeloFecha(fechaActual), this);
		fechaPanel.replaceWith(nuevoPanel);
		fechaPanel = nuevoPanel;
	}

	private void reemplazarTablaPosiciones() {
		Component nuevoPanel = new PanelTablaPosiciones("panel-tabla", competencia);
		tablaPanel.replaceWith(nuevoPanel);
		tablaPanel = nuevoPanel;
	}

}
