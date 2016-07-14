package com.trucostar.context;

import org.springframework.security.core.Authentication;

import com.trucostar.domain.Usuario;

public final class RequestContext {

  private RequestContext() {
  }

  public static Usuario usuarioActual() {
    Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext()
        .getAuthentication();
    Usuario usuario = null;

    if (authentication != null) {
      usuario = (Usuario) authentication.getPrincipal();
    }

    return usuario;
  }
}
