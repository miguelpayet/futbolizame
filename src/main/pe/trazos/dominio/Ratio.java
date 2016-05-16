package pe.trazos.dominio;

public class Ratio {

	float derrotas;
	float empates;
	float total;
	float victorias;

	public Ratio() {
		total = 0;
		derrotas = 0;
		empates = 0;
		victorias = 0;
	}

	private void agregarDerrota() {
		derrotas++;
		total++;
	}

	private void agregarEmpate() {
		empates++;
		total++;
	}

	public void agregarPartido(Partido unPartido, Equipo unEquipo) {
		if (unPartido.esValido() && unPartido.tieneResultado()) {
			if (unPartido.esEmpate()) {
				agregarEmpate();
			} else if (unPartido.getGanador().getEquipo().equals(unEquipo)) {
				agregarVictoria();
			} else {
				agregarDerrota();
			}
		}
	}

	private void agregarVictoria() {
		victorias++;
		total++;
	}

	public float getDerrotas() {
		return derrotas;
	}

	public float getEmpates() {
		return empates;
	}

	public float getRatioDerrotas() {
		float ratio = 0;
		if (total > 0) {
			ratio = derrotas / total;
		}
		return ratio;
	}

	public float getRatioEmpates() {
		float ratio = 0;
		if (total > 0) {
			ratio = empates / total;
		}
		return ratio;
	}

	public float getRatioVictorias() {
		float ratio = 0;
		if (total > 0) {
			ratio = victorias / total;
		}
		return ratio;
	}

	public float getTotal() {
		return total;
	}

	public float getVictorias() {
		return victorias;
	}

	public String toString() {
		return String.format("% 3.0f%%/% 3.0f%%/% 3.0f%%", getRatioVictorias() * 100, getRatioEmpates() * 100, getRatioDerrotas() * 100);
	}

}
