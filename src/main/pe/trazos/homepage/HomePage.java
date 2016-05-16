package pe.trazos.homepage;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.PropertyPopulator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.trazos.componentes.WebPageBase;
import pe.trazos.dao.DaoConcurso;
import pe.trazos.dao.ProviderPosicion;
import pe.trazos.dominio.*;
import pe.trazos.web.FutbolizameApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class HomePage extends WebPageBase {

	protected Concurso concurso;
	protected WebMarkupContainer containerTabla;
	protected SimpleDateFormat dateFormat;
	protected Fecha fecha;
	private final Logger logger = LoggerFactory.getLogger(HomePageAnonimo.class);
	protected RepeatingView repetidor;

	protected class ConcursoImagePopulator<T> extends PropertyPopulator<T> {

		private String propiedad;

		public ConcursoImagePopulator(String property) {
			super(property);
			propiedad = property;
		}

		@Override
		public void populateItem(final Item<ICellPopulator<T>> cellItem, final String componentId, final IModel<T> rowModel) {
			ImagePanel celda = new ImagePanel(componentId, new PropertyModel<>(rowModel, propiedad));
			cellItem.add(celda);
		}

	}

	protected class ConcursoPropertyPopulator<T> extends PropertyPopulator<T> {

		private String clase;
		private String propiedad;

		public ConcursoPropertyPopulator(String unaPropiedad, String unaClase) {
			super(unaPropiedad);
			propiedad = unaPropiedad;
			clase = unaClase;
		}

		@Override
		public void populateItem(final Item<ICellPopulator<T>> cellItem, final String componentId, final IModel<T> rowModel) {
			Label celda = new Label(componentId, new PropertyModel<>(rowModel, propiedad));
			cellItem.add(celda);
			cellItem.add(new AttributeModifier("class", clase));
		}

	}

	protected class FormPartidos extends Form {

		public FormPartidos(String id) {
			super(id);
		}

		@Override
		protected void onSubmit() {
			formSubmit();
		}

	}

	public HomePage() {
		this(new PageParameters());
	}

	public HomePage(PageParameters parameters) {
		super(parameters);
		initPagina();
		agregarIntro();
	}

	protected void agregarIntro() throws RuntimeException {
		String intro = concurso.getNombre();
		if (getSesion().isSignedIn()) {
			intro += " - " + getSesion().getUserName();
		}
		add(new Label("intro", intro));
	}

	protected void agregarPartidos() {
		Form formPartidos = new FormPartidos("formPartidos");
		add(formPartidos);
		repetidor = new RepeatingView("repeater");
		formPartidos.add(repetidor);
		formPartidos.add(new AjaxSubmitLink("save", formPartidos) {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				logger.info("save.onsubmit");
				target.add(containerTabla);
			}
		});
		add(new Label("nombreFecha", fecha.getNombre()));
		fecha.getPartidos().forEach(this::agregarUnPartido);
	}

	protected void agregarTabla() {
		List<ICellPopulator<Posicion>> columns = crearColumnas();
		ProviderPosicion provider = new ProviderPosicion(concurso);
		DataGridView<Posicion> dgv = new DataGridView<>("rows", columns, provider);
		dgv.setOutputMarkupId(true);
		containerTabla = new WebMarkupContainer("containerTabla");
		containerTabla.setOutputMarkupId(true);
		containerTabla.add(dgv);
		add(containerTabla);
	}

	protected void agregarUnEquipo(String unId, Partido unPartido, Participacion unaPartic, WebMarkupContainer unContainer) {
		Fragment fragCelda = new Fragment("equipo" + unId, "equipo" + unId, this);
		fragCelda.add(new ContextImage("imagen", unaPartic.getEquipo().getLogo()));
		fragCelda.add(new Label("equipo", unaPartic.getEquipo().getNombre()));
		unContainer.add(fragCelda);
	}

	protected abstract void agregarUnPartido(Partido unPartido);

	protected abstract void agregarUnaEstadistica(String unId, Partido unPartido, Participacion unaParticipacion, Fragment unContainer);

	protected abstract void calcularPosiciones();

	private List<ICellPopulator<Posicion>> crearColumnas() {
		List<ICellPopulator<Posicion>> columns = new ArrayList<>();
		columns.add(new ConcursoImagePopulator<>("logo"));
		columns.add(new ConcursoPropertyPopulator<>("equipo", "nombre"));
		columns.add(new ConcursoPropertyPopulator<>("puntos", "puntos"));
		columns.add(new ConcursoPropertyPopulator<>("partidosJugados", "partidos"));
		columns.add(new ConcursoPropertyPopulator<>("partidosGanados", "partidos"));
		columns.add(new ConcursoPropertyPopulator<>("partidosEmpatados", "partidos"));
		columns.add(new ConcursoPropertyPopulator<>("partidosPerdidos", "partidos"));
		columns.add(new ConcursoPropertyPopulator<>("golesFavor", "goles"));
		columns.add(new ConcursoPropertyPopulator<>("golesContra", "goles"));
		columns.add(new ConcursoPropertyPopulator<>("diferenciaGoles", "goles-diferencia"));
		return columns;
	}

	protected abstract void formSubmit();

	protected void initPagina() throws RuntimeException {
		dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Integer idConcurso = Integer.valueOf(FutbolizameApplication.get().getInitParameter("concurso.id"));
		if (idConcurso == null) {
			throw new RuntimeException("no está configurado id de concurso");
		}
		DaoConcurso dc = new DaoConcurso();
		concurso = dc.get(idConcurso);
		if (concurso == null) {
			throw new RuntimeException("no está creado el objeto concurso");
		}
		fecha = concurso.getFechaSiguiente(new Date());
		if (fecha == null) {
			throw new RuntimeException("no existe próxima fecha");
		}
		calcularPosiciones();
	}


}
