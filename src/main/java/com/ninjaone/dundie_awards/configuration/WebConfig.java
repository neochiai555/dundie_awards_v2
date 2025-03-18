package com.ninjaone.dundie_awards.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ninjaone.dundie_awards.converter.ActivityConverter;
import com.ninjaone.dundie_awards.converter.EmployeeConverter;
import com.ninjaone.dundie_awards.converter.OrganizationConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ActivityConverter());
        registry.addConverter(new EmployeeConverter());
        registry.addConverter(new OrganizationConverter());
    }
}