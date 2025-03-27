package com.ninjaone.dundie_awards;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.ninjaone.dundie_awards.converter.ActivityConverter;
import com.ninjaone.dundie_awards.converter.EmployeeConverter;
import com.ninjaone.dundie_awards.dto.ActivityDto;
import com.ninjaone.dundie_awards.dto.EmployeeDto;
import com.ninjaone.dundie_awards.messaging.MessageBroker;
import com.ninjaone.dundie_awards.model.Activity;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.model.Organization;

import jakarta.inject.Inject;

@SpringBootTest
class DundieAwardsApplicationTests {

	@Inject
	private MessageBroker messageBroker;
	
	@Test
	void contextLoads() {
		Assertions.assertNotNull(messageBroker);
	}
	
	@Test
	void testAwardsCache() {
		AwardsCache cache = new AwardsCache();
		cache.setTotalAwards(0);
		
		Assertions.assertEquals(0, cache.getTotalAwards());
		
		cache.addOneAward();
		Assertions.assertEquals(1, cache.getTotalAwards());		
	}
	
	@Test
	void testActivityConverter() {
		ActivityConverter activityConverter = new ActivityConverter();
		
		LocalDateTime now = LocalDateTime.now();
		Activity activty = new Activity(now, "Activity to be converted");
		ActivityDto activityDto = activityConverter.convert(activty);
		
		Assertions.assertEquals(activityDto.getOccuredAt(), now);
	}
	
	@Test
	void testEmployeeConverter() {
		EmployeeConverter employeeConverter = new EmployeeConverter();

		Organization organization = new Organization("Jmeter");
		Employee employee = new Employee("Jmeter", "Test", organization);
		employee.setDundieAwards(1);
		EmployeeDto employeeDto = employeeConverter.convert(employee);
		
		Assertions.assertEquals("Jmeter", employeeDto.getFirstName());
		Assertions.assertEquals("Test", employeeDto.getLastName());
		Assertions.assertEquals(1, employeeDto.getDundieAwards());
		Assertions.assertEquals("Jmeter", employeeDto.getOrganization().getName());
	}

}
