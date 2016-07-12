package com.trucostar.domain;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class JuegoTest {

  private JuegoFactory juegoFactory = new JuegoFactory();

  @Test(expected = IllegalArgumentException.class)
  public void equipoDisponible() {
    Juego juego = juegoFactory.crearJuego();
    assertTrue(juego.equipoDisponible().equals("Piedra"));
    juego.agregarJugador(crearJugador(juego.equipoDisponible()));
    assertTrue(juego.equipoDisponible().equals("Piedra"));
    juego.agregarJugador(crearJugador(juego.equipoDisponible()));
    assertTrue(juego.equipoDisponible().equals("Madera"));
    juego.agregarJugador(crearJugador(juego.equipoDisponible()));
    assertTrue(juego.equipoDisponible().equals("Madera"));
    juego.agregarJugador(crearJugador(juego.equipoDisponible()));
    juego.equipoDisponible();
  }

  @Test
  public void manoActual() {
    Juego juego = juegoFactory.crearJuego();
    assertTrue(juego.manoActual() == null);
    assertTrue(!juego.empezado());
    Jugador jugador1 = crearJugador(juego.equipoDisponible());
    Jugador jugador2 = crearJugador(juego.equipoDisponible());
    Jugador jugador3 = crearJugador(juego.equipoDisponible());
    Jugador jugador4 = crearJugador(juego.equipoDisponible());
    juego.agregarJugador(jugador1);
    juego.agregarJugador(jugador2);
    juego.agregarJugador(jugador3);
    juego.agregarJugador(jugador4);
    assertTrue(juego.empezado());
    assertTrue(juego.manoActual().jugadores().size() == 4);
    assertTrue(juego.manoActual().jugadores().get(0).getJugador() == jugador1);
    assertTrue(juego.manoActual().jugadores().get(1).getJugador() == jugador2);
    assertTrue(juego.manoActual().jugadores().get(2).getJugador() == jugador3);
    assertTrue(juego.manoActual().jugadores().get(3).getJugador() == jugador4);
  }

  private Jugador crearJugador(String equipo) {
    return new Jugador(new Usuario("pepe", "pepe", "pwd"), equipo);
  }
}
