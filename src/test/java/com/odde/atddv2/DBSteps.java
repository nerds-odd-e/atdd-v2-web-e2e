package com.odde.atddv2;

import com.github.leeonky.dal.extensions.jdbc.DataBaseBuilder;
import io.cucumber.java.en.When;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;

import static com.github.leeonky.dal.Assertions.expect;

public class DBSteps {
    private final DataBaseBuilder builder = new DataBaseBuilder();

    @Autowired
    private DataSource dataSource;


    @SneakyThrows
    @When("DB should:")
    public void assertDB(String expression) {
        Connection connection = dataSource.getConnection();
        expect(builder.connect(connection)).should(expression);
    }
}
