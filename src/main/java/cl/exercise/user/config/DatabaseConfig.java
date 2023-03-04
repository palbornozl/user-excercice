package cl.exercise.user.config;

import lombok.extern.slf4j.Slf4j;
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

import javax.sql.DataSource;
import java.util.HashMap;

@Slf4j
@Configuration
@PropertySource({"classpath:persistence-${spring.profiles.active:default}.properties"})
@EnableJpaRepositories(
        basePackages = "cl.exercise.user.repository",
        entityManagerFactoryRef = "userEntityManager",
        transactionManagerRef = "transactionManagerUser")
public class DatabaseConfig {
    public DatabaseConfig() {
        super();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean userEntityManager(Environment env) {

        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        final HashMap<String, Object> properties = new HashMap<>();

        properties.put("hibernate.dialect", env.getProperty("db.user.dialect"));

        em.setDataSource(userDataSource());
        em.setPackagesToScan("cl.exercise.user.entities");
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    @ConfigurationProperties(prefix = "db.user")
    public DataSource userDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "transactionManagerUser")
    public PlatformTransactionManager userTransactionManager(Environment env) {
        final JpaTransactionManager transactionManagerUser = new JpaTransactionManager();
        transactionManagerUser.setEntityManagerFactory(userEntityManager(env).getObject());
        return transactionManagerUser;
    }
}
