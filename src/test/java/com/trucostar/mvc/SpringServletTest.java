package com.trucostar.mvc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.trucostar.context.JuegoDetailsService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:META-INF/test-infrastructure.xml",
    "classpath:/META-INF/spring/applicationContext.xml",
    "classpath:/META-INF/spring/spring-servlet.xml"})
@Transactional
@Rollback
public class SpringServletTest {

  @Autowired
  private JuegoDetailsService juegoDetailsService;

  @Test
  public void load() {
    juegoDetailsService.loadUserByUsername("test");
  }
}
