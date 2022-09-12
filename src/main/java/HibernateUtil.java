import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class HibernateUtil {
    private final static SessionFactory FACTORY;

    static {
        Configuration conf=new Configuration();

        Properties pros=new Properties();
        pros.put(Environment.DIALECT,"org.hibernate.dialect.OracleDialect");
        pros.put(Environment.DRIVER,"oracle.jdbc.driver.OracleDriver");
        pros.put(Environment.URL,"jdbc:oracle:thin:@localhost:1521:xe");
        pros.put(Environment.USER,"LETRUONG");
        pros.put(Environment.PASS,"270101");
        conf.addAnnotatedClass(entity.Driver.class);

        conf.addAnnotatedClass(entity.BusLine.class);

        conf.addAnnotatedClass(entity.BusRouteManager.class);
        conf.setProperties(pros);
        ServiceRegistry registry=new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
        FACTORY = conf.buildSessionFactory(registry);
    }
    public static SessionFactory getSessionFcatory(){
        return FACTORY;
    }

}
