package pe.trazos.homepage;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.DataGridView;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import pe.trazos.dominio.Posicion;

public class PanelTablaPosiciones extends Panel{
	protected WebMarkupContainer tablaExterior;
	protected DataGridView<Posicion> tablaGrid = null;

	public PanelTablaPosiciones(String unId) {
		super(unId);
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

}
