package tr.edu.duzce.mf.bm.yancim.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tr.edu.duzce.mf.bm.yancim.model.*;

import java.util.Properties;

import static org.hibernate.cfg.AvailableSettings.*;

@PropertySource(value = "classpath:hibernate.properties", encoding = "UTF-8")
@EnableTransactionManagement
@Configuration
@ComponentScan(basePackages = {"tr.edu.duzce.mf.bm.yancim"})
public class AppConfig {

    @Autowired
    private Environment environment;

    @Bean
    public LocalSessionFactoryBean getSessionFactory() {
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        Properties props = new Properties();
        props.put(DRIVER, environment.getProperty("postgresql.driver"));
        props.put(URL, environment.getProperty("postgresql.url"));
        props.put(USER, environment.getProperty("postgresql.user"));
        props.put(PASS, environment.getProperty("postgresql.password"));
        props.put(SHOW_SQL, environment.getProperty("hibernate.show_sql"));
        props.put(HBM2DDL_AUTO, environment.getProperty("hibernate.hbm2ddl.auto"));
        props.put(DIALECT, environment.getProperty("hibernate.dialect"));
        props.put(C3P0_MIN_SIZE, environment.getProperty("hibernate.c3p0.min_size"));
        props.put(C3P0_MAX_SIZE, environment.getProperty("hibernate.c3p0.max_size"));
        props.put(C3P0_ACQUIRE_INCREMENT, environment.getProperty("hibernate.c3p0.acquire_increment"));
        props.put(C3P0_TIMEOUT, environment.getProperty("hibernate.c3p0.timeout"));
        props.put(C3P0_MAX_STATEMENTS, environment.getProperty("hibernate.c3p0.max_statements"));
        props.put(C3P0_CONFIG_PREFIX + ".initialPoolSize", environment.getProperty("hibernate.c3p0.initialPoolSize"));
        factoryBean.setHibernateProperties(props);
        factoryBean.setAnnotatedClasses(User.class, Gender.class, OperationClaim.class, UserOperationClaim.class, City.class, GameType.class, Room.class, RoomUser.class);
        return factoryBean;
    }

    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager() {
        HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
        hibernateTransactionManager.setSessionFactory(getSessionFactory().getObject());
        return hibernateTransactionManager;
    }
}
