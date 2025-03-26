package com.ninjaone.dundie_awards.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ninjaone.dundie_awards.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	@Query("select e from Employee e where e.organization.id = :organizationId")
	List<Employee> findByOrganizationId(@Param("organizationId") Long organizationId);
}
