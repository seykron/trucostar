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

  @Id
  @GeneratedValue
  private long id;

  @Column(name = "grupo")
  private String grupo;

  @ElementCollection
  @JoinColumn(name = "carta_id")
  private List<Carta> cartas;

  @OneToMany
  @JoinColumn(name = "mano_id")
  private List<Mano> manos;

  @OneToMany
  @JoinColumn(name = "jugador_id")
  private List<Jugador> jugadores = new ArrayList<Jugador>();

  /** Default constructor, for Hibernate */
  Juego() {
  }

  Juego(List<Carta> cartas) {

  }

  public Mano repartir() {
    mezclar();

    List<ManoJugador> manoJugadores = new ArrayList<ManoJugador>();

    for (Jugador jugador : jugadores) {
      List<Carta> manoCartas = new ArrayList<Carta>();
      Collections.addAll(manoCartas, darCarta(), darCarta(), darCarta());
      manoJugadores.add(new ManoJugador(jugador, manoCartas));
    }

    Mano mano = new Mano(manoJugadores);
    manos.add(mano);

    return mano;
  }

  public void agregarJugador(Jugador jugador) {
    Validate.isTrue(jugadores.size() < 4,
        "No puede haber más de cuatro jugadores");
    jugadores.add(jugador);
  }

  public void eliminarJugador(long jugadorId) {
    Validate.isTrue(manos.size() == 0,
        "No se pueden eliminar jugadores cuando empezó el juego");
    Iterator<Jugador> jugadorIt = jugadores.iterator();

    while (jugadorIt.hasNext()) {
      Jugador jugador = jugadorIt.next();

      if (jugador.getId() == jugadorId) {
        jugadorIt.remove();
      }
    }
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
