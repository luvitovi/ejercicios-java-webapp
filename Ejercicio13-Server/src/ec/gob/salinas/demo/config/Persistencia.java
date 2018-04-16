package ec.gob.salinas.demo.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "ec.gob.salinas.demo.repos")
@PropertySource("classpath:/resources/jpa.properties")
public class Persistencia {

	@Autowired
	private Environment env;

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource datasource = new DriverManagerDataSource();
		
		datasource.setDriverClassName(env.getProperty("javax.persistence.jdbc.driver"));
		datasource.setUrl(env.getProperty("javax.persistence.jdbc.url"));
		datasource.setUsername(env.getProperty("javax.persistence.jdbc.user"));
		datasource.setPassword(env.getProperty("javax.persistence.jdbc.password"));
		
		return datasource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		EclipseLinkJpaVendorAdapter vendorAdapter = new EclipseLinkJpaVendorAdapter();
		
		vendorAdapter.setDatabase(Database.POSTGRESQL);
		vendorAdapter.setGenerateDdl(false);
		vendorAdapter.setShowSql(true);
		
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		
		em.setDataSource(dataSource());
		em.setPackagesToScan("ec.gob.salinas.demo.modelo.pojo");
		em.setJpaVendorAdapter(vendorAdapter);
		
		em.setJpaProperties(additionalProperties());
		
		return em;
	}
	
	
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		
		transactionManager.setEntityManagerFactory(emf);
		
		return transactionManager;
	}
	
	Properties additionalProperties() {
		
		Properties propiedades = new Properties();
		
		propiedades.setProperty("eclipselink.logging.level", env.getProperty("eclipselink.logging.level"));
		propiedades.setProperty("eclipselink.logging.parameters", env.getProperty("eclipselink.logging.parameters"));
		propiedades.setProperty("eclipselink.connection-pool.default.initial", env.getProperty("eclipselink.connection-pool.default.initial"));
		propiedades.setProperty("eclipselink.connection-pool.default.min", env.getProperty("eclipselink.connection-pool.default.min"));
		propiedades.setProperty("eclipselink.connection-pool.default.max", env.getProperty("eclipselink.connection-pool.default.max"));
		propiedades.setProperty("eclipselink.weaving.internal", env.getProperty("eclipselink.weaving.internal"));
		propiedades.setProperty("eclipselink.weaving", env.getProperty("eclipselink.weaving"));
		
		return propiedades;
		
	}

	
}
