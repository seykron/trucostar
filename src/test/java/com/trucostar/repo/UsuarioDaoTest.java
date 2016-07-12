package com.trucostar.repo;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.trucostar.domain.Rol;
import com.trucostar.domain.Usuario;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:META-INF/test-infrastructure.xml",
    "classpath:/META-INF/spring/applicationContext.xml"})
@Transactional
@Rollback
public class UsuarioDaoTest {

  @Autowired
  private UsuarioDao usuarioDao;

  @Test
  public void registrar() {
    Usuario usuario = new Usuario("pepe", "pepe", "pwd");
    usuarioDao.registrar(usuario);
  }

  @Test
  public void buscarPorLogin() {
    Usuario usuario1 = new Usuario("pepe", "pepe", "pwd");
    Usuario usuario2 = new Usuario("juan", "juan", "pwd");
    usuarioDao.registrar(usuario1);
    usuarioDao.registrar(usuario2);

    assertTrue(usuario1.equals(usuarioDao.buscarPorLogin("pepe")));
    assertTrue(usuario2.equals(usuarioDao.buscarPorLogin("juan")));
    assertTrue(usuarioDao.buscarPorLogin("pedro") == null);
  }

  @Test
  public void agregarRol() {
    Usuario usuario1 = new Usuario("pepe", "pepe", "pwd");
    Usuario usuario2 = new Usuario("juan", "juan", "pwd");
    usuario1.agregarRol(new Rol("admin"));
    usuario2.agregarRol(new Rol("jugador"));
    usuarioDao.registrar(usuario1);
    usuarioDao.registrar(usuario2);

    assertTrue("admin".equals(
        usuarioDao.buscarPorLogin("pepe").roles().get(0).nombre()));
    assertTrue("jugador".equals(
        usuarioDao.buscarPorLogin("juan").roles().get(0).nombre()));
  }
}
