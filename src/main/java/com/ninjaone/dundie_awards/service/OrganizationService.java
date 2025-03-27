package com.ninjaone.dundie_awards.service;

import java.rmi.UnexpectedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ninjaone.dundie_awards.AwardsCache;
import com.ninjaone.dundie_awards.dto.ActivityDto;
import com.ninjaone.dundie_awards.exception.OrganizationNotFoundException;
import com.ninjaone.dundie_awards.messaging.MessageBroker;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.model.Organization;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import com.ninjaone.dundie_awards.repository.OrganizationRepository;

@Service
public class OrganizationService {
	private final OrganizationRepository organizationRepository;
	private final EmployeeRepository employeeRepository;
	
	@Qualifier("messageBroker")
	private final MessageBroker messageBroker;
	
    @Qualifier("awardsCache")
    private final AwardsCache awardsCache;
    
	public OrganizationService(OrganizationRepository organizationRepository, 
			EmployeeRepository employeeRepository,
			MessageBroker messageBroker,
			AwardsCache awardsCache) {
		this.organizationRepository = organizationRepository;
		this.employeeRepository = employeeRepository;
		this.messageBroker = messageBroker;
		this.awardsCache = awardsCache;
	}
	
	public Organization findById(Long id) {
		return organizationRepository.findById(id).orElse(null);
	}
	
	@Transactional
	public void giveDundieAwards(Long organizationId) throws OrganizationNotFoundException, UnexpectedException {
		try {
			Organization organization = organizationRepository.findById(organizationId).orElse(null);
			if (organization == null) {
				throw new OrganizationNotFoundException(String.format("Organization ID = %d not found.", organizationId));
			}
	 		List<Employee> organizationEmployees = employeeRepository.findByOrganizationId(organizationId);
	 		if (organizationEmployees != null && !organizationEmployees.isEmpty()) {
	 			for (Employee employee: organizationEmployees) {
	 				int dundieAwards = Objects.requireNonNullElse(employee.getDundieAwards(), 0);
	 				employee.setDundieAwards(dundieAwards + 1);
	 				employeeRepository.save(employee);
	 				
	 				awardsCache.addOneAward();
	 			}
	 		}
	 		
	 		messageBroker.initMessageBroker();
	 		messageBroker.sendMessageCreate(new ActivityDto(LocalDateTime.now(),
	 				String.format("Dundie awards given to organization ID = %d", organizationId)), organizationId);
		} catch (OrganizationNotFoundException e) {
			throw(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnexpectedException(String.format("Error giving dundie awards to employees of organization ID = %d.", organizationId), e);
		}
	}	
}