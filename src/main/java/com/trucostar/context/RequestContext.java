package com.trucostar.context;

import com.trucostar.domain.Usuario;

public final class RequestContext {

  private RequestContext() {
  }

  public static Usuario usuarioActual() {
    return new Usuario("jsmith", "jsmith", "pwd");
  }
}
