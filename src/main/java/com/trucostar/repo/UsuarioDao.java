package com.trucostar.repo;

import org.springframework.stereotype.Repository;

import com.trucostar.domain.Usuario;

@Repository
public class UsuarioDao extends BaseDao {

  public Usuario buscarPorLogin(String login) {
    return (Usuario) uniqueResult(entityManager()
        .createQuery("select u from Usuario u where u.login = :login")
          .setParameter("login", login));
  }

  public void registrar(Usuario usuario) {
    entityManager().persist(usuario);
  }
}
