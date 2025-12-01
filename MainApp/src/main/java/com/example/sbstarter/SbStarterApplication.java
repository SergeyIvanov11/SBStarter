package com.example.sbstarter;

import com.example.sbstarter.dto.FileEntity;
import com.example.sbstarter.service.FileService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;

@EnableCaching
@EnableScheduling
@SpringBootApplication
public class SbStarterApplication {
    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchFieldException, IllegalAccessException {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        SpringApplication.run(SbStarterApplication.class, args);

        FileService service = new FileService();
        System.out.println("=== Тестируем FileService ===");

        service.saveFile("hello.txt", "hello!".getBytes());
        System.out.println("Файлов после сохранения - " + service.countFiles());
        byte[] loaded = service.getFile("hello.txt");
        System.out.println("Загруженный файл - " + new String(loaded));

        // добавляем устаревший файл
        var field = FileService.class.getDeclaredField("storage");
        field.setAccessible(true);
        Map<String, FileEntity> storage = (Map<String, FileEntity>) field.get(service);

        storage.put("old.txt",
                new FileEntity("OLD".getBytes(), Instant.now().minusSeconds(7200))
        );

        System.out.println("Файлов до очистки - " + service.countFiles());
        service.cleanup();
        System.out.println("Файлов после очистки - " + service.countFiles());

    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("=== Приложение запущено ===");

            // Проверяем DataSource
            DataSource dataSource = ctx.getBean(DataSource.class);
            System.out.println("DataSource бин: " + dataSource.getClass().getSimpleName());

            // Проверяем стартер
            String[] beanNames = ctx.getBeanDefinitionNames();
            String starter = Arrays.stream(beanNames)
                    .filter(n -> n.toLowerCase().contains("mystarter"))
                            .findFirst().get();

            System.out.println("Стартер бин: " + starter);
        };
    }
}
