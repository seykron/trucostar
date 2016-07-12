package com.trucostar.mvc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.trucostar.context.RequestContext;
import com.trucostar.domain.Carta;
import com.trucostar.domain.Jugador;
import com.trucostar.domain.ManoJugador;
import com.trucostar.domain.Usuario;

public class JugadorDto {

  private long id;
  private String equipo;
  private List<String> cartasMano = new ArrayList<String>();
  private List<String> cartasJugadas = new ArrayList<String>();

  JugadorDto(long id, String equipo, List<String> cartasMano,
      List<String> cartasJugadas) {
    this.id = id;
    this.equipo = equipo;
    this.cartasMano = cartasMano;
    this.cartasJugadas = cartasJugadas;
  }

  public long getId() {
    return id;
  }

  public String getEquipo() {
    return equipo;
  }

  public List<String> getCartasMano() {
    return cartasMano;
  }

  public List<String> getCartasJugadas() {
    return cartasJugadas;
  }

  public static final JugadorDto crear(Jugador jugador) {
    return new JugadorDto(jugador.id(), jugador.equipo(),
        new ArrayList<String>(), new ArrayList<String>());
  }

  public static final JugadorDto crear(ManoJugador manoJugador) {
    Validate.notNull(manoJugador, "El jugador no puede ser nulo");
    Jugador jugador = manoJugador.getJugador();
    List<String> cartasJugadas = new ArrayList<String>();
    List<String> cartasMano = new ArrayList<String>();
    Usuario usuarioActual = RequestContext.usuarioActual();

    for (Carta cartaJugada : manoJugador.getCartasJugadas()) {
      cartasJugadas.add(cartaJugada.nombre());
    }
    for (Carta carta : manoJugador.getCartas()) {
      if (jugador.usuario().id() == usuarioActual.id() &&
          !cartasJugadas.contains(carta.nombre())) {
        cartasMano.add(carta.nombre());
      }
    }
    return new JugadorDto(jugador.id(), jugador.equipo(),
        cartasMano, cartasJugadas);
  }
}
