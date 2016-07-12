package com.trucostar.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.Validate;

@Entity
@Table(name = "bazas")
public class Baza {

  @Id
  @GeneratedValue
  private long id;

  @OneToMany
  @JoinColumn(name = "mano_jugador_id")
  private List<ManoJugador> manoJugadores = new ArrayList<ManoJugador>();

  @Transient
  private int numeroJugadores;

  Baza() {
  }

  public Baza(int numeroJugadores) {
      this.numeroJugadores = numeroJugadores;
  }

  public void tirar(ManoJugador manoJugador, int cartaIndex) {
    Validate.isTrue(cartaIndex <= manoJugador.getCartas().size(),
        "La carta especificada no existe");
    Validate.isTrue(!yaTiro(manoJugador), "El jugador ya tiró en esta baza");

    Carta carta = manoJugador.getCartas().get(cartaIndex);
    manoJugador.tirar(carta);
    manoJugadores.add(manoJugador);
  }

  public ManoJugador ganador() {
    Validate.isTrue(terminada(), "La baza no está terminada");

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

  public boolean terminada() {
      return numeroJugadores == manoJugadores.size();
  }

  private boolean yaTiro(ManoJugador manoJugadorRequerido) {
    for (ManoJugador manoJugador : manoJugadores) {
      if (manoJugador.getJugador().id() == manoJugadorRequerido.getJugador().id()) {
        return true;
      }
    }

    return false;
  }
}
