package com.novus.api_gateway.configuration;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DateConfiguration {

    public Date newDate() {
        return new Date(System.currentTimeMillis() + 7200000);
    }

}