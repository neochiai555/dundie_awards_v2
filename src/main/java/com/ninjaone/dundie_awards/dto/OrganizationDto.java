package com.ninjaone.dundie_awards.dto;

import java.util.Set;

public class OrganizationDto {

  private long id;
  private String name;
  private Set<EmployeeDto> employess;

  public OrganizationDto() {

  }

  public OrganizationDto(String name) {
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

  public Set<EmployeeDto> getEmployess() {
    return employess;
  }

   public void setEmployess(Set<EmployeeDto> employess) {
     this.employess = employess;
   }
	  
}
