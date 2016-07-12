package com.trucostar.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
public class Rol implements GrantedAuthority {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "nombre")
  private String nombre;

  Rol() {
  }

  public Rol(String nombre) {
    this.nombre = nombre;
  }

  public Long id() {
    return id;
  }

  public String nombre() {
    return nombre;
  }

  @Override
  public String getAuthority() {
    return nombre;
  }
}
