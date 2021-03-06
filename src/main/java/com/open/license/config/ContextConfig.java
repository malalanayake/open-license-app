package com.open.license.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.security.AuthenticationUser;
import org.apache.activemq.security.SimpleAuthenticationPlugin;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

/**
 * 
 * Distibution under GNU GENERAL PUBLIC LICENSE Version 2, June 1991
 * 
 * @author malalanayake
 * @created Nov 8, 2015 7:47:29 PM
 * 
 * @blog https://malalanayake.wordpress.com/
 */
@Configuration
public class ContextConfig {

	private Logger log = Logger.getLogger(getClass().getName());
	public final String APP_ENV;
	public final String DATASOURCE_URL;
	public final String DATABASE_USERNAME;
	public final String DATABASE_PASSWORD;
	public final boolean START_EMBEDDED_BROKER;
	public final String ACTIVEMQ_HOST_NAME;
	public final String ACTIVEMQ_TCP_PORT;
	public final String ACTIVEMQ_STOMP_PORT;

	public ContextConfig() {
		APP_ENV = System.getProperty("APP_ENV");

		DATASOURCE_URL = System.getProperty("DATASOURCE_URL");
		DATABASE_USERNAME = System.getProperty("DATABASE_USERNAME");
		DATABASE_PASSWORD = System.getProperty("DATABASE_PASSWORD");

		START_EMBEDDED_BROKER = Boolean.valueOf(System.getProperty("START_EMBEDDED_BROKER"));
		ACTIVEMQ_HOST_NAME = System.getProperty("ACTIVEMQ_HOST_NAME");
		ACTIVEMQ_TCP_PORT = System.getProperty("ACTIVEMQ_TCP_PORT");
		ACTIVEMQ_STOMP_PORT = System.getProperty("ACTIVEMQ_STOMP_PORT");
	}

	@Bean
	public PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		log.info("[START:Load Application Properties]");

		String propertiesFilename = "/application-" + APP_ENV + ".properties";

		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		configurer.setLocation(new ClassPathResource(propertiesFilename));
		log.info("[ACTIVATE:Profile " + APP_ENV + "]");
		log.info("[END:Load Application Properties]");

		// Start ActiveMQ Broker if enabled
		startActiveMqBroker();
		return configurer;
	}

	@Bean
	public DataSource dataSource() {
		log.info("[START:Set DataSource]");
		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setUrl(DATASOURCE_URL);
		basicDataSource.setUsername(DATABASE_USERNAME);
		basicDataSource.setPassword(DATABASE_PASSWORD);
		log.info("[END:Set DataSource]");
		return basicDataSource;

	}

	private void startActiveMqBroker() {
		BrokerService broker = new BrokerService();
		if (START_EMBEDDED_BROKER) {
			log.info("[START:ActiveMQ Broker]");
			try {
				SimpleAuthenticationPlugin authentication = new SimpleAuthenticationPlugin();

				List<AuthenticationUser> users = new ArrayList<AuthenticationUser>();
				users.add(new AuthenticationUser("admin", "admin", "admins,publishers,consumers"));
				authentication.setUsers(users);
				broker.setPlugins(new BrokerPlugin[] { authentication });

				broker.addConnector("tcp://" + ACTIVEMQ_HOST_NAME + ":" + ACTIVEMQ_TCP_PORT);
				broker.addConnector("stomp://" + ACTIVEMQ_HOST_NAME + ":" + ACTIVEMQ_STOMP_PORT);
				broker.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
