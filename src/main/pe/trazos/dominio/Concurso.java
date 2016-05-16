package pe.trazos.dominio;

import org.hibernate.annotations.SortComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

@Entity
@Table(name = "concurso")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Concurso implements IObjetoDominio<Integer> {

	static final int PUNTOS_DERROTA = 0;
	static final int PUNTOS_EMPATE = 1;
	static final int PUNTOS_VICTORIA = 3;
	private static final Logger log = LoggerFactory.getLogger(Concurso.class);

	@Column(name = "actualizado")
	private Date actualizado;
	@OneToMany(mappedBy = "concurso", cascade = CascadeType.ALL)
	@SortComparator(ComparadorFecha.class)
	private SortedSet<Fecha> fechas;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idconcurso")
	private Integer id;
	@Column(name = "nombre")
	private String nombre;
	@Column(name = "numero")
	private Integer numero;
	@Transient
	Map<Equipo, Posicion> posiciones;

	public Concurso() {
		posiciones = new HashMap<>();
	}

	public void crearPosiciones() {
		crearPosiciones(null);
	}

	// en vez de recibir la lista de pronosticos podría recibir el userid e irlos recogiendo de la bd
	// pero como ya los tengo en memoria (los pronosticos de una fecha)
	// ¿qué pasa cuando sean varias fechas? transicionar a leer de la BD
	public void crearPosiciones(Map<Participacion, Pronostico> pronosticos) {
		log.debug("crear posiciones");
		resetPosiciones();
		for (Fecha fec : fechas) {
			log.debug("fecha {}", fec);
			for (Partido partido : fec.getPartidos()) {
				log.debug("partido {}", partido);
				Map<Boolean, Participacion> participantes = partido.getParticipantes();
				// buscar los pronosticos y armar el mapa de pronosticos
				Map<Boolean, Pronostico> pronosticoPartido = obtenerPronosticos(participantes, pronosticos);
				// calcular las posiciones para los 2 participantes
				if (pronosticoPartido != null) {
					crearUnaPosicion(pronosticoPartido);
				} else {
					crearUnaPosicion(participantes);
				}
			}
		}
		log.debug("posiciones: " + posiciones.size());
	}

	private void crearUnaPosicion(Map<Boolean, ? extends Posicionable> unosParticipantes) {
		// quiero que se asimile en la posicion cada uno de los pronosticios & participaciones
		// en la participacion está relacionado a través del partido
		// pero en el pronóstico no y por eso vienen en pares
		for (Map.Entry<Boolean, ? extends Posicionable> partEntry : unosParticipantes.entrySet()) {
			Posicionable participante = partEntry.getValue();
			Posicion posicion = obtenerPosicion(participante);
			Posicionable opositor = unosParticipantes.get(!partEntry.getKey());
			posicion.sumar(participante, opositor);
		}
	}

	public Date getActualizado() {
		return actualizado;
	}

	public SortedSet<Fecha> getFechas() {
		return fechas;
	}

	public Integer getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public Integer getNumero() {
		return numero;
	}

	public Map<Equipo, Posicion> getPosiciones() {
		return posiciones;
	}

	public Fecha getFechaSiguiente(Date unaFecha) {
		for (Fecha fecha : fechas) {
			if (fecha.getFecha().compareTo(unaFecha) > 0) {
				return fecha;
			}
		}
		return null;
	}

	private Posicion obtenerPosicion(Posicionable unPosicionable) {
		Posicion posi = posiciones.get(unPosicionable.getEquipo());
		if (posi == null) {
			posi = new Posicion();
			posi.setEquipo(unPosicionable.getEquipo());
			posiciones.put(unPosicionable.getEquipo(), posi);
		}
		return posi;
	}

	private Map<Boolean, Pronostico> obtenerPronosticos(Map<Boolean, Participacion> participantes, Map<Participacion, Pronostico> pronosticos) {
		Map<Boolean, Pronostico> pronosticoPartido = null;
		if (pronosticos != null) {
			pronosticoPartido = new HashMap<>();
			for (Map.Entry<Boolean, Participacion> partEntry : participantes.entrySet()) {
				Participacion participante = partEntry.getValue();
				Pronostico pro = pronosticos.get(participante);
				if (pro != null) {
					pronosticoPartido.put(partEntry.getKey(), pro);
				}
			}
			// si no tengo dos pronosticos prefiero que sea null
			if (pronosticoPartido.size() != 2) {
				pronosticoPartido = null;
			}
		}
		return pronosticoPartido;
	}

	private void resetPosiciones() {
		if (posiciones == null) {
			posiciones = new HashMap<>();
		}
		posiciones.values().forEach(Posicion::reset);
	}

	public void setActualizado(Date actualizado) {
		this.actualizado = actualizado;
	}

	public void setFechas(SortedSet<Fecha> fechas) {
		this.fechas = fechas;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public void setPosiciones(Map<Equipo, Posicion> posiciones) {
		this.posiciones = posiciones;
	}

	/*
	public void zcrearPosicionesAnterior(Map<Participacion, Pronostico> pronosticos) {
		log.debug("crear posiciones");
		resetPosiciones();
		for (Fecha fec : fechas) {
			log.debug("fecha {}", fec);
			for (Partido partido : fec.getPartidos()) {
				for (Participacion partic : partido.getParticipantes().values()) {
					Posicion posi = posiciones.get(partic.getEquipo());
					if (posi == null) {
						posi = new Posicion();
						posi.setEquipo(partic.getEquipo());
						posiciones.put(partic.getEquipo(), posi);
					}
					posi.setConcurso(this);
					Pronostico pro = null;
					if (pronosticos != null) {
						pro = pronosticos.get(partic);
					}
					if (pro != null) {
						log.debug("usando pronostico {}", pro);
						posi.actualizarCon(pro);
					} else {
						posi.actualizarCon(partic);
					}
				}
			}
		}
		log.debug("posiciones: " + posiciones.size());
	}
	*/

}
