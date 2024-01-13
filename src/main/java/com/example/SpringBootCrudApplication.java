package com.example;


import com.example.controller.UserController;
import com.example.service.EventService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example")
public class SpringBootCrudApplication {




    public static void main(String[] args) {

        SpringApplication.run(SpringBootCrudApplication.class, args);


    }



}