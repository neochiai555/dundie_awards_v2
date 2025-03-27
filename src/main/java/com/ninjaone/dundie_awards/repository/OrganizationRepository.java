package com.ninjaone.dundie_awards.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ninjaone.dundie_awards.model.Organization;

/**
 * Organization repository
 */
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
	public List<Organization> findByName(String name); 
}
