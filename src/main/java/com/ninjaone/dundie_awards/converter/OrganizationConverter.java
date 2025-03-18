package com.ninjaone.dundie_awards.converter;

import org.springframework.core.convert.converter.Converter;

import com.ninjaone.dundie_awards.dto.OrganizationDto;
import com.ninjaone.dundie_awards.model.Organization;

public class OrganizationConverter 
	implements Converter<Organization,OrganizationDto>	{
	
	@Override
	public OrganizationDto convert(Organization source) {
		return new OrganizationDto(source.getName());
	}
	
}
