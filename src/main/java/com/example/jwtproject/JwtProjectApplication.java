package com.example.jwtproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.example.jwtproject.common.ERole;
import com.example.jwtproject.entities.Role;
import com.example.jwtproject.repositories.RoleRepository;

@SpringBootApplication
public class JwtProjectApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext c = SpringApplication.run(JwtProjectApplication.class, args);
		RoleRepository roleRepo = c.getBean(RoleRepository.class);
		Role role = new Role(ERole.ROLE_HERO);
		roleRepo.save(role);
		Role rl = roleRepo.findByName(ERole.ROLE_HERO).get();
		Role rl1 = roleRepo.findByName(ERole.ROLE_ADMIN).get();
		Role rl2 = roleRepo.findByName(ERole.ROLE_MODERATOR).get();
		Role rl3 = roleRepo.findByName(ERole.ROLE_USER).get();
		System.out.println(rl.getName());
		System.out.println(rl1.getName());
		System.out.println(rl2.getName());
		System.out.println(rl3.getName());
	}

}
