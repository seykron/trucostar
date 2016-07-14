package com.trucostar.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "login", unique = true)
  private String login;

  @Column(name = "nombre", nullable = false)
  private String nombre;

  @Column(name = "password", nullable = false)
  private String password;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(name = "usuario_roles")
  private List<Rol> roles = new ArrayList<Rol>();

  Usuario() {
  }

  public Usuario(String login, String nombre, String password) {
    this.login = login;
    this.nombre = nombre;
    this.password = password;
  }

  public Long id() {
    return id;
  }

  public String login() {
    return login;
  }

  public String nombre() {
    return nombre;
  }

  public List<Rol> roles() {
    return roles;
  }

  public void agregarRol(Rol rol) {
    roles.add(rol);
  }

  public String password() {
    return password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles();
  }

  @Override
  public String getPassword() {
    return password();
  }

  @Override
  public String getUsername() {
    return login();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
