package com.trucostar.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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

  @OneToMany
  @JoinColumn(name = "mano_jugador_id")
  private List<ManoJugador> manoJugadores;

  @OneToMany
  @JoinColumn(name = "baza_id")
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

  public List<Puntaje> puntajes() {
    List<Puntaje> puntajes = new ArrayList<Puntaje>();

    for (ManoJugador manoJugador : manoJugadores) {
      puntajes.add(new Puntaje(this, manoJugador.getJugador().usuario(),
          manoJugador.getPuntos()));
    }

    return puntajes;
  }

  public void envido(Jugador jugador) {
    Validate.isTrue(!hayTruco(), "Ya se cantó truco");
    Validate.isTrue(!hayEnvido(), "Ya se cantó envido");
    ManoJugador manoJugador = buscarManoJugador(jugador);
    manoJugador.envido();
  }

  public void truco(Jugador jugador) {
    Validate.isTrue(!hayTruco(), "Ya se cantó truco");
    ManoJugador manoJugador = buscarManoJugador(jugador);
    manoJugador.truco();
  }

  public Acciones acciones(Jugador jugador) {
    ManoJugador manoJugadorActual = buscarManoJugador(jugador);
    Acciones acciones = new Acciones();

    for (ManoJugador manoJugador : manoJugadores) {
      if (manoJugador.cantoEnvido()) {
        acciones.marcarEnvido(false);
      }

      if (manoJugador.cantoTruco()) {
        acciones.marcarEnvido(false);
        acciones.marcarTruco(false);
      }

      // El quiero lo tiene siempre el otro equipo
      if (manoJugador.getJugador().id() != manoJugadorActual.getJugador().id()
          && !manoJugador.getJugador().equipo().equals(manoJugadorActual.getJugador().equipo())) {
        acciones.marcarQuiero(true);
        acciones.marcarNoQuiero(true);
      }
    }

    return acciones;
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
      if (manoJugador.getJugador().id() == jugador.id()) {
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

  private boolean hayEnvido() {
    for (ManoJugador manoJugador : manoJugadores) {
      if (manoJugador.cantoEnvido()) {
        return true;
      }
    }
    return false;
  }

  private List<ManoJugador> ganadores() {
    List<String> equipos = new ArrayList<String>();

    // Guarda los equipos ganadores
    for (Baza baza : bazas) {
      equipos.add(baza.ganador().getJugador().equipo());
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
      if (manoJugador.getJugador().equipo().equals(equipoGanador)) {
        ganadores.add(manoJugador);
      }
    }

    return ganadores;
  }

  public static class Acciones {
    private boolean envido = true;
    private boolean realEnvido = false;
    private boolean faltaEnvido = false;
    private boolean truco = true;
    private boolean retruco = false;
    private boolean vale4 = false;
    private boolean quiero = false;
    private boolean noQuiero = false;

    Acciones() {
    }

    public void marcarEnvido(boolean estado) {
      envido = estado;
    }

    public boolean isEnvido() {
      return envido;
    }

    public void marcarRealEnvido(boolean estado) {
      realEnvido = estado;
    }

    public boolean isRealEnvido() {
      return realEnvido;
    }

    public void marcarFaltaEnvido(boolean estado) {
      faltaEnvido = estado;
    }

    public boolean isFaltaEnvido() {
      return faltaEnvido;
    }

    public void marcarTruco(boolean estado) {
      truco = estado;
    }

    public boolean isTruco() {
      return truco;
    }

    public void marcarRetruco(boolean estado) {
      retruco = estado;
    }

    public boolean isRetruco() {
      return retruco;
    }

    public void marcarVale4(boolean estado) {
      vale4 = estado;
    }

    public boolean isVale4() {
      return vale4;
    }

    public void marcarQuiero(boolean estado) {
      quiero = estado;
    }

    public boolean isQuiero() {
      return quiero;
    }

    public void marcarNoQuiero(boolean estado) {
      noQuiero = estado;
    }

    public boolean isNoQuiero() {
      return noQuiero;
    }
  }
}
