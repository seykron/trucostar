package com.trucostar.domain;

public enum TipoCartas {
  AS_ESPADA(1, 100),
  AS_BASTO(1, 99),
  SIETE_ESPADAS(7, 98),
  SIETE_ORO(1, 97),
  TRES(3, 96),
  DOS(2, 95),
  AS_FALSO(1, 94),
  REY(12, 93),
  CABALLO(11, 92),
  SOTA(10, 91),
  SIETE_FALSO(7, 90),
  SEIS(6, 89),
  CINCO(5, 88),
  CUATRO(4, 87);

  private int numero;
  private int peso;

  private TipoCartas(int numero, int peso) {
    this.numero = numero;
    this.peso = peso;
  }

  public int getNumero() {
    return numero;
  }

  public int getPeso() {
    return peso;
  }
}
