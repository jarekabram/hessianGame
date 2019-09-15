package com.example.server;

import com.example.services.IGameEngine;
import com.example.services.GameEngineImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.remoting.support.RemoteExporter;

@Configuration @ComponentScan @EnableAutoConfiguration public class Server {

    @Bean
    IGameEngine bookingService() {
        return new GameEngineImpl();
    }

    @Bean(name = "/guess") RemoteExporter hessianService(IGameEngine service) {
        HessianServiceExporter exporter = new HessianServiceExporter();
        exporter.setService(bookingService());
        exporter.setServiceInterface(IGameEngine.class);
        return exporter;
    }

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }

}