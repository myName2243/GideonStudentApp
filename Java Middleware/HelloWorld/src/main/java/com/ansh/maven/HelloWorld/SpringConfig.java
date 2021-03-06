package com.ansh.maven.HelloWorld;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("application.properties")
public class SpringConfig {
	
	@Autowired
	Environment env;
	
	@Bean
	public JdbcTemplate jdbcTemplate() {
		DriverManagerDataSource ds = new DriverManagerDataSource(env.getProperty("DB_URL"), env.getProperty("USER"), env.getProperty("PASS"));
		return new JdbcTemplate(ds);
	}
	
	
}
