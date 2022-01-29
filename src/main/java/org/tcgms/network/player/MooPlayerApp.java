package org.tcgms.network.player;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class MooPlayerApp
{
    public static void main( String [] args )
    {
        SpringApplication.run( MooPlayerApp.class, args);
    }


}

