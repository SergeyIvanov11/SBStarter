package com.example.starter;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;

@Configuration
//@ConditionalOnProperty(prefix = "my.starter", name = "enabled", havingValue = "true")
//@ConditionalOnBean(DataSource.class)
@EnableConfigurationProperties(MyStarterProperties.class)
public class MyStarterAutoConfiguration {

    private final DataSource dataSource;

    public MyStarterAutoConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void printDatabaseTime() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT CURRENT_TIMESTAMP")) {
            System.out.println("Стартер запущен");

            if (rs.next()) {
                String dbTime = rs.getString(1);
                System.out.println("Время из датасорса: " + dbTime);
            }
        } catch (Exception e) {
            System.out.println("Ошибка в MyStarterAutoConfiguration");
            e.printStackTrace();
        }
    }
}
