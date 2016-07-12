package com.trucostar.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.trucostar.repo.UsuarioDao;

public class JuegoDetailsService implements UserDetailsService {

  @Autowired
  private UsuarioDao usuarioDao;

  @Override
  public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
    return usuarioDao.buscarPorLogin(login);
  }
}
