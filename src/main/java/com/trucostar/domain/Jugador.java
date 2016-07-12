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

  @Column(name = "equipo")
  private String equipo;

  Jugador() {
  }

  public Jugador(Usuario usuario, String equipo) {
    this.usuario = usuario;
    this.equipo = equipo;
  }

  public long id() {
    return id;
  }

  public String equipo() {
    return equipo;
  }

  public Usuario usuario() {
    return usuario;
  }
}
