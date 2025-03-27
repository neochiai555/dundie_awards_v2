package com.ninjaone.dundie_awards.dto;

/**
 * Employee DTO class
 */
public class EmployeeDto {

    private long id;
    private String firstName;
    private String lastName;
    private Integer dundieAwards;

    private OrganizationDto organization;

    public EmployeeDto() {

    }

    public EmployeeDto(long id, String firstName, String lastName, OrganizationDto organization, Integer dundieAwards) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.organization = organization;
        this.dundieAwards = dundieAwards;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

    public OrganizationDto getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationDto organization) {
        this.organization = organization;
    }

    public Integer getDundieAwards(){
        return dundieAwards;
    }

	public void setDundieAwards(int dundieAwards) {
		this.dundieAwards = dundieAwards;
	}
    
}