package com.objectmentor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ObjectmentorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ObjectmentorApplication.class, args);

        try {
            Args arg = new Args("l,p#,d*", args);
            boolean logging = arg.getBoolean('l');
            int port = arg.getInt('p');
            String directory = arg.getString('d');
            executeApplication(logging, port, directory);
        } catch (Exception e) {
            System.out.print("Argument error: %s\n", e.errorMessage());
        }
    }

}
