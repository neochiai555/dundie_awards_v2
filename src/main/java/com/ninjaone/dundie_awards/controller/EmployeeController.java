package com.ninjaone.dundie_awards.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ninjaone.dundie_awards.AwardsCache;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.model.Organization;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import com.ninjaone.dundie_awards.repository.OrganizationRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class EmployeeController { 

    private final EmployeeRepository employeeRepository;
    //private final ActivityRepository activityRepository;
    private final OrganizationRepository organizationRepository;
    //private final MessageBroker messageBroker;
    
    @Qualifier("awardsCache")
    private final AwardsCache awardsCache;

    public EmployeeController(
    		EmployeeRepository employeeRepository,
    		//ActivityRepository activityRepository,
    		OrganizationRepository organizationRepository,
    		//MessageBroker messageBroker,  
    		AwardsCache awardsCache) {
    	this.employeeRepository = employeeRepository;
    	//this.activityRepository = activityRepository;
    	this.organizationRepository = organizationRepository;
    	//this.messageBroker = messageBroker;
    	this.awardsCache = awardsCache;
    }
    
    // get all employees
    @Operation(summary = "Get all employees")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Success", 
        content = { @Content(mediaType = "application/json", 
          array = @ArraySchema(schema = @Schema(implementation = Employee.class))
        )}) 
    })
    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // create employee rest api
    @Operation(summary = "Create employee")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Success", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = Employee.class) 
        )}) 
    })
    @PostMapping("/employees")
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

    // get employee by id rest api
    @Operation(summary = "Get employee by ID")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Success", 
        content = { @Content(mediaType = "application/json", 
          array = @ArraySchema(schema = @Schema(implementation = Employee.class))
        )}) 
    })
    @Parameter(in = ParameterIn.PATH, name ="id" ,schema = @Schema(type = "long"))
    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@Parameter(description = "Employee ID") @PathVariable(value = "id") Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            return ResponseEntity.ok(optionalEmployee.get());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // update employee rest api
    @Operation(summary = "Update employee")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Success", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = Employee.class) 
        )}) 
    })
    @Parameter(in = ParameterIn.PATH, name ="id" ,schema = @Schema(type = "long"))
    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@Parameter(description = "Employee ID") @PathVariable(value = "id") Long id, @RequestBody Employee employeeDetails) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (!optionalEmployee.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
        
        return ResponseEntity.ok(updatedEmployee);
    }

    // delete employee rest api
    @Operation(summary = "Delete employee")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Success", 
        content = { @Content(mediaType = "application/json", 
          schema = @Schema(implementation = Map.class) 
        )}) 
    })
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteEmployee(@Parameter(description = "Employee ID") @PathVariable(value = "id") Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (!optionalEmployee.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Employee employee = optionalEmployee.get();
        employeeRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}