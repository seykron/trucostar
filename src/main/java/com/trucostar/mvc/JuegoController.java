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
import com.trucostar.domain.Cantos;
import com.trucostar.domain.Juego;
import com.trucostar.domain.JuegoFactory;
import com.trucostar.domain.Jugador;
import com.trucostar.repo.JuegoDao;

@Controller
@RequestMapping("/juego")
@Transactional
public class JuegoController {

  @Autowired
  private JuegoDao juegoDao;

  @Autowired
  private JuegoFactory juegoFactory;

  @RequestMapping(path = "/entrar/{grupo}",
      method = RequestMethod.POST, produces = "application/json")
  public @ResponseBody JuegoDto entrar(
      @PathVariable("grupo") String grupo) {

    Validate.notEmpty(grupo, "El nombre del grupo no puede ser null");

    Juego juego = juegoDao.buscarPorGrupo(grupo);
    Jugador jugadorActual;

    if (juego == null) {
      juego = juegoFactory.crearJuego(grupo);
      jugadorActual = crearJugador(juego.equipoDisponible());
      juegoDao.guardarJugador(jugadorActual);

      juego.agregarJugador(jugadorActual);
      juegoDao.guardar(juego);
    } else {
      jugadorActual = juego.buscarJugador(RequestContext.usuarioActual());
    }

    return JuegoDto.crear(juego, jugadorActual);
  }

  @RequestMapping(path = "/{id}/estado",
      method = RequestMethod.POST, produces = "application/json")
  public @ResponseBody JuegoDto estado(@PathVariable("id") Long id) {

    Validate.notNull(id, "El id de juego no puede ser null");

    Juego juego = juegoDao.buscarPorId(id);
    Jugador jugadorActual = juego.buscarJugador(RequestContext.usuarioActual());

    return JuegoDto.crear(juego, jugadorActual);
  }

  @RequestMapping(path = "/{id}/tirar/{carta}",
      method = RequestMethod.POST, produces = "application/json")
  @Transactional
  public @ResponseBody JuegoDto tirar(@PathVariable("id") Long id,
      @PathVariable("carta") String carta) {

    Validate.notNull(id, "El id de juego no puede ser null");
    Validate.notNull(carta, "La carta no puede ser null");

    Juego juego = juegoDao.buscarPorId(id);
    Jugador jugadorActual = juego.buscarJugador(RequestContext.usuarioActual());
    juego.manoActual().tirar(jugadorActual, carta);

    return JuegoDto.crear(juego, jugadorActual);
  }

  @RequestMapping(path = "/{id}/cantar/{canto}",
      method = RequestMethod.POST, produces = "application/json")
  @Transactional
  public @ResponseBody JuegoDto cantar(@PathVariable("id") Long id,
      @PathVariable("canto") String canto) {

    Validate.notNull(id, "El id de juego no puede ser null");
    Validate.notNull(canto, "El canto no puede ser null");

    Juego juego = juegoDao.buscarPorId(id);
    Jugador jugadorActual = juego.buscarJugador(RequestContext.usuarioActual());

    if ("quiero".equals(canto)) {
      juego.manoActual().quiero(jugadorActual);
    } else if ("noQuiero".equals(canto)) {
      juego.manoActual().noQuiero();
    } else {
      juego.manoActual().cantar(jugadorActual, Cantos.porNombre(canto));
    }

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
