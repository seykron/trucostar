package com.trucostar.repo;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.trucostar.domain.Juego;
import com.trucostar.domain.Jugador;

@Repository
public class JuegoDao {

  @PersistenceContext
  private EntityManager em;

  public void guardarJugador(Jugador jugador) {
    em.persist(jugador);
  }

  public void guardar(Juego juego) {
    em.persist(juego);
  }

  public Juego buscarPorGrupo(String grupo) {
    return (Juego) uniqueResult(em
      .createQuery("select j from Juego j where j.grupo = :grupo")
        .setParameter("grupo", grupo));
  }

  public Juego buscarPorId(Long id) {
    return em.find(Juego.class, id);
  }

  private Object uniqueResult(Query query) {
    try {
      return query.getSingleResult();
    } catch (NoResultException cause) {
      return null;
    }
  }
}
