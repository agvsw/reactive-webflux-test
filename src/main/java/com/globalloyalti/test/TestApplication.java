package com.globalloyalti.test;

import com.globalloyalti.test.config.RedisProperties;
import io.r2dbc.spi.ConnectionFactory;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.web.reactive.config.EnableWebFlux;
import redis.embedded.RedisServer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;

@EnableR2dbcRepositories("com.globalloyalti.test.dao.h2db")
@EnableRedisRepositories("com.globalloyalti.test.dao.redis")
@EnableWebFlux
@SpringBootApplication
public class TestApplication {

	private RedisServer redisServer;

	@Bean
	ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

		ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
		initializer.setConnectionFactory(connectionFactory);
		initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));

		return initializer;
	}

//	public TestApplication(RedisProperties redisProperties) {
//		redisServer = new RedisServer(redisProperties.getRedisPort());
//	}

	@PostConstruct
	public void startRedis() {
		redisServer = new RedisServer(6370);
		redisServer.start();
	}

	@PreDestroy
	public void stopRedis(){
		redisServer.stop();
	}

	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}

}
