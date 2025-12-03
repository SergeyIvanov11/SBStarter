package com.example.starter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

class MyStarterAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    MyStarterAutoConfiguration.class,
                    DataSourceAutoConfiguration.class
            ));

    @Test
    void starterEnabledWithDataSource() {
        contextRunner
                .withPropertyValues(
                        "my.starter.enabled=true",
                        "spring.datasource.url=jdbc:h2:mem:testdb",
                        "spring.datasource.driver-class-name=org.h2.Driver",
                        "spring.datasource.username=sa",
                        "spring.datasource.password="
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(MyStarterAutoConfiguration.class);
                    assertThat(context).hasSingleBean(DataSource.class);
                    System.out.println("Стартер создан при наличии DataSource");
                });
    }

    @Test
    void starterDisabled() {
        contextRunner
                .withPropertyValues(
                        "my.starter.enabled=false"
                )
                .run(context -> {
                    assertThat(context).doesNotHaveBean(MyStarterAutoConfiguration.class);
                    assertThat(context).hasSingleBean(DataSource.class);
                    System.out.println("Стартер не создан при my.starter.enabled=false");
                });
    }
}