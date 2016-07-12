package com.trucostar.repo;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.trucostar.domain.Juego;
import com.trucostar.domain.JuegoFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:META-INF/test-infrastructure.xml",
    "classpath:/META-INF/spring/applicationContext.xml"})
@Transactional
@Rollback
public class JuegoDaoTest {

  @Autowired
  private JuegoDao juegoDao;

  @Autowired
  private JuegoFactory juegoFactory;

  @Test
  public void guardar() {
    Juego juego = juegoFactory.crearJuego();

    juegoDao.guardar(juego);
  }

  @Test
  public void buscarPorGrupo() {
    Juego juego1 = juegoFactory.crearJuego("juego1");
    Juego juego2 = juegoFactory.crearJuego("juego2");
    juegoDao.guardar(juego1);
    juegoDao.guardar(juego2);
    assertTrue(juego1.equals(juegoDao.buscarPorGrupo("juego1")));
    assertTrue(juego2.equals(juegoDao.buscarPorGrupo("juego2")));
    assertTrue(juegoDao.buscarPorGrupo("juego3") == null);
  }
}
