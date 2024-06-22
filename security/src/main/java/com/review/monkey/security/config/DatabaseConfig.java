package com.review.monkey.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = {"com.review.monkey.security"})
@EnableTransactionManagement
public class DatabaseConfig {
}
