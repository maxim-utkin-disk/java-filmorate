package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FilmorateApplication {

	private static ConfigurableApplicationContext appContext;

	public static void main(String[] args) {
		appContext = SpringApplication.run(FilmorateApplication.class, args);
	}

	public static void stopServer() {
		SpringApplication.exit(appContext);
	}

}
