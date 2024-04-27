package me.deamonet.mi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"me.deamonet.mi", "org.nebula.contrib" })
public class NoteApplication {
    public static void main(String[] args) {
        new SpringApplication(NoteApplication.class).run(args);
    }
}
