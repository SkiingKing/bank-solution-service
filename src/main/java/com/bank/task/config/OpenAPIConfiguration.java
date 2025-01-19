package com.bank.task.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("Volodymyr Shvets");

        Info information = new Info()
                .title("Banking Solution")
                .version("1.0")
                .description("This API should allow users to perform basic banking operations such as creating accounts,\n" +
                        "making deposits, and transferring funds.")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }

}
