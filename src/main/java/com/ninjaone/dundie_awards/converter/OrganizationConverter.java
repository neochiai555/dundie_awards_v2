package com.ninjaone.dundie_awards.converter;

import org.springframework.core.convert.converter.Converter;

import com.ninjaone.dundie_awards.dto.OrganizationDto;
import com.ninjaone.dundie_awards.model.Organization;

/**
 * Converter between organization entity and DTO
 */
public class OrganizationConverter 
	implements Converter<Organization,OrganizationDto>	{
	
	// Convert entity do DTO
	@Override
	public OrganizationDto convert(Organization source) {
		return new OrganizationDto(source.getId(), source.getName());
	}
	
}
