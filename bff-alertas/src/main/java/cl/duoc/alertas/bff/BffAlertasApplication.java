package cl.duoc.alertas.bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BffAlertasApplication {
    public static void main(String[] args) {
        SpringApplication.run(BffAlertasApplication.class, args);
    }
}
