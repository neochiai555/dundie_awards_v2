package com.ninjaone.dundie_awards.converter;

import org.springframework.core.convert.converter.Converter;

import com.ninjaone.dundie_awards.dto.EmployeeDto;
import com.ninjaone.dundie_awards.dto.OrganizationDto;
import com.ninjaone.dundie_awards.model.Employee;

/**
 * Converter between employee entity and DTO
 */
public class EmployeeConverter implements Converter<Employee, EmployeeDto> {

	// Convert entity to DTO
	@Override
	public EmployeeDto convert(Employee source) {
		OrganizationDto organizatioDto = new OrganizationConverter().convert(source.getOrganization());
		return new EmployeeDto(source.getId(), source.getFirstName(), source.getLastName(), organizatioDto, source.getDundieAwards());
	}

}
