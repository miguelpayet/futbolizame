package pe.trazos.homepage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
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
import pe.trazos.dao.ProviderPosicion;
import pe.trazos.dominio.Participacion;
import pe.trazos.dominio.Partido;
import pe.trazos.dominio.Posicion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends WebPageBase {

	protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private static final Logger log = LoggerFactory.getLogger(HomePage.class);

	protected ModeloHomePage modelo;
	private WebMarkupContainer partidoExterior;
	private Form partidoForm;
	protected RepeatingView partidoRepetidor;
	protected WebMarkupContainer tablaExterior;
	protected DataGridView<Posicion> tablaGrid = null;
	protected WebMarkupContainer tablaInterior;
	protected Label tablaTitulo;

	public HomePage() {
		this(new PageParameters());
	}

	public HomePage(PageParameters parameters) {
		super(parameters);
		init();
		initPagina();
	}

	protected void agregarIntro() throws RuntimeException {
		add(new Label("intro", modelo.getNombreCompetencia()));
	}

	protected void agregarPartidos() {
		// nombre de la fecha
		add(new Label("nombreFecha", new PropertyModel(modelo, "nombreFecha")));
		// formulario
		partidoForm = new FormPartidos("formPartidos", this);
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

	protected void agregarTabla() {
		// título de la tabla
		tablaTitulo = new Label("tituloTabla", new PropertyModel<String>(modelo, "tituloTabla"));
		tablaTitulo.setOutputMarkupId(true);
		add(tablaTitulo);
		// elemento exterior para la tabla
		tablaExterior = new WebMarkupContainer("exteriorTabla");
		tablaExterior.setOutputMarkupId(true);
		add(tablaExterior);
		// contenedor para la tabla
		tablaInterior = new WebMarkupContainer("interiorTabla");
		tablaInterior.setOutputMarkupId(true);
		tablaExterior.add(tablaInterior);
	}

	protected void agregarUnEquipo(String unId, Partido unPartido, Participacion unaPartic, WebMarkupContainer unContainer) {
		Fragment fragCelda = new Fragment("equipo" + unId, "equipo" + unId, this);
		fragCelda.add(new ContextImage("imagen", unaPartic.getEquipo().getLogo()));
		fragCelda.add(new Label("equipo", unaPartic.getEquipo().getNombre()));
		unContainer.add(fragCelda);
	}

	protected void agregarUnaEstadistica(String unId, Partido unPartido, Participacion unaParticipacion, Fragment unContainer) {
	}

	private void calcularPosiciones() {
		modelo.crearPosiciones();
	}

	private List<ICellPopulator<Posicion>> crearColumnas() {
		List<ICellPopulator<Posicion>> columns = new ArrayList<>();
		columns.add(new ImagePopulatorCompetencia<>("logo"));
		columns.add(new PropertyPopulatorCompetencia<>("equipo", "nombre"));
		columns.add(new PropertyPopulatorCompetencia<>("puntos", "puntos"));
		columns.add(new PropertyPopulatorCompetencia<>("partidosJugados", "partidos"));
		columns.add(new PropertyPopulatorCompetencia<>("partidosGanados", "partidos"));
		columns.add(new PropertyPopulatorCompetencia<>("partidosEmpatados", "partidos"));
		columns.add(new PropertyPopulatorCompetencia<>("partidosPerdidos", "partidos"));
		columns.add(new PropertyPopulatorCompetencia<>("golesFavor", "goles"));
		columns.add(new PropertyPopulatorCompetencia<>("golesContra", "goles"));
		columns.add(new PropertyPopulatorCompetencia<>("diferenciaGoles", "goles-diferencia"));
		return columns;
	}

	private void crearPartidos() {
		RepeatingView nuevoRepetidor = new RepeatingView("repeater");
		nuevoRepetidor.setOutputMarkupId(true);
		for (Partido p : modelo.getPartidos()) {
			crearUnPartido(p, nuevoRepetidor);
		}
		partidoRepetidor.replaceWith(nuevoRepetidor);
		partidoForm.add(nuevoRepetidor);
	}

	private void crearTabla() {
		if (tablaGrid == null) {
			Fragment fragTabla = new Fragment("interiorTabla", "tabla-posiciones", this);
			List<ICellPopulator<Posicion>> columns = crearColumnas();
			ProviderPosicion provider = new ProviderPosicion(modelo);
			tablaGrid = new DataGridView<>("rows", columns, provider);
			tablaGrid.setOutputMarkupId(true);
			fragTabla.add(tablaGrid);
			tablaInterior.replaceWith(fragTabla);
			tablaExterior.add(fragTabla);
		}
	}

	protected void crearUnPartido(Partido unPartido, RepeatingView unRepetidor) {
		// container para el partido
		partidoExterior = new WebMarkupContainer(unRepetidor.newChildId());
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
				calcularPosiciones();
				// tabla de posiciones
				crearTabla();
				// elementos a refrescar
				unTarget.add(tablaTitulo);
				unTarget.add(tablaExterior);
			}
		}
	}

	@Override
	protected void doLogout(AjaxRequestTarget unTarget) {
		getSesion().signOut();
		// tabla de posiciones
		calcularPosiciones();
		crearTabla();
		// partidos
		crearPartidos();
		// elementos a refrescar
		unTarget.add(tablaTitulo);
		unTarget.add(tablaExterior);
		unTarget.add(partidoForm);
	}

	protected void formSubmit() {
		log.info("formSubmit");
	}

	private void init() {
		modelo = new ModeloHomePage();
	}

	private void initPagina() throws RuntimeException {
		calcularPosiciones();
		agregarIntro();
		agregarTabla();
		agregarPartidos();
	}

}
