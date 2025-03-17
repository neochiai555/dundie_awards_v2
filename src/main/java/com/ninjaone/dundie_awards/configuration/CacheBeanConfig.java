package com.ninjaone.dundie_awards.configuration;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.ninjaone.dundie_awards.AwardsCache;

@Configuration
public class CacheBeanConfig {

    @Bean(name = "awardsCache")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    AwardsCache awardsCacheBean() {
        return new AwardsCache();
    }

}