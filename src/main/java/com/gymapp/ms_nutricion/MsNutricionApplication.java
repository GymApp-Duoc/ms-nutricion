package com.gymapp.ms_nutricion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // Habilita la comunicación síncrona con ms-miembros, gamificación y notificaciones
public class MsNutricionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsNutricionApplication.class, args);
	}
}