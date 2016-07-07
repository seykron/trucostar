package com.trucostar.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "puntajes")
public class Puntaje {

  @Id
  @GeneratedValue
  private long id;

  @ManyToOne
  @JoinColumn(name = "mano_id")
  private Mano mano;

  @ManyToOne
  @JoinColumn(name = "usuario_id")
  private Usuario usuario;

  @Column(name = "puntos")
  private int puntos;

  public Puntaje(Mano mano, Usuario usuario, int puntos) {
    this.mano = mano;
    this.usuario = usuario;
    this.puntos = puntos;
  }

  public long id() {
    return id;
  }

  public Mano mano() {
    return mano;
  }

  public int puntos() {
    return puntos;
  }

  public Usuario usuario() {
    return usuario;
  }
}
