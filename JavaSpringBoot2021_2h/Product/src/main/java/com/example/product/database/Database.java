package com.example.product.database;

import com.example.product.models.Product;
import com.example.product.repositories.ProductRepositoryDAO;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;



@Configuration //annotation bên trong chứa các bean method, sẽ được gọi ngay khi chạy project
public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class); // Thay cho đối tượng system.out.println
    @Bean
    CommandLineRunner initDatabase(ProductRepositoryDAO productRepositoryDAO){
        // CommandLineRunner : interface
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Product productA = new Product("Macbook",2022,3900.0,"");
                Product productB = new Product("Ipad",2023,2400.0,"");
                logger.info("insert data: " + productRepositoryDAO.save(productA));
                logger.info("insert data: " + productRepositoryDAO.save(productB));
            }
        };
    }

}
