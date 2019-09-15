package com.example.client;

import com.example.services.GameException;
import com.example.services.IGameEngine;
import javafx.util.Pair;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Configuration
public class HessianClient {

    @Bean
    public HessianProxyFactoryBean hessianInvoker() {
        HessianProxyFactoryBean invoker = new HessianProxyFactoryBean();
        invoker.setServiceUrl("http://localhost:8080/guess");
        invoker.setServiceInterface(IGameEngine.class);
        return invoker;
    }

    public static void main(String[] args) throws GameException {
        IGameEngine service = SpringApplication.run(HessianClient.class, args).getBean(IGameEngine.class);
        try {
            System.out.println("Welcome in guess the number game.");
            processMainMenu(service);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private static void processMainMenu(IGameEngine service) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean play = true;
        while(play) {
            System.out.println("Please choose an option: \n1. Play (play)\n2. Exit (exit)");
            String choose = br.readLine();
            switch (choose) {
                case "play":
                    play = true;
                    System.out.println("Please choose difficulty: \n(1 - EASY)\n(2 - MEDIUM)\n(3 - HARD)");
                    String in = br.readLine();
                    Integer difficulty = Integer.parseInt(in);
                    if (!validateDifficulty(difficulty)) {
                        System.out.println("No such difficulty");
                        break;
                    }
                    System.out.println("Please insert number: ");

                    innerMenu(service, difficulty);
                    break;
                case "exit":
                    System.out.println("Thank you for playing, see you next time.");
                    try {
                        service.exit();
                    }
                    catch(Exception ex) {
                        // let the program sneak through the exception, otherwise it will printout the stacktrace
                    }
                    System.exit(0);
                default:
                    play = true;
                    System.out.println("no such option, please type in once more.\nOptions: play|exit");
                    break;
            }
        }
    }

    private static void innerMenu(IGameEngine service, Integer difficulty) throws IOException {
        boolean restart = false;
        while (!restart) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String in = br.readLine();
            Integer number;
            if (!validateNumber(in)) {
                System.out.println("Provided string is not a number");
                break;
            }
            number = Integer.parseInt(in);
            Pair<String, Integer> result = service.guessNumber(number, difficulty);

            if (result.getValue().equals(0)) {
                System.out.println("You lost, I'm sorry...");
                restart = true;
            } else {
                if (service.hasPlayerWin()) {
                    System.out.println(result.getKey());
                    restart = true;
                } else {
                    System.out.println(result.getKey() + " you have " + result.getValue() + " retries");
                    restart = false;
                }
            }
        }
    }
    private static boolean validateNumber(String in) {
        boolean valid = false;
        for (char c: in.toCharArray()) {
            if(c >= '0' && c <= '9') {
                valid = true;
            }
            else {
                return false;
            }
        }

        return valid;
    }

    private static boolean validateDifficulty(Integer difficulty) {
        if((difficulty >= 1) && (difficulty <= 3)) {
            return true;
        }
        return false;
    }


}
