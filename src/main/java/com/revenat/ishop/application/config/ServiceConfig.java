package com.revenat.ishop.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@Configuration
@ComponentScan({
	"com.revenat.ishop.application.mapper.impl",
	"com.revenat.ishop.application.service.impl",
	"com.revenat.ishop.infrastructure.service.impl",
	"com.revenat.ishop.application.transform.transformer.impl"
	})
public class ServiceConfig {

	/**
	 * 
	 * Also, be particularly careful with BeanPostProcessor and BeanFactoryPostProcessor definitions via @Bean. 
	 * Those should usually be declared as static @Bean methods, not triggering the instantiation of their containing configuration class. 
	 * Otherwise, @Autowired and @Value won’t work on the configuration class itself since it is being created as a bean instance too early.
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() throws Exception {
		PropertySourcesPlaceholderConfigurer conf = new PropertySourcesPlaceholderConfigurer();
		conf.setLocation(new ClassPathResource("application.properties"));
		return conf;
	}
}
