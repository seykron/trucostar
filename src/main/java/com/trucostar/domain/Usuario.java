package com.trucostar.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "nombre")
  private String nombre;

  Usuario() {
  }

  public Usuario(String nombre) {
    this.nombre = nombre;
  }

  public Long getId() {
    return id;
  }

  public String getNombre() {
    return nombre;
  }
}
