package com.trucostar.domain;

import static com.trucostar.domain.Carta.Palo.BASTO;
import static com.trucostar.domain.Carta.Palo.COPA;
import static com.trucostar.domain.Carta.Palo.ESPADA;
import static com.trucostar.domain.Carta.Palo.ORO;

import java.util.ArrayList;
import java.util.List;

import com.trucostar.domain.Carta.Palo;

public class JuegoFactory {

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
      cartas.add(new Carta(palo, tipoCarta.getNumero(), tipoCarta.getPeso()));
    }
  }
}
