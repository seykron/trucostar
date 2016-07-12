package com.trucostar.mvc;

import java.util.ArrayList;
import java.util.List;

import com.trucostar.domain.Juego;
import com.trucostar.domain.Jugador;
import com.trucostar.domain.Mano;
import com.trucostar.domain.ManoJugador;
import com.trucostar.domain.Mano.Acciones;

public class JuegoDto {

  private long id;
  private String grupo;
  private List<JugadorDto> jugadores;
  private Acciones acciones;

  JuegoDto(long id, String grupo, List<JugadorDto> jugadores, Acciones acciones) {
    this.id = id;
    this.grupo = grupo;
    this.jugadores = jugadores;
    this.acciones = acciones;
  }

  public long getId() {
    return id;
  }

  public String getGrupo() {
    return grupo;
  }

  public List<JugadorDto> getJugadores() {
    return jugadores;
  }

  public Acciones getAcciones() {
    return acciones;
  }

  public static JuegoDto crear(Juego juego, Jugador jugadorActual) {
    List<JugadorDto> jugadores = new ArrayList<JugadorDto>();
    Mano mano = juego.manoActual();

    for (ManoJugador jugador : mano.jugadores()) {
      jugadores.add(JugadorDto.crear(jugador));
    }

    return new JuegoDto(juego.id(), juego.grupo(), jugadores,
        mano.acciones(jugadorActual));
  }
}
