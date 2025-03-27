package com.ninjaone.dundie_awards.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.ninjaone.dundie_awards.AwardsCache;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.model.Organization;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import com.ninjaone.dundie_awards.repository.OrganizationRepository;

import io.swagger.v3.oas.annotations.Parameter;

/**
 * Employee service 
 */
@Service
public class EmployeeService {
	private final EmployeeRepository employeeRepository;
	private final OrganizationRepository organizationRepository;
    @Qualifier("awardsCache")
    private final AwardsCache awardsCache;
	
	public EmployeeService(
			EmployeeRepository employeeRepository,
			OrganizationRepository organizationRepository,
			AwardsCache awardsCache) {
		this.employeeRepository = employeeRepository;
		this.organizationRepository = organizationRepository;
		this.awardsCache = awardsCache;
	}
	
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    
    @Transactional
    public Employee createEmployee(@RequestBody Employee employee) {
        List<Organization> organizations = organizationRepository.findByName(employee.getOrganization().getName());
        Organization organization = null;
        if (organizations.isEmpty()) {
        	organization = new Organization();
        	organization.setName(employee.getOrganization().getName());
        	organizationRepository.save(organization);
        } else {
        	organization = organizations.get(0);
        }
        employee.setOrganization(organization);
        employee = employeeRepository.save(employee);
        
        awardsCache.setTotalAwards(awardsCache.getTotalAwards() + employee.getDundieAwards());
        
        return employee;
    }
    
    public Optional<Employee> getEmployeeById(@Parameter(description = "Employee ID") @PathVariable(value = "id") Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        return optionalEmployee;
    }
    
    @Transactional
    public Optional<Employee> updateEmployee(@Parameter(description = "Employee ID") @PathVariable(value = "id") Long id, @RequestBody Employee employeeDetails) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (!optionalEmployee.isPresent()) {
            return Optional.empty();
        }

        Employee employee = optionalEmployee.get();
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        
        Integer deltaDundieAwards =
        		Objects.requireNonNullElse(employeeDetails.getDundieAwards(), 0) 
        		- Objects.requireNonNullElse(employee.getDundieAwards(), 0);
        
        employee.setDundieAwards(employeeDetails.getDundieAwards());
        
        List<Organization> organizations = organizationRepository.findByName(employeeDetails.getOrganization().getName());
        Organization organization = null;
        if (organizations.isEmpty()) {
        	organization = new Organization();
        	organization.setName(employeeDetails.getOrganization().getName());
        	organizationRepository.save(organization);
        } else {
        	organization = organizations.get(0);
        }
        employee.setOrganization(organization);

        Employee updatedEmployee = employeeRepository.save(employee);
        
        awardsCache.setTotalAwards(awardsCache.getTotalAwards() + deltaDundieAwards);
        
        return Optional.of(updatedEmployee);
    }
    
    @Transactional
    public Optional<Map<String, Boolean>> deleteEmployee(@Parameter(description = "Employee ID") @PathVariable(value = "id") Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (!optionalEmployee.isPresent()) {
            return Optional.empty();
        }

        Employee employee = optionalEmployee.get();
        employeeRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return Optional.of(response);
    }
}
