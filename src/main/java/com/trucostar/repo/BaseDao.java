package com.trucostar.repo;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class BaseDao {

  @PersistenceContext
  private EntityManager em;

  protected EntityManager entityManager() {
    return em;
  }

  protected Object uniqueResult(Query query) {
    try {
      return query.getSingleResult();
    } catch (NoResultException cause) {
      return null;
    }
  }
}
