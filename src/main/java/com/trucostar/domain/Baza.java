package com.trucostar.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;

import org.apache.commons.lang3.Validate;

@Embeddable
public class Baza {

  @ElementCollection
  @JoinColumn(name = "mano_jugador_id")
  private List<ManoJugador> manoJugadores = new ArrayList<ManoJugador>();

  public void tirar(ManoJugador manoJugador, int cartaIndex) {
    Validate.isTrue(cartaIndex <= manoJugador.getCartas().size(),
        "La carta especificada no existe");
    Carta carta = manoJugador.getCartas().get(cartaIndex);
    manoJugador.tirar(carta);
    manoJugadores.add(manoJugador);
  }

  public boolean yaTiro(ManoJugador manoJugadorRequerido) {
    for (ManoJugador manoJugador : manoJugadores) {
      if (manoJugador.getJugador().getId() == manoJugadorRequerido.getJugador().getId()) {
        return true;
      }
    }

    return false;
  }

  public ManoJugador ganador() {
    ManoJugador ganador = null;
    Carta mejorCarta = null;

    for (ManoJugador manoJugador : manoJugadores) {
      List<Carta> cartasJugadas = manoJugador.getCartasJugadas();

      Validate.isTrue(cartasJugadas.size() == manoJugador.getCartas().size(),
          "Todavia faltan jugar cartas en esta baza");
      Carta ultimaCarta = cartasJugadas.get(cartasJugadas.size() - 1);

      if (ultimaCarta.mata(mejorCarta)) {
        mejorCarta = ultimaCarta;
        ganador = manoJugador;
      }
    }

    return ganador;
  }

  public List<ManoJugador> getManoJugadores() {
    return manoJugadores;
  }
}
