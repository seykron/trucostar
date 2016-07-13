package com.trucostar.domain;

public enum Cantos {
  ENVIDO(2, "envido") {
    @Override
    boolean ganador(ManoJugador desafiante, ManoJugador oponente) {
      throw new UnsupportedOperationException("Canto no soportado");
    }
  },
  REAL_ENVIDO(3, "realEnvido") {
    @Override
    boolean ganador(ManoJugador desafiante, ManoJugador oponente) {
      throw new UnsupportedOperationException("Canto no soportado");
    }
  },
  FALTA_ENVIDO(30, "faltaEnvido") {
    @Override
    boolean ganador(ManoJugador desafiante, ManoJugador oponente) {
      throw new UnsupportedOperationException("Canto no soportado");
    }
  },
  TRUCO(2, "truco") {
    @Override
    boolean ganador(ManoJugador desafiante, ManoJugador oponente) {
      throw new UnsupportedOperationException("Canto no soportado");
    }
  },
  RETRUCO(3, "retruco") {
    @Override
    boolean ganador(ManoJugador desafiante, ManoJugador oponente) {
      throw new UnsupportedOperationException("Canto no soportado");
    }
  },
  VALE4(4, "vale4") {
    @Override
    boolean ganador(ManoJugador desafiante, ManoJugador oponente) {
      throw new UnsupportedOperationException("Canto no soportado");
    }
  };

  abstract boolean ganador(ManoJugador desafiante, ManoJugador oponente);

  private int puntos;
  private String nombre;

  private Cantos(int puntos, String nombre) {
    this.puntos = puntos;
    this.nombre = nombre;
  }

  public String nombre() {
    return nombre;
  }

  public int puntos() {
    return puntos;
  }

  public static Cantos porNombre(String nombre) {
    for (Cantos canto : values()) {
      if (canto.nombre().equals(nombre)) {
        return canto;
      }
    }

    return null;
  }
}
