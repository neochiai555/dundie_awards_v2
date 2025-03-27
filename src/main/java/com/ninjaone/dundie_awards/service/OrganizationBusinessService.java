package com.ninjaone.dundie_awards.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ninjaone.dundie_awards.AwardsCache;
import com.ninjaone.dundie_awards.exception.OrganizationNotFoundException;
import com.ninjaone.dundie_awards.exception.UnexpectedException;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.model.Organization;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import com.ninjaone.dundie_awards.repository.OrganizationRepository;

/**
 * Organization business logic service 
 */
@Service
public class OrganizationBusinessService {
	private final OrganizationRepository organizationRepository;
	private final EmployeeRepository employeeRepository;
	
    @Qualifier("awardsCache")
    private final AwardsCache awardsCache;
    
	public OrganizationBusinessService(OrganizationRepository organizationRepository, 
			EmployeeRepository employeeRepository,
			@Lazy AwardsCache awardsCache) {
		this.organizationRepository = organizationRepository;
		this.employeeRepository = employeeRepository;
		this.awardsCache = awardsCache;
	}
	
	@Transactional
	public void withDrawDundieAwards(Long organizationId) throws OrganizationNotFoundException, UnexpectedException {
		try {
			Organization organization = organizationRepository.findById(organizationId).orElse(null);
			if (organization == null) {
				throw new OrganizationNotFoundException(String.format("Organization ID = %d not found.", organizationId));
			}
	 		List<Employee> organizationEmployees = employeeRepository.findByOrganizationId(organizationId);
	 		if (organizationEmployees != null && !organizationEmployees.isEmpty()) {
	 			for (Employee employee: organizationEmployees) {
	 				int dundieAwards = Objects.requireNonNullElse(employee.getDundieAwards(), 0);
	 				employee.setDundieAwards(dundieAwards - 1);
	 				employeeRepository.save(employee);
	 				
	 				awardsCache.subtractOneAward();
	 			}
	 		}
		} catch (OrganizationNotFoundException e) {
			throw(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnexpectedException(String.format("Error withdrawing dundie awards from employees of organization ID = %d.", organizationId), e);
		}
	}
}