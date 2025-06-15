package com.jorge.projeto.tarefas.tarefas_java_jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EntityScan(basePackages = "com.jorge.projeto.tarefas.tarefas_java_jwt")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

		//Gerando uma senha para ter um usuário admin no banco para poder cadastrar novos admins:
		System.out.println("Senha do admin gerada:");
		System.out.println(new BCryptPasswordEncoder().encode("Projeto123*"));

	}

}
