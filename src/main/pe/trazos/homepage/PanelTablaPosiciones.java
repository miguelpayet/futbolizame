package pe.trazos.homepage;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import pe.trazos.dao.ProviderPosicion;
import pe.trazos.dominio.Posicion;

import java.util.ArrayList;
import java.util.List;

public class PanelTablaPosiciones extends Panel {

	private ModeloCompetencia competencia;
	private WebMarkupContainer tablaExterior;

	public PanelTablaPosiciones(String unId, ModeloCompetencia unModelo) {
		super(unId);
		competencia = unModelo;
		calcularPosiciones();
		agregarTabla();
		agregarGrid();
	}

	private void agregarGrid() {
		List<ICellPopulator<Posicion>> columns = crearColumnas();
		ProviderPosicion provider = new ProviderPosicion(competencia);
		DataGridView<Posicion> tablaGrid = new DataGridView<>("panel-rows", columns, provider);
		tablaGrid.setOutputMarkupId(true);
		tablaExterior.add(tablaGrid);
	}

	protected void agregarTabla() {
		// título de la tabla
		Label tituloTabla = new Label("titulo-tabla", new PropertyModel<String>(competencia, "titulo"));
		tituloTabla.setOutputMarkupId(true);
		add(tituloTabla);
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