package com.evseev.appconfig;

import com.evseev.controller.ImageComparisonController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class ImageComparisonApplication {

	public static void main(String[] args) {
        Object[] appClasses = {ImageComparisonApplication.class, ImageComparisonConfiguration.class, 
                ImageComparisonController.class};
        SpringApplication.run(appClasses, args);
	}
}
