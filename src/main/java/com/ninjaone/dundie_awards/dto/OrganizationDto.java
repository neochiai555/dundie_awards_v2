package com.ninjaone.dundie_awards.dto;

import java.util.Set;

/**
 * Organization DTO class
 */
public class OrganizationDto {

  private long id;
  private String name;
  private Set<EmployeeDto> employess;

  public OrganizationDto() {

  }

  public OrganizationDto(long id, String name) {
    super();
    this.id = id;
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

  public Set<EmployeeDto> getEmployess() {
    return employess;
  }

   public void setEmployess(Set<EmployeeDto> employess) {
     this.employess = employess;
   }
	  
}
