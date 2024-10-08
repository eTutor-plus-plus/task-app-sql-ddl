package at.jku.dke.task_app.sql_ddl;

import at.jku.dke.etutor.task_app.AppHelper;
import at.jku.dke.task_app.sql_ddl.config.DbConnectionParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.core.env.Environment;

/**
 * The main class of the application.
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class})
@ConfigurationPropertiesScan(basePackageClasses = DbConnectionParameters.class)
public class TaskAppApplication {

    private static final Logger LOG = LoggerFactory.getLogger(TaskAppApplication.class);

    /**
     * The entry point of the application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        var app = new SpringApplication(TaskAppApplication.class);
        Environment env = app.run(args).getEnvironment();
        AppHelper.logApplicationStartup(LOG, env);
    }

    /**
     * Creates a new instance of class {@link TaskAppApplication}.
     */
    public TaskAppApplication() {
    }

}
