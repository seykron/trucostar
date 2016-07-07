package com.trucostar.domain;

import java.util.Arrays;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.apache.commons.lang3.Validate;

@Entity
@Table(name = "manos")
public class Mano {

  @ElementCollection
  @JoinColumn(name = "mano_jugador_id")
  private List<ManoJugador> manoJugadores;

  @Embedded

  @ElementCollection
  @JoinColumn(name = "baza_id")
  @AttributeOverrides({
    @AttributeOverride(name = "mano_jugador_id",
        column = @Column(name = "baza_mano_jugador_id"))
  })
  private List<Baza> bazas = Arrays.asList(new Baza());

  Mano() {
  }

  public Mano(List<ManoJugador> manoJugadores) {
    this.manoJugadores = manoJugadores;
  }

  public void tirar(Jugador jugador, int cartaIndex) {
    ManoJugador manoJugador = buscarManoJugador(jugador);

    Baza baza = bazaActual();

    Validate.isTrue(!baza.yaTiro(manoJugador), "El jugador ya tiró en esta baza");

    baza.tirar(manoJugador, cartaIndex);

    if (baza.getManoJugadores().size() == manoJugadores.size()) {
      // Si terminó la baza crea una nueva.
      bazas.add(new Baza());
    }
  }

  public List<ManoJugador> getJugadores() {
    return manoJugadores;
  }

  private Baza bazaActual() {
    return bazas.get(bazas.size() - 1);
  }

  private ManoJugador buscarManoJugador(Jugador jugador) {
    for (ManoJugador manoJugador : manoJugadores) {
      if (manoJugador.getJugador().getId() == jugador.getId()) {
        return manoJugador;
      }
    }
    throw new IllegalArgumentException("El jugador no está en esta mano");
  }
}
