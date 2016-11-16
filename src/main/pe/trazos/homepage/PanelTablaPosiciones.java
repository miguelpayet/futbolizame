package pe.trazos.homepage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import pe.trazos.dao.ProviderPosicion;
import pe.trazos.dominio.Posicion;

import java.util.ArrayList;
import java.util.List;

public class PanelTablaPosiciones extends Panel {

	private static final Logger log = LogManager.getLogger(PanelTablaPosiciones.class);

	private ModeloCompetencia competencia;
	private WebMarkupContainer tablaExterior;

	public PanelTablaPosiciones(String unId, ModeloCompetencia unModelo) {
		super(unId);
		competencia = unModelo;
		calcularPosiciones();
		agregarTabla();
		agregarGrid();
		// todo: agregar contador de fechas pronosticadas
	}

	private void agregarGrid() {
		List<ICellPopulator<Posicion>> columns = crearColumnas();
		ProviderPosicion provider = new ProviderPosicion(competencia);
		DataGridView<Posicion> tablaGrid = new DataGridView<Posicion>("panel-rows", columns, provider) {
			int fila = 0;

			@Override
			protected Item<Posicion> newRowItem(final String id, final int index, final IModel<Posicion> model) {
				Item<Posicion> item = new Item<>(id, index, model);

				item.add(new AttributeModifier("class", "fila-" + String.valueOf(++fila)));
				return item;
			}
		};
		tablaGrid.setOutputMarkupId(true);
		tablaExterior.add(tablaGrid);
	}

	/**
	 * agregar a la página los elementos para el render de la tabla de posiciones
	 * agrega el título y el WebMarkupContainer exterior
	 */
	protected void agregarTabla() {
		// título de la tabla
		Label tituloTabla = new Label("titulo-tabla", new PropertyModel<String>(competencia, "titulo"));
		//tituloTabla.setOutputMarkupId(true);
		add(tituloTabla);
		// subtitulo
		Label subTituloTabla = new Label("subtitulo-tabla", new PropertyModel<>(competencia, "subTitulo"));
		add(subTituloTabla);
		// elemento exterior para la tabla
		tablaExterior = new WebMarkupContainer("panel-tabla");
		tablaExterior.setOutputMarkupId(true);
		add(tablaExterior);
	}

	private void calcularPosiciones() {
		competencia.crearPosiciones();
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

}
