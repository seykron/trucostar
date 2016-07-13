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
    mano.tirar(jugador1.getJugador(), "oro-1");
    mano.tirar(jugador2.getJugador(), "espada-12");
    mano.tirar(jugador3.getJugador(), "oro-11");
    mano.tirar(jugador4.getJugador(), "espada-7");
    assertTrue("La mano no termino", mano.terminada() == false);

    // Segunda baza: jugador3 gana
    mano.tirar(jugador1.getJugador(), "espada-4");
    mano.tirar(jugador2.getJugador(), "oro-7");
    mano.tirar(jugador3.getJugador(), "basto-1");
    mano.tirar(jugador4.getJugador(), "copa-5");
    assertTrue("La mano no termino", mano.terminada() == false);

    // Tercera baza: jugador1 gana
    mano.tirar(jugador1.getJugador(), "espada-1");
    mano.tirar(jugador2.getJugador(), "copa-1");
    mano.tirar(jugador3.getJugador(), "copa-7");
    mano.tirar(jugador4.getJugador(), "oro-10");
    assertTrue("La mano termino", mano.terminada() == true);

    validatePuntaje(mano.puntajes().get(0), mano, jugador1.getJugador().usuario(), 1);
    validatePuntaje(mano.puntajes().get(1), mano, jugador2.getJugador().usuario(), 0);
    validatePuntaje(mano.puntajes().get(2), mano, jugador3.getJugador().usuario(), 1);
    validatePuntaje(mano.puntajes().get(3), mano, jugador4.getJugador().usuario(), 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void tirar_errorYaTiro() {
    Mano mano = new Mano(Arrays.asList(jugador1, jugador2));

    mano.tirar(jugador1.getJugador(), "oro-1");
    mano.tirar(jugador1.getJugador(), "espada-1");
  }

  @Test
  public void acciones_envido() {
    Mano mano = new Mano(Arrays.asList(jugador1, jugador2, jugador3, jugador4));
    assertTrue(mano.acciones(jugador1.getJugador()).isEnvido() == true);
    assertTrue(mano.acciones(jugador2.getJugador()).isEnvido() == true);
    assertTrue(mano.acciones(jugador3.getJugador()).isEnvido() == true);
    assertTrue(mano.acciones(jugador4.getJugador()).isEnvido() == true);

    mano.cantar(jugador1.getJugador(), Cantos.ENVIDO);
    assertTrue(mano.acciones(jugador1.getJugador()).isEnvido() == false);
    assertTrue(mano.acciones(jugador1.getJugador()).isQuiero() == false);
    assertTrue(mano.acciones(jugador1.getJugador()).isNoQuiero() == false);
    assertTrue(mano.acciones(jugador2.getJugador()).isEnvido() == false);
    assertTrue(mano.acciones(jugador2.getJugador()).isQuiero() == true);
    assertTrue(mano.acciones(jugador2.getJugador()).isNoQuiero() == true);

    mano.noQuiero();
    assertTrue(mano.acciones(jugador2.getJugador()).isQuiero() == false);
    assertTrue(mano.acciones(jugador2.getJugador()).isNoQuiero() == false);
 }

  @Test
  public void acciones_truco() {
    Mano mano = new Mano(Arrays.asList(jugador1, jugador2, jugador3, jugador4));

    mano.cantar(jugador1.getJugador(), Cantos.ENVIDO);
    mano.noQuiero();
    assertTrue(mano.acciones(jugador1.getJugador()).isEnvido() == false);
    assertTrue(mano.acciones(jugador2.getJugador()).isEnvido() == false);
    assertTrue(mano.acciones(jugador3.getJugador()).isEnvido() == false);
    assertTrue(mano.acciones(jugador4.getJugador()).isEnvido() == false);

    assertTrue(mano.acciones(jugador1.getJugador()).isTruco() == true);
    assertTrue(mano.acciones(jugador2.getJugador()).isTruco() == true);
    assertTrue(mano.acciones(jugador3.getJugador()).isTruco() == true);
    assertTrue(mano.acciones(jugador4.getJugador()).isTruco() == true);

    mano.cantar(jugador1.getJugador(), Cantos.TRUCO);
    assertTrue(mano.acciones(jugador1.getJugador()).isTruco() == false);
    assertTrue(mano.acciones(jugador1.getJugador()).isRetruco() == false);
    assertTrue(mano.acciones(jugador1.getJugador()).isQuiero() == false);
    assertTrue(mano.acciones(jugador1.getJugador()).isNoQuiero() == false);
    assertTrue(mano.acciones(jugador2.getJugador()).isTruco() == false);
    assertTrue(mano.acciones(jugador2.getJugador()).isRetruco() == true);
    assertTrue(mano.acciones(jugador2.getJugador()).isQuiero() == true);
    assertTrue(mano.acciones(jugador2.getJugador()).isNoQuiero() == true);
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
