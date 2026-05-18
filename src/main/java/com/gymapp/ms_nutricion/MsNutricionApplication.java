package com.gymapp.ms_nutricion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

<<<<<<< HEAD

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
=======
@SpringBootApplication
>>>>>>> 02e6e4bee25703bacad01b88f3d59c71fade2b73
@EnableFeignClients
public class MsNutricionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsNutricionApplication.class, args);
	}
}