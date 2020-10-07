package com.sales.purchaseitem;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * entry point of the application
 */

@SpringBootApplication(scanBasePackages = {"com.sales.purchaseitem"})
public class RunSpringBootSalesApplication {

    public static void main(String args[]) {
        SpringApplication.run(RunSpringBootSalesApplication.class, args);
    }
}
