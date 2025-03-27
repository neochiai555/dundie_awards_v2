package com.ninjaone.dundie_awards.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ninjaone.dundie_awards.AwardsCache;
import com.ninjaone.dundie_awards.messaging.MessageBroker;
import com.ninjaone.dundie_awards.repository.ActivityRepository;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@Controller
public class IndexController {

    private EmployeeRepository employeeRepository;
    private ActivityRepository activityRepository;
    @Qualifier("messageBroker")
    private MessageBroker messageBroker;
    @Qualifier("awardsCache")
    private AwardsCache awardsCache;
    
    public IndexController(
    		EmployeeRepository employeeRepository,
    		ActivityRepository activityRepository,
    		MessageBroker messageBroker,
    		AwardsCache awardsCache
    		) {
    	this.employeeRepository = employeeRepository;
    	this.activityRepository = activityRepository;
    	this.messageBroker = messageBroker;
    	this.awardsCache = awardsCache;
    }

    // Redirect to the index page
    @Operation(summary = "Index page")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Success", 
        content = { @Content(mediaType = "text/plain", 
          schema = @Schema(implementation = String.class)
        )}) 
    })
    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("activities", activityRepository.findAll());
        model.addAttribute("queueMessages", messageBroker.getMessages());
        model.addAttribute("totalDundieAwards", awardsCache.getTotalAwards());
        return "index";
    }
}