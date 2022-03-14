package org.tcgms.network.player.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@ConfigurationProperties( prefix = "application.storage" )
@Configuration
public class FileStorageProperties
{
    private String dir;

    public String getDir()
    {
        return dir;
    }

    public void setDir(String dir)
    {
        this.dir = dir;
    }
}
