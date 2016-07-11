package com.trucostar.domain;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.trucostar.domain.Carta.Palo;

public class ManoTest {

  private static final Carta AS_ESPADA = darCarta(Palo.ESPADA, TipoCartas.AS_ESPADA);
  private static final Carta AS_ORO = darCarta(Palo.ORO, TipoCartas.AS_FALSO);
  private static final Carta CUATRO_ESPADA = darCarta(Palo.ESPADA, TipoCartas.CUATRO);
  private static final Carta SIETE_ORO = darCarta(Palo.ORO, TipoCartas.SIETE_ORO);
  private static final Carta UNO_COPA = darCarta(Palo.COPA, TipoCartas.AS_FALSO);
  private static final Carta REY_ESPADA = darCarta(Palo.ESPADA, TipoCartas.REY);
  private static final Carta AS_BASTO = darCarta(Palo.BASTO, TipoCartas.AS_BASTO);
  private static final Carta SIETE_COPA = darCarta(Palo.COPA, TipoCartas.SIETE_FALSO);
  private static final Carta CABALLO_ORO = darCarta(Palo.ORO, TipoCartas.CABALLO);
  private static final Carta SIETE_ESPADA = darCarta(Palo.ESPADA, TipoCartas.SIETE_ESPADAS);
  private static final Carta CINCO_COPA = darCarta(Palo.COPA, TipoCartas.CINCO);
  private static final Carta SOTA_ORO = darCarta(Palo.ORO, TipoCartas.SOTA);

  private ManoJugador jugador1;
  private ManoJugador jugador2;
  private ManoJugador jugador3;
  private ManoJugador jugador4;

  @Before
  public void setUp() {
    jugador1 = crearJugador("jugador 1", "equipo1", AS_ESPADA, AS_ORO, CUATRO_ESPADA);
    jugador2 = crearJugador("jugador 2", "equipo2", SIETE_ORO, UNO_COPA, REY_ESPADA);
    jugador3 = crearJugador("jugador 3", "equipo1", AS_BASTO, SIETE_COPA, CABALLO_ORO);
    jugador4 = crearJugador("jugador 4", "equipo2", SIETE_ESPADA, CINCO_COPA, SOTA_ORO);
  }

  @Test
  public void tirar() {
    Mano mano = new Mano(Arrays.asList(jugador1, jugador2, jugador3, jugador4));

    // Primera baza: jugador4 gana
    mano.tirar(jugador1.getJugador(), 1);
    mano.tirar(jugador2.getJugador(), 2);
    mano.tirar(jugador3.getJugador(), 2);
    mano.tirar(jugador4.getJugador(), 0);
    assertTrue("La mano no termino", mano.terminada() == false);

    // Segunda baza: jugador3 gana
    mano.tirar(jugador1.getJugador(), 2);
    mano.tirar(jugador2.getJugador(), 0);
    mano.tirar(jugador3.getJugador(), 0);
    mano.tirar(jugador4.getJugador(), 1);
    assertTrue("La mano no termino", mano.terminada() == false);

    // Tercera baza: jugador1 gana
    mano.tirar(jugador1.getJugador(), 0);
    mano.tirar(jugador2.getJugador(), 1);
    mano.tirar(jugador3.getJugador(), 1);
    mano.tirar(jugador4.getJugador(), 2);
    assertTrue("La mano termino", mano.terminada() == true);

    validatePuntaje(mano.puntajes().get(0), mano, jugador1.getJugador().getUsuario(), 1);
    validatePuntaje(mano.puntajes().get(1), mano, jugador2.getJugador().getUsuario(), 0);
    validatePuntaje(mano.puntajes().get(2), mano, jugador3.getJugador().getUsuario(), 1);
    validatePuntaje(mano.puntajes().get(3), mano, jugador4.getJugador().getUsuario(), 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void tirar_errorYaTiro() {
    Mano mano = new Mano(Arrays.asList(jugador1, jugador2));

    mano.tirar(jugador1.getJugador(), 1);
    mano.tirar(jugador1.getJugador(), 0);
  }

  private ManoJugador crearJugador(String nombre, String equipo, Carta... cartas) {
    Usuario usuario = new Usuario(nombre);
    Jugador jugador = new Jugador(usuario, equipo);
    long randomId = Math.round(System.currentTimeMillis() * Math.random());

    ReflectionTestUtils.setField(jugador, "id", randomId);
    return new ManoJugador(jugador, Arrays.asList(cartas));
  }

  private static Carta darCarta(Palo palo, TipoCartas tipoCarta) {
    return new Carta(palo, tipoCarta.getNumero(), tipoCarta.getPeso());
  }

  private void validatePuntaje(Puntaje puntaje, Mano mano, Usuario usuario,
      int puntos) {
    assertTrue("Mano invalida", puntaje.mano().equals(mano));
    assertTrue("Usuario invalido", puntaje.usuario().equals(usuario));
    assertTrue("Puntos invalidos", puntaje.puntos() == puntos);
  }
}
