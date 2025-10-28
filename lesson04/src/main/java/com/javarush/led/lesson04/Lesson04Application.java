package com.javarush.led.lesson04;

import com.javarush.led.lesson04.entity.User;
import com.javarush.led.lesson04.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Lesson04Application {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Lesson04Application.class, args);
		UserService userService = context.getBean(UserService.class);
		User[] users={
				new User(1L,"admin","pass","ADMIN"),
				new User(3L,"guest","asda","GUEST"),
				new User(2L,"user","qweqwe","USER"),
		};
		userService.save(users);
		for (Long i = 1L; i <= 3; i++) {
			User user = userService.getById(i);
			System.err.println(user+"\n");
		}}

}
