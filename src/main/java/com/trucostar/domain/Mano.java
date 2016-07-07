package com.trucostar.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.apache.commons.lang3.Validate;

@Entity
@Table(name = "manos")
public class Mano {

  private static final int TOTAL_BAZAS = 3;
  private static final int PUNTAJE_BASE = 1;
  private static final int PUNTAJE_TRUCO = 2;

  @Id
  @GeneratedValue
  private long id;

  @ElementCollection
  @JoinColumn(name = "mano_jugador_id")
  private List<ManoJugador> manoJugadores;

  @ElementCollection
  @JoinColumn(name = "baza_id")
  @AttributeOverrides({
    @AttributeOverride(name = "mano_jugador_id",
        column = @Column(name = "baza_mano_jugador_id"))
  })
  private List<Baza> bazas = new ArrayList<Baza>();

  Mano() {
  }

  public Mano(List<ManoJugador> manoJugadores) {
    this.manoJugadores = manoJugadores;
    proximaBaza();
  }

  public void tirar(Jugador jugador, int cartaIndex) {
    Validate.isTrue(!terminada(), "La mano ya terminó");

    ManoJugador manoJugador = buscarManoJugador(jugador);
    Baza baza = bazaActual();

    baza.tirar(manoJugador, cartaIndex);

    if (terminada()) {
      // Terminó la partida, calcula el puntaje..
      calcularPuntaje();
    } else if (baza.terminada()) {
      // Si terminó la baza crea una nueva.
      proximaBaza();
    }
  }

  public boolean terminada() {
      return bazas.size() == TOTAL_BAZAS
          && bazas.get(TOTAL_BAZAS - 1).terminada();
  }

  public List<ManoJugador> jugadores() {
    return manoJugadores;
  }

  public List<Puntaje> getPuntajes() {
    List<Puntaje> puntajes = new ArrayList<Puntaje>();

    for (ManoJugador manoJugador : manoJugadores) {
      puntajes.add(new Puntaje(this, manoJugador.getJugador().getUsuario(),
          manoJugador.getPuntos()));
    }

    return puntajes;
  }

  private void calcularPuntaje() {
    for (ManoJugador ganador : ganadores()) {
      if (hayTruco()) {
        ganador.darPuntos(PUNTAJE_TRUCO);
      } else {
        ganador.darPuntos(PUNTAJE_BASE);
      }
    }
  }

  private Baza bazaActual() {
    return bazas.get(bazas.size() - 1);
  }

  private ManoJugador buscarManoJugador(Jugador jugador) {
    for (ManoJugador manoJugador : manoJugadores) {
      if (manoJugador.getJugador().getId() == jugador.getId()) {
        return manoJugador;
      }
    }
    throw new IllegalArgumentException("El jugador no está en esta mano");
  }

  private void proximaBaza() {
    Validate.isTrue(!terminada(), "La mano ya terminó");
    bazas.add(new Baza(manoJugadores.size()));
  }

  private boolean hayTruco() {
    for (ManoJugador manoJugador : manoJugadores) {
      if (manoJugador.cantoTruco()) {
        return true;
      }
    }
    return false;
  }

  private List<ManoJugador> ganadores() {
    List<String> equipos = new ArrayList<String>();

    // Guarda los equipos ganadores
    for (Baza baza : bazas) {
      equipos.add(baza.ganador().getJugador().getEquipo());
    }

    String equipoGanador = null;
    int cantidadEquipoGanador = 0;

    // Busca cual fue el equipo que gano más bazas
    for (String equipo : equipos) {
      int cantidad = Collections.frequency(equipos, equipo);

      if (cantidad > cantidadEquipoGanador) {
        equipoGanador = equipo;
        cantidadEquipoGanador = cantidad;
      }
    }

    // Busca los jugadores del equipo ganador
    List<ManoJugador> ganadores = new ArrayList<ManoJugador>();
    for (ManoJugador manoJugador : manoJugadores) {
      if (manoJugador.getJugador().getEquipo().equals(equipoGanador)) {
        ganadores.add(manoJugador);
      }
    }

    return ganadores;
  }
}
