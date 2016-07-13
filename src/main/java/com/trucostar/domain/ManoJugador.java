package com.trucostar.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.Validate;

@Entity
@Table(name = "mano_jugadores")
public class ManoJugador {

  @Id
  @GeneratedValue
  private long id;

  @ManyToOne
  private Jugador jugador;

  @ElementCollection
  @JoinColumn(name = "carta_id")
  private List<Carta> cartas;

  @ElementCollection
  @JoinColumn(name = "carta_jugada_id")
  private List<Carta> cartasJugadas = new ArrayList<Carta>();

  @Column(name = "puntos")
  private int puntos;

  @ElementCollection
  @Enumerated(EnumType.STRING)
  @CollectionTable(name = "mano_jugador_cantos")
  @Column(name = "canto")
  private List<Cantos> cantos = new ArrayList<Cantos>();

  ManoJugador() {
  }

  public ManoJugador(Jugador jugador, List<Carta> cartas) {
    this.jugador = jugador;
    this.cartas = cartas;
  }

  public void tirar(Carta carta) {
    Validate.isTrue(cartas.contains(carta), "El jugador no tiene esta carta");
    cartasJugadas.add(carta);
  }

  public void cantar(Cantos canto) {
    Validate.isTrue(!cantos.contains(canto), "El jugador ya cantó " + canto.nombre());
    cantos.add(canto);
  }

  public boolean canto(Cantos... cantosParaRevisar) {
    for (Cantos canto : cantosParaRevisar) {
      if (cantos.contains(canto)) {
        return true;
      }
    }
    return false;
  }

  public Jugador getJugador() {
    return jugador;
  }

  public List<Carta> getCartas() {
    return cartas;
  }

  public List<Carta> getCartasJugadas() {
    return cartasJugadas;
  }

  public int getPuntos() {
    return puntos;
  }

  public void darPuntos(int puntos) {
    this.puntos += puntos;
  }

  public boolean ganador(ManoJugador oponente, Cantos canto) {
    return canto.ganador(this, oponente);
  }
}
