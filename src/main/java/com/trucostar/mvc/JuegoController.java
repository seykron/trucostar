package com.trucostar.mvc;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trucostar.context.RequestContext;
import com.trucostar.domain.Juego;
import com.trucostar.domain.JuegoFactory;
import com.trucostar.domain.Jugador;
import com.trucostar.repo.JuegoDao;

@Controller
@RequestMapping("/juego")
public class JuegoController {

  @Autowired
  private JuegoDao juegoDao;

  @Autowired
  private JuegoFactory juegoFactory;

  @RequestMapping(path = "/entrar/{grupo}",
      method = RequestMethod.POST, produces = "application/json")
  @Transactional
  public @ResponseBody JuegoDto entrarGrupo(
      @PathVariable("grupo") String grupo) {

    Validate.notEmpty(grupo, "El nombre del grupo no puede ser null");

    Juego juego = juegoDao.buscarPorGrupo(grupo);

    if (juego == null) {
      juego = juegoFactory.crearJuego(grupo);
      juegoDao.guardar(juego);
    }

    Jugador jugadorActual = crearJugador(juego.equipoDisponible());
    juego.agregarJugador(jugadorActual);

    return JuegoDto.crear(juego, jugadorActual);
  }

  private Jugador crearJugador(String equipoDisponible) {
    Jugador jugador = new Jugador(RequestContext.usuarioActual(), equipoDisponible);
    juegoDao.guardarJugador(jugador);
    return jugador;
  }

  @RequestMapping(path = "/unir/{grupo}/{usuarioId}",
      method = RequestMethod.POST, produces = "application/json")
  public @ResponseBody JuegoDto unir(@PathVariable("grupo") String grupo,
      @PathVariable("usuarioId") Long usuarioId) {

    Validate.notEmpty(grupo, "El nombre del grupo no puede ser null");
    Validate.notNull(usuarioId, "El id de usuario no puede ser null");

    return null;
  }
}
