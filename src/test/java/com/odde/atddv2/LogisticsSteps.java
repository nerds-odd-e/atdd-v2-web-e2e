package com.odde.atddv2;

import com.github.leeonky.cucumber.restful.RestfulStep;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class LogisticsSteps {
    @Autowired
    private RestfulStep restfulStep;

    @Before("@logistics-api")
    public void setBaseUrl() {
        restfulStep.setBaseUrl("http://127.0.0.1:10083/api/");
    }
}
