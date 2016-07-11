package com.trucostar.repo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trucostar.domain.Juego;

@Repository
public class JuegoDao {

  @PersistenceContext
  private EntityManager em;

  @Transactional
  public void save(Juego juego) {
    em.persist(juego);
  }
}
