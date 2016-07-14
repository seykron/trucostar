package com.trucostar.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.Validate;

@Entity
@Table(name = "juegos")
public class Juego {

  private static final int TOTAL_JUGADORES = 4;
  private static final int JUGADORES_POR_EQUIPO = 2;
  private static final String EQUIPO_PIEDRA = "Piedra";
  private static final String EQUIPO_MADERA = "Madera";

  @Id
  @GeneratedValue
  private long id;

  @Column(name = "grupo", unique = true)
  private String grupo;

  @ElementCollection
  @JoinColumn(name = "carta_id")
  private List<Carta> cartas;

  @OneToMany
  @JoinColumn(name = "mano_id")
  private List<Mano> manos = new ArrayList<Mano>();

  @OneToMany
  @JoinColumn(name = "jugador_id")
  private List<Jugador> jugadores = new ArrayList<Jugador>();

  /** Default constructor, for Hibernate */
  Juego() {
  }

  Juego(List<Carta> cartas) {
    this(cartas, null);
  }

  Juego(List<Carta> cartas, String grupo) {
    this.cartas = cartas;
    this.grupo = grupo;
  }

  public Mano repartir() {
    mezclar();

    List<ManoJugador> manoJugadores = new ArrayList<ManoJugador>();

    for (Jugador jugador : jugadores) {
      List<Carta> manoCartas = new ArrayList<Carta>();
      Collections.addAll(manoCartas, darCarta(), darCarta(), darCarta());
      manoJugadores.add(new ManoJugador(jugador, manoCartas));
    }

    return new Mano(manoJugadores);
  }

  public void agregarJugador(Jugador jugador) {
    Validate.isTrue(!empezado(),
        "El juego ya empezó, no se pueden agregar jugadores");
    jugadores.add(jugador);

    if (empezado()) {
      manos.add(repartir());
    }
  }

  public void eliminarJugador(long jugadorId) {
    Validate.isTrue(!empezado(),
        "No se pueden eliminar jugadores cuando empezó el juego");
    Iterator<Jugador> jugadorIt = jugadores.iterator();

    while (jugadorIt.hasNext()) {
      Jugador jugador = jugadorIt.next();

      if (jugador.id() == jugadorId) {
        jugadorIt.remove();
      }
    }
  }

  public Jugador buscarJugador(Usuario usuario) {
    for (Jugador jugador : jugadores) {
      if (usuario.id().equals(jugador.id())) {
        return jugador;
      }
    }
    return null;
  }

  public long id() {
    return id;
  }

  public List<Mano> manos() {
    return manos;
  }

  public Mano manoActual() {
    if (manos.size() > 0) {
      return manos.get(manos.size() - 1);
    }
    return null;
  }

  public String grupo() {
    return grupo;
  }

  public List<Jugador> jugadores() {
    return jugadores;
  }

  public boolean empezado() {
    return jugadores.size() == TOTAL_JUGADORES;
  }

  public String equipoDisponible() {
    Validate.isTrue(!empezado(), "No hay equipos disponibles");

    // TODO: calcular los equipos dinámicamente.
    int piedra = 0;
    int madera = 0;

    for (Jugador jugador : jugadores) {
      if (EQUIPO_PIEDRA.equals(jugador.equipo())) {
        piedra += 1;
      }
      if (EQUIPO_MADERA.equals(jugador.equipo())) {
        madera += 1;
      }
    }

    if (piedra < TOTAL_JUGADORES / JUGADORES_POR_EQUIPO) {
      return EQUIPO_PIEDRA;
    }
    if (madera < TOTAL_JUGADORES / JUGADORES_POR_EQUIPO) {
      return EQUIPO_MADERA;
    }

    throw new IllegalStateException("No hay equipos disponibles");
  }

  private void mezclar() {
    long seed = System.nanoTime();
    Collections.shuffle(cartas, new Random(seed));
  }

  private Carta darCarta() {
    Validate.isTrue(cartas.size() > 0, "No hay más cartas");
    return cartas.remove(cartas.size() - 1);
  }
}
