package cl.exercise.user.config;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@PropertySource({"classpath:persistence-${spring.profiles.active:default}.properties"})
@EnableJpaRepositories(
    basePackages = "cl.exercise.user.repository",
    entityManagerFactoryRef = "userEntityManager",
    transactionManagerRef = "transactionManagerUser")
public class DatabaseConfig {

  @Autowired private Environment env;

  public DatabaseConfig() {
    super();
  }

  private static String getPropertyAsString(Properties prop) {
    StringWriter writer = new StringWriter();
    prop.list(new PrintWriter(writer));
    return writer.getBuffer().toString();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean userEntityManager() throws SQLException {

    final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    final HashMap<String, Object> properties = new HashMap<String, Object>();

    properties.put("hibernate.dialect", env.getProperty("db.user.dialect"));

    em.setDataSource(userDataSource());
    em.setPackagesToScan("cl.exercise.user.entities");
    em.setJpaVendorAdapter(vendorAdapter);
    em.setJpaPropertyMap(properties);

    log.info("userConfig...");
    log.info("--> env db {}", em.getDataSource().getConnection());
    log.info("--> jpa db {}", em.getJpaPropertyMap());
    log.info("--> vendor db {}", em.getJpaVendorAdapter().toString());

    return em;
  }

  @Bean
  @ConfigurationProperties(prefix = "db.user")
  public DataSource userDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "transactionManagerUser")
  public PlatformTransactionManager userTransactionManager() throws SQLException {
    final JpaTransactionManager transactionManagerUser = new JpaTransactionManager();
    transactionManagerUser.setEntityManagerFactory(userEntityManager().getObject());
    return transactionManagerUser;
  }
}
