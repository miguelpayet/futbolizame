package pe.trazos.componentes;

import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

public class ImagePanel extends Panel {

	public ImagePanel(String id, PropertyModel unModelo) {
		super(id);
		String valorPropiedad;
		add(new ContextImage("imagen", (String) unModelo.getObject()));
	}

}
