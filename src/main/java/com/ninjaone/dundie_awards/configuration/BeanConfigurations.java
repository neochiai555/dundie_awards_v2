package com.ninjaone.dundie_awards.configuration;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.ninjaone.dundie_awards.AwardsCache;
import com.ninjaone.dundie_awards.messaging.MessageBroker;
import com.ninjaone.dundie_awards.service.ActivityService;
import com.ninjaone.dundie_awards.service.OrganizationBusinessService;

@Configuration
public class BeanConfigurations {
	
	private final ActivityService activityService;
	private final OrganizationBusinessService organizationBusinessService;
	
	// Constructor dependency injection
	public BeanConfigurations (ActivityService activityService, OrganizationBusinessService organizationBusinessService) {
		this.activityService = activityService;
		this.organizationBusinessService = organizationBusinessService;
	}

	// Awards cache singleton bean
    @Bean(name = "awardsCache")
    @Order(Ordered.LOWEST_PRECEDENCE)
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    AwardsCache awardsCacheBean() {
        return new AwardsCache();
    }

    // Message broker singleton bean
    @Bean(name = "messageBroker")
    @Order(Ordered.LOWEST_PRECEDENCE)
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    MessageBroker messageBroker() {
        return new MessageBroker(activityService, organizationBusinessService);
    }
}