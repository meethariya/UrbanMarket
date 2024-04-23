package com.urbanmarket.inventoryservice.component;

import com.urbanmarket.inventoryservice.configuration.CommonConfigurations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
@Slf4j
public class ErrorPropertyReader {
    private static final int INTERNAL_SERVER_ERROR_CODE = 500;
    @Autowired
    private CommonConfigurations commonConfigurations;

    private Map<String, String> errorCodeMessageMap = new HashMap<>();
    private Map<String, String> errorCodeStatusMap = new HashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    public void readErrorMap(){
        log.info("Loading error code mappings properties from the resources");
        try{
            InputStream messageInputStream = commonConfigurations.getErrorMessageMappingLocation();
            Properties errorMessageProperties = new Properties();
            errorMessageProperties.load(messageInputStream);
            errorMessageProperties.entrySet().forEach(objectObjectEntry -> errorCodeMessageMap.put(objectObjectEntry.getKey().toString(),objectObjectEntry.getValue().toString()));

            InputStream codeInputStream = commonConfigurations.getErrorStatusMappingLocation();
            Properties errorCodeProperties = new Properties();
            errorCodeProperties.load(codeInputStream);
            errorCodeProperties.entrySet().forEach(objectObjectEntry -> errorCodeStatusMap.put(objectObjectEntry.getKey().toString(),objectObjectEntry.getValue().toString()));

        } catch (IOException e) {
            log.error("Could not load error mapping related data.",e);
            throw new IllegalStateException("Could not load error mapping related data.",e);
        }
        log.info("Error code map loading completed. Number of loaded error-message mappings: {}, Number of loaded error-status mappings: {}",errorCodeMessageMap.size(),errorCodeStatusMap.size());
    }

    public String getErrorCodeMessageValue(String errorCode){
        if(errorCodeMessageMap.isEmpty()){
            return null;
        }
        if(errorCodeMessageMap.get(errorCode) == null){
            return null;
        }
        return errorCodeMessageMap.get(errorCode);
    }

    public Integer getErrorStatusValue(String errorCode){
        if(errorCodeMessageMap.isEmpty()){
            return INTERNAL_SERVER_ERROR_CODE;
        }
        if(errorCodeStatusMap.get(errorCode) == null){
            return INTERNAL_SERVER_ERROR_CODE;
        }
        return Integer.parseInt(errorCodeStatusMap.get(errorCode));
    }
}
