package com.urbanmarket.inventoryservice.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@ConfigurationProperties(prefix = "uminv")
@NoArgsConstructor
@Setter
@Getter
public class CommonConfigurations {
    private String errorMessageMappingLocation;
    private String errorStatusMappingLocation;

    public InputStream getErrorMessageMappingLocation() throws IOException{
        return  new ClassPathResource("errorCode_message_mapping.properties").getInputStream();
    }

    public InputStream getErrorStatusMappingLocation() throws IOException{
        return  new ClassPathResource("errorCode_status_mapping.properties").getInputStream();
    }
}
