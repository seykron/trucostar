package com.trucostar.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "jugadores")
public class Jugador {

  @Id
  @GeneratedValue
  private long id;

  @ManyToOne
  private Usuario usuario;

  @Column(name = "puntos")
  private int puntos;

  Jugador() {
  }

  public Jugador(Usuario usuario) {
    this.usuario = usuario;
  }

  public long getId() {
    return id;
  }
}
