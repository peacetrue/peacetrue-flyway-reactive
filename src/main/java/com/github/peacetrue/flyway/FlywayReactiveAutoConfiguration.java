package com.github.peacetrue.flyway;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author peace
 * @see org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
 * @see org.springframework.boot.autoconfigure.flyway.FlywayProperties
 * @since 1.0
 **/
@EnableConfigurationProperties(FlywayProperties.class)
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Flyway.class)
@ConditionalOnProperty(prefix = "spring.flyway", name = "enabled", matchIfMissing = true)
public class FlywayReactiveAutoConfiguration {

    @Bean(initMethod = "migrate")
    public Flyway flyway(FlywayProperties properties) {
        FluentConfiguration configuration = Flyway.configure()
                .dataSource(properties.getUrl(), properties.getUser(), properties.getPassword());
        configureProperties(configuration, properties);
        return new Flyway(configuration);
    }


    /** @see FlywayAutoConfiguration.FlywayConfiguration#configureProperties(FluentConfiguration, FlywayProperties) */
    private void configureProperties(FluentConfiguration configuration, FlywayProperties properties) {
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
//        String[] locations = new FlywayAutoConfiguration.LocationResolver(configuration.getDataSource())
//                .resolveLocations(properties.getLocations()).toArray(new String[0]);
//        map.from(locations).to(configuration::locations);
        map.from(properties.getLocations())
                .to((locations) -> configuration.locations(locations.toArray(new String[0])));
        map.from(properties.getEncoding()).to(configuration::encoding);
        map.from(properties.getConnectRetries()).to(configuration::connectRetries);
        // No method reference for compatibility with Flyway 6.x
        map.from(properties.getLockRetryCount())
                .to((lockRetryCount) -> configuration.lockRetryCount(lockRetryCount));
        // No method reference for compatibility with Flyway 5.x
        map.from(properties.getDefaultSchema()).to((schema) -> configuration.defaultSchema(schema));
        map.from(properties.getSchemas()).as(StringUtils::toStringArray).to(configuration::schemas);
        configureCreateSchemas(configuration, properties.isCreateSchemas());
        map.from(properties.getTable()).to(configuration::table);
        // No method reference for compatibility with Flyway 5.x
        map.from(properties.getTablespace()).whenNonNull().to((tablespace) -> configuration.tablespace(tablespace));
        map.from(properties.getBaselineDescription()).to(configuration::baselineDescription);
        map.from(properties.getBaselineVersion()).to(configuration::baselineVersion);
        map.from(properties.getInstalledBy()).to(configuration::installedBy);
        map.from(properties.getPlaceholders()).to(configuration::placeholders);
        map.from(properties.getPlaceholderPrefix()).to(configuration::placeholderPrefix);
        map.from(properties.getPlaceholderSuffix()).to(configuration::placeholderSuffix);
        map.from(properties.isPlaceholderReplacement()).to(configuration::placeholderReplacement);
        map.from(properties.getSqlMigrationPrefix()).to(configuration::sqlMigrationPrefix);
        map.from(properties.getSqlMigrationSuffixes()).as(StringUtils::toStringArray)
                .to(configuration::sqlMigrationSuffixes);
        map.from(properties.getSqlMigrationSeparator()).to(configuration::sqlMigrationSeparator);
        map.from(properties.getRepeatableSqlMigrationPrefix()).to(configuration::repeatableSqlMigrationPrefix);
        map.from(properties.getTarget()).to(configuration::target);
        map.from(properties.isBaselineOnMigrate()).to(configuration::baselineOnMigrate);
        map.from(properties.isCleanDisabled()).to(configuration::cleanDisabled);
        map.from(properties.isCleanOnValidationError()).to(configuration::cleanOnValidationError);
        map.from(properties.isGroup()).to(configuration::group);
        map.from(properties.isIgnoreMissingMigrations()).to(configuration::ignoreMissingMigrations);
        map.from(properties.isIgnoreIgnoredMigrations()).to(configuration::ignoreIgnoredMigrations);
        map.from(properties.isIgnorePendingMigrations()).to(configuration::ignorePendingMigrations);
        map.from(properties.isIgnoreFutureMigrations()).to(configuration::ignoreFutureMigrations);
        map.from(properties.isMixed()).to(configuration::mixed);
        map.from(properties.isOutOfOrder()).to(configuration::outOfOrder);
        map.from(properties.isSkipDefaultCallbacks()).to(configuration::skipDefaultCallbacks);
        map.from(properties.isSkipDefaultResolvers()).to(configuration::skipDefaultResolvers);
        configureValidateMigrationNaming(configuration, properties.isValidateMigrationNaming());
        map.from(properties.isValidateOnMigrate()).to(configuration::validateOnMigrate);
        map.from(properties.getInitSqls()).whenNot(CollectionUtils::isEmpty)
                .as((initSqls) -> StringUtils.collectionToDelimitedString(initSqls, "\n"))
                .to(configuration::initSql);
        // Pro properties
        map.from(properties.getBatch()).whenNonNull().to(configuration::batch);
        map.from(properties.getDryRunOutput()).whenNonNull().to(configuration::dryRunOutput);
        map.from(properties.getErrorOverrides()).whenNonNull().to(configuration::errorOverrides);
        map.from(properties.getLicenseKey()).whenNonNull().to(configuration::licenseKey);
        map.from(properties.getOracleSqlplus()).whenNonNull().to(configuration::oracleSqlplus);
        // No method reference for compatibility with Flyway 5.x
        map.from(properties.getOracleSqlplusWarn()).whenNonNull()
                .to((oracleSqlplusWarn) -> configuration.oracleSqlplusWarn(oracleSqlplusWarn));
        map.from(properties.getStream()).whenNonNull().to(configuration::stream);
        map.from(properties.getUndoSqlMigrationPrefix()).whenNonNull().to(configuration::undoSqlMigrationPrefix);
        // No method reference for compatibility with Flyway 6.x
        map.from(properties.getCherryPick()).whenNonNull().to((cherryPick) -> configuration.cherryPick(cherryPick));
        // No method reference for compatibility with Flyway 6.x
        map.from(properties.getJdbcProperties()).whenNot(Map::isEmpty)
                .to((jdbcProperties) -> configuration.jdbcProperties(jdbcProperties));
        // No method reference for compatibility with Flyway 6.x
        map.from(properties.getOracleKerberosCacheFile()).whenNonNull()
                .to((cacheFile) -> configuration.orackeKerberosCacheFile(cacheFile));
        // No method reference for compatibility with Flyway 6.x
        map.from(properties.getOracleKerberosConfigFile()).whenNonNull()
                .to((configFile) -> configuration.orackeKerberosConfigFile(configFile));
        // No method reference for compatibility with Flyway 6.x
        map.from(properties.getOutputQueryResults()).whenNonNull()
                .to((outputQueryResults) -> configuration.outputQueryResults(outputQueryResults));
        // No method reference for compatibility with Flyway 6.x
        map.from(properties.getSkipExecutingMigrations()).whenNonNull()
                .to((skipExecutingMigrations) -> configuration.skipExecutingMigrations(skipExecutingMigrations));
    }

    private void configureCreateSchemas(FluentConfiguration configuration, boolean createSchemas) {
        try {
            configuration.createSchemas(createSchemas);
        } catch (NoSuchMethodError ex) {
            // Flyway < 6.5
        }
    }

    private void configureValidateMigrationNaming(FluentConfiguration configuration,
                                                  boolean validateMigrationNaming) {
        try {
            configuration.validateMigrationNaming(validateMigrationNaming);
        } catch (NoSuchMethodError ex) {
            // Flyway < 6.2
        }
    }

}
