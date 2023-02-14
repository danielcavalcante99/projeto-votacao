package br.com.microservicovotacao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class MicroservicoVotacaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicoVotacaoApplication.class, args);
	}

}
