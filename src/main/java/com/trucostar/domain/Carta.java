package com.trucostar.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Embeddable
public class Carta {

  public enum Palo {
    ESPADA, BASTO, ORO, COPA
  }

  @Column(name = "palo")
  @Enumerated(EnumType.STRING)
  private Palo palo;

  @Column(name = "numero")
  private int numero;

  @Column(name = "peso")
  private int peso;

  Carta() {
  }

  public Carta(Palo palo, int numero, int peso) {
    this.palo = palo;
    this.numero = numero;
    this.peso = peso;
  }

  public Palo palo() {
    return palo;
  }

  public int numero() {
    return numero;
  }

  public String nombre() {
    return palo().name().toLowerCase() + "-" + String.valueOf(numero());
  }

  public boolean mata(Carta otraCarta) {
    return otraCarta == null || peso > otraCarta.peso();
  }

  int peso() {
    return peso;
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this, false);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj, false);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
