package com.trucostar.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

  @Id
  @GeneratedValue
  private long id;

  @OneToMany
  @JoinColumn(name = "mano_jugador_id")
  private List<ManoJugador> manoJugadores;

  @OneToMany
  @JoinColumn(name = "baza_id")
  private List<Baza> bazas = new ArrayList<Baza>();

  @Enumerated(EnumType.STRING)
  @Column(name = "ultimo_canto")
  private Cantos ultimoCanto;

  Mano() {
  }

  public Mano(List<ManoJugador> manoJugadores) {
    this.manoJugadores = manoJugadores;
    proximaBaza();
  }

  public void tirar(Jugador jugador, String nombreCarta) {
    Validate.isTrue(!terminada(), "La mano ya terminó");

    ManoJugador manoJugador = buscarManoJugador(jugador);
    Baza baza = bazaActual();

    baza.tirar(manoJugador, nombreCarta);

    if (terminada()) {
      // Terminó la partida, calcula el puntaje.
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

  public Acciones acciones(Jugador jugador) {
    ManoJugador manoJugadorActual = buscarManoJugador(jugador);
    Acciones acciones = new Acciones();

    acciones.marcarEnvido(!canto(Cantos.ENVIDO));
    acciones.marcarRealEnvido(ultimoCanto == Cantos.ENVIDO
        && !cantor().getJugador().equipo().equals(jugador.equipo()));
    acciones.marcarFaltaEnvido(ultimoCanto == Cantos.REAL_ENVIDO
        && !cantor().getJugador().equipo().equals(jugador.equipo()));
    acciones.marcarTruco(canto(Cantos.ENVIDO) && !canto(Cantos.TRUCO));
    acciones.marcarRetruco(ultimoCanto == Cantos.TRUCO
        && !cantor().getJugador().equipo().equals(jugador.equipo()));
    acciones.marcarVale4(ultimoCanto == Cantos.RETRUCO
        && !cantor().getJugador().equipo().equals(jugador.equipo()));

    // El quiero lo tiene siempre el otro equipo
    if (cantor() != null
        && cantor().getJugador().id() != manoJugadorActual.getJugador().id()
        && !cantor().getJugador().equipo().equals(manoJugadorActual.getJugador().equipo())) {
      acciones.marcarQuiero(true);
      acciones.marcarNoQuiero(true);
    }

    return acciones;
  }

  public void noQuiero() {
    cantor().darPuntos(PUNTAJE_BASE);
    ultimoCanto = null;
  }

  public void quiero(Jugador jugador) {
    ManoJugador cantor = cantor();
    ManoJugador oponente = buscarManoJugador(jugador);

    if (cantor.ganador(oponente, ultimoCanto)) {
      cantor.darPuntos(ultimoCanto.puntos());
    } else {
      oponente.darPuntos(PUNTAJE_BASE);
    }
    ultimoCanto = null;
  }

  public void cantar(Jugador jugador, Cantos canto) {
    Validate.isTrue(ultimoCanto == null, "Todavia no se respondió un canto");

    ManoJugador manoJugador = buscarManoJugador(jugador);
    manoJugador.cantar(canto);
    ultimoCanto = canto;
  }

  private void calcularPuntaje() {
    for (ManoJugador ganador : ganadores()) {
      if (ultimoCanto == null) {
        ganador.darPuntos(PUNTAJE_BASE);
      } else {
        ganador.darPuntos(ultimoCanto.puntos());
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

  private ManoJugador cantor() {
    for (ManoJugador manoJugador : manoJugadores) {
      if (manoJugador.canto(ultimoCanto)) {
        return manoJugador;
      }
    }
    return null;
  }

  private void proximaBaza() {
    Validate.isTrue(!terminada(), "La mano ya terminó");
    bazas.add(new Baza(manoJugadores.size()));
  }

  private boolean canto(Cantos... canto) {
    for (ManoJugador manoJugador : manoJugadores) {
      if (manoJugador.canto(canto)) {
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
