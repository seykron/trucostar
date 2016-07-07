package com.trucostar.domain;

import static com.trucostar.domain.Carta.Palo.ESPADA;
import static com.trucostar.domain.Carta.Palo.BASTO;
import static com.trucostar.domain.Carta.Palo.ORO;
import static com.trucostar.domain.Carta.Palo.COPA;

import java.util.ArrayList;
import java.util.List;

import com.trucostar.domain.Carta.Palo;

public class JuegoFactory {

  private enum TipoCartas {
    AS_ESPADA(1, 100),
    AS_BASTO(1, 99),
    SIETE_ESPADAS(7, 98),
    SIETE_ORO(1, 97),
    TRES(3, 96),
    DOS(2, 95),
    AS_FALSO(1, 94),
    REY(12, 93),
    CABALLO(11, 92),
    SOTA(10, 91),
    SIETE_FALSO(7, 90),
    SEIS(6, 89),
    CINCO(5, 88),
    CUATRO(4, 87);

    private int numero;
    private int peso;

    private TipoCartas(int numero, int peso) {
      this.numero = numero;
      this.peso = peso;
    }
  }

  public Juego crearJuego() {
    return new Juego(crearMazo());
  }

  private List<Carta> crearMazo() {
    List<Carta> cartas = new ArrayList<Carta>();
    crearCartas(cartas, TipoCartas.AS_ESPADA, ESPADA);
    crearCartas(cartas, TipoCartas.AS_BASTO, BASTO);
    crearCartas(cartas, TipoCartas.SIETE_ESPADAS, ESPADA);
    crearCartas(cartas, TipoCartas.SIETE_ORO, ORO);
    crearCartas(cartas, TipoCartas.TRES, ESPADA, ORO, BASTO, COPA);
    crearCartas(cartas, TipoCartas.DOS, ESPADA, ORO, BASTO, COPA);
    crearCartas(cartas, TipoCartas.AS_FALSO, ORO, COPA);
    crearCartas(cartas, TipoCartas.REY, ESPADA, ORO, BASTO, COPA);
    crearCartas(cartas, TipoCartas.CABALLO, ESPADA, ORO, BASTO, COPA);
    crearCartas(cartas, TipoCartas.SOTA, ESPADA, ORO, BASTO, COPA);
    crearCartas(cartas, TipoCartas.SIETE_FALSO, BASTO, COPA);
    crearCartas(cartas, TipoCartas.SEIS, ESPADA, ORO, BASTO, COPA);
    crearCartas(cartas, TipoCartas.CINCO, ESPADA, ORO, BASTO, COPA);
    crearCartas(cartas, TipoCartas.CUATRO, ESPADA, ORO, BASTO, COPA);
    return cartas;
  }

  private void crearCartas(List<Carta> cartas, TipoCartas tipoCarta, Palo... palos) {
    for (Palo palo : palos) {
      cartas.add(new Carta(palo, tipoCarta.numero, tipoCarta.peso));
    }
  }
}
