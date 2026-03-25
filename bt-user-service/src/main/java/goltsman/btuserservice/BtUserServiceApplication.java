package goltsman.btuserservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BtUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BtUserServiceApplication.class, args);
    }

}
