package com.open.license.config;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.activemq.Service;
import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.security.AuthenticationUser;
import org.apache.activemq.security.JaasAuthenticationPlugin;
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

	@Bean
	public PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		log.info("[START:Load Application Properties]");

		String activeProfile = System.getProperty("APP_ENV");
		String propertiesFilename = "/application-" + activeProfile + ".properties";

		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		configurer.setLocation(new ClassPathResource(propertiesFilename));
		log.info("[ACTIVATE:Profile " + activeProfile + "]");
		log.info("[END:Load Application Properties]");

		startActiveMqBroker();
		return configurer;
	}

	@Bean
	public DataSource dataSource() {
		log.info("[START:Set DataSource]");

		String username = System.getProperty("database.username");
		String password = System.getProperty("database.password");
		String url = System.getProperty("datasource.url");

		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setUrl(url);
		basicDataSource.setUsername(username);
		basicDataSource.setPassword(password);

		log.info("[END:Set DataSource]");
		return basicDataSource;

	}

	private void startActiveMqBroker() {
		log.info("[START:ActiveMQ Broker]");
		BrokerService broker = new BrokerService();
		TransportConnector tc = null;
		TransportConnector tc2 = null;
		try {

			// tc2 = new TransportConnector();
			// tc2.setUri(new URI("ws://localhost:1884"));
			// tc2.setName("WSConn");
			// broker.addConnector(tc2);

			SimpleAuthenticationPlugin authentication = new SimpleAuthenticationPlugin();

			List<AuthenticationUser> users = new ArrayList<AuthenticationUser>();
			users.add(new AuthenticationUser("admin", "password", "admins,publishers,consumers"));
			users.add(new AuthenticationUser("publisher", "password", "publishers,consumers"));
			users.add(new AuthenticationUser("consumer", "password", "consumers"));
			users.add(new AuthenticationUser("guest", "password", "guests"));
			authentication.setUsers(users);
			broker.setPlugins(new BrokerPlugin[] { authentication });
			
			broker.addConnector("tcp://localhost:61616");
			broker.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
