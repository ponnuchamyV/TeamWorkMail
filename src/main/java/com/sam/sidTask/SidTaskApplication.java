package com.sam.sidTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan("com.sam.sidTask.servlets")
public class SidTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(SidTaskApplication.class, args);
	}

}
