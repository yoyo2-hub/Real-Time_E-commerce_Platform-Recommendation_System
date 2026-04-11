package com.example.reco_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
@SpringBootApplication
@EnableKafka

public class RecoEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecoEngineApplication.class, args);
	}

}
