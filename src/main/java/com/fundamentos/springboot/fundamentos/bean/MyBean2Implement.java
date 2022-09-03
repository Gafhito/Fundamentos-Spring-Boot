package com.fundamentos.springboot.fundamentos.bean;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

public class MyBean2Implement implements MyBean {
    Log LOGGER = LogFactory.getLog(MyBean2Implement.class);
    @Override
    public void print() {
        LOGGER.info("Implementación propia del bean número 2");
        System.out.println("Hola desde mi implementación 2 propia del bean 2");
    }
}
