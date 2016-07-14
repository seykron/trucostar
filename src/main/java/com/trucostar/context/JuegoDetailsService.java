package com.trucostar.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.trucostar.domain.Rol;
import com.trucostar.domain.Usuario;
import com.trucostar.repo.UsuarioDao;

@Transactional
public class JuegoDetailsService implements UserDetailsService {

  @Autowired
  private UsuarioDao usuarioDao;

  @Override
  public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
    Usuario usuario = usuarioDao.buscarPorLogin(login);

    if (usuario == null) {
      usuario = new Usuario(login, login, "123456");
      usuario.agregarRol(new Rol("ROLE_JUGADOR"));
      usuarioDao.registrar(usuario);
    }

    return usuario;
  }
}
