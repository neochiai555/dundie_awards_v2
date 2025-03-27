package com.ninjaone.dundie_awards.configuration;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import com.ninjaone.dundie_awards.AwardsCache;
import com.ninjaone.dundie_awards.MessageBroker;
import com.ninjaone.dundie_awards.service.ActivityService;

@Configuration
public class BeanConfigurations {
	
	private final ActivityService activityService;
	
	public BeanConfigurations (ActivityService activityService) {
		this.activityService = activityService;
	}

    @Bean(name = "awardsCache")
    @Order(Ordered.LOWEST_PRECEDENCE)
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    AwardsCache awardsCacheBean() {
        return new AwardsCache();
    }

    @Bean(name = "messageBroker")
    @Order(Ordered.LOWEST_PRECEDENCE)
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    MessageBroker messageBroker() {
        return new MessageBroker(activityService);
    }
}