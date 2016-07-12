package com.trucostar.repo;

import org.springframework.stereotype.Repository;

import com.trucostar.domain.Juego;
import com.trucostar.domain.Jugador;

@Repository
public class JuegoDao extends BaseDao {

  public void guardarJugador(Jugador jugador) {
    entityManager().persist(jugador);
  }

  public void guardar(Juego juego) {
    entityManager().persist(juego);
  }

  public Juego buscarPorGrupo(String grupo) {
    return (Juego) uniqueResult(entityManager()
      .createQuery("select j from Juego j where j.grupo = :grupo")
        .setParameter("grupo", grupo));
  }

  public Juego buscarPorId(Long id) {
    return entityManager().find(Juego.class, id);
  }
}
