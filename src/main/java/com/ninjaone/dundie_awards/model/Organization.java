package com.ninjaone.dundie_awards.model;

import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "organizations")
public class Organization {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "name")
  private String name;
  
  @OneToMany
  private Set<Employee> employess;

  public Organization() {

  }

  public Organization(String name) {
    super();
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
