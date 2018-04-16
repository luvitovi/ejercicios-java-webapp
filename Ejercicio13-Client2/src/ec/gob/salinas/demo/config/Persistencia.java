package ec.gob.salinas.demo.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class Persistencia {

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource datasource = new DriverManagerDataSource();
		
		datasource.setDriverClassName("org.postgresql.Driver");
		datasource.setUrl("jdbc:postgresql://127.0.0.1/gadms");
		datasource.setUsername("postgres");
		datasource.setPassword("postgres");
		
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
		
		//TODO: Asignar estas propiedades
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
		
		propiedades.setProperty("eclipselink.logging.level", "FINE");
		propiedades.setProperty("eclipselink.logging.parameters", "true");
		propiedades.setProperty("eclipselink.connection-pool.default.initial", "1");
		propiedades.setProperty("eclipselink.connection-pool.default.min", "10");
		propiedades.setProperty("eclipselink.connection-pool.default.max", "100000");
		propiedades.setProperty("eclipselink.weaving.internal", "false");
		propiedades.setProperty("eclipselink.weaving", "false");
		
		return propiedades;
		
	}

	
}
