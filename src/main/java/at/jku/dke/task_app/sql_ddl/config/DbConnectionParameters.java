package at.jku.dke.task_app.sql_ddl.config;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;


/**
 * The type Db connection parameters.
 */
@Validated
@ConfigurationProperties(prefix = "jdbc")
public record DbConnectionParameters(
    @NotNull String url,
    @NotNull UserCredentials admin,
    @NotNull @Positive int maxPoolSize,
    @NotNull @Positive long maxLifetime,
    @NotNull @Positive long connectionTimeout,
    @NotNull String driverClassName) {
    /**
     * The user credentials.
     *
     * @param username The username.
     * @param password The password.
     */
    public record UserCredentials(@NotNull String username, @NotNull String password) {
    }
}
