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
	private AjaxLink[] linksFecha;
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
		linksFecha = new AjaxLink[2];
		linksFecha[0] = new AjaxLink("boton-anterior") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				competencia.getObject().setFechaAnterior();
				if (competencia.getObject().getFechaActual() != null) {
					reemplazarTablaPosiciones();
					reemplazarFecha();
					target.add(fechaExterior);
					target.add(tablaExterior);
				}
			}
		};
		linksFecha[1] = new AjaxLink("boton-siguiente") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				competencia.getObject().setFechaSiguiente();
				if (competencia.getObject().getFechaActual() != null) {
					reemplazarTablaPosiciones();
					reemplazarFecha();
					target.add(fechaExterior);
					target.add(tablaExterior);
				}
			}
		};
		for (AjaxLink link : linksFecha) {
			link.setVisible(false);
			fechaExterior.add(link);
		}
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

	/**
	 * responde a la acción de login de facebook
	 *
	 * @param unUsuario -> id de usuario de facebook
	 * @param unToken   -> token de facebook
	 * @param unTarget  -> objetos a ser actualizados por wicket
	 */
	@Override
	protected void doLogin(String unUsuario, String unToken, AjaxRequestTarget unTarget) {
		// activar los links de siguiente y anterior fecha
		mostrarLinksFecha(unTarget);
		// si ya tiene facebook login, no hacer nada
		if (!getSesion().isLoginFacebook()) {
			if (getSesion().loginFacebook(unUsuario, unToken)) {
				// hacer visibles los links de fecha siguiente y anterior
				// actualizar el display de tabla y partidos
				actualizar(unTarget);
			}
		}
	}

	/**
	 * responde a la acción de logout del api de facebook o a la carga del api de facebook (sin login)
	 *
	 * @param unTarget -> objetos a ser actualizados por wicket
	 */
	@Override
	protected void doLogout(AjaxRequestTarget unTarget) {

		// logout tipo facebook
		getSesion().logoutFacebook();
		// volver a sesion anonima
		getSesion().loginAnonimo();
		// activar los links de siguiente y anterior fecha
		mostrarLinksFecha(unTarget);
		// actualizar el display de tabla y partidos
		actualizar(unTarget);
	}

	private void init() {

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

	/**
	 * mostrar los links de fecha anterior y siguiente cuando termina de cargar la lógica de facebook
	 */
	private void mostrarLinksFecha(AjaxRequestTarget unTarget) {
		for (AjaxLink link : linksFecha) {
			link.setVisible(true);
			unTarget.add(link);
		}
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
