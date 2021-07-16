package oneapp.incture.workbox.demo.test.config;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class HibernateConfiguration {

	
	//@Value("${db.driver}")
		private String DRIVER="com.sap.db.jdbc.Driver";
	 
		//@Value("${db.password}")
		private String PASSWORD="Vp5OwatFxF1meAf2F_Q7pYLXN38AAzI2w8L5f8YxUKpfHs9SAZKBveiVjZ.597F4XUbiz-LrMCUpA6wVpqiSEx2T5XAALemC_IMhxL9_W-ZlGoIB1ry20KN7mw_s4np1";
		
	 
		//@Value("${db.url}")
		private String URL="jdbc:sap://zeus.hana.prod.eu-central-1.whitney.dbaas.ondemand.com:24458?encrypt=true&validateCertificate=true&currentschema=WORKBOX";
	 
		//@Value("${db.username}")
		private String USERNAME="WORKBOX";
	 
		//@Value("${hibernate.dialect}")
		private String DIALECT="org.hibernate.dialect.HANAColumnStoreDialect";
	 
		//@Value("${hibernate.show_sql}")
		private String SHOW_SQL="false";
	 
		//@Value("${hibernate.hbm2ddl.auto}")
		private String HBM2DDL_AUTO="update";
	 
		//@Value("${entitymanager.packagesToScan}")
		private String PACKAGES_TO_SCAN="oneapp.incture.workbox.demo";
 
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(DRIVER);
		dataSource.setUrl(URL);
		dataSource.setUsername(USERNAME);
		dataSource.setPassword(PASSWORD);
		return dataSource;
	}
 
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
	
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(PACKAGES_TO_SCAN);
		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", DIALECT);
		hibernateProperties.put("hibernate.show_sql", SHOW_SQL);
		hibernateProperties.put("hibernate.hbm2ddl.auto", HBM2DDL_AUTO);
		sessionFactory.setHibernateProperties(hibernateProperties);
 
		return sessionFactory;
	}
 
	@Bean
	public HibernateTransactionManager transactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory().getObject());
		return transactionManager;
	}	
}