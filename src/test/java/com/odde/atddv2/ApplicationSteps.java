package com.odde.atddv2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.leeonky.cucumber.restful.RestfulStep;
import com.github.leeonky.jfactory.JFactory;
import com.github.leeonky.jfactory.cucumber.JData;
import com.odde.atddv2.entity.Order;
import com.odde.atddv2.entity.User;
import com.odde.atddv2.page.HomePage;
import com.odde.atddv2.page.OrderPage;
import com.odde.atddv2.page.WelcomePage;
import com.odde.atddv2.repo.OrderRepo;
import com.odde.atddv2.repo.UserRepo;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.zh_cn.假如;
import io.cucumber.java.zh_cn.当;
import io.cucumber.java.zh_cn.那么;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.test.context.ContextConfiguration;

import static org.awaitility.Awaitility.await;

@ContextConfiguration(classes = {CucumberConfiguration.class}, loader = SpringBootContextLoader.class)
@CucumberContextConfiguration
public class ApplicationSteps {

    @Autowired
    JFactory jFactory;
    @Autowired
    JData jData;
    @Autowired
    private HomePage homePage;
    @Autowired
    private WelcomePage welcomePage;
    @Autowired
    private Browser browser;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private OrderPage orderPage;

    @Autowired
    private SeleniumVideoApi seleniumVideoApi;

    @假如("存在用户名为{string}和密码为{string}的用户")
    public void 存在用户名为和密码为的用户(String userName, String password) {
        userRepo.save(new User().setUserName(userName).setPassword(password));
    }

    @当("以用户名为{string}和密码为{string}登录时")
    public void 以用户名为和密码为登录时(String userName, String password) {
        homePage.open();
        homePage.login(userName, password);
    }

    @那么("{string}登录成功")
    public void 登录成功(String userName) {
        browser.shouldHaveText("Welcome " + userName);
    }

    @那么("登录失败的错误信息是{string}")
    public void 登录失败的错误信息是(String message) {
        browser.shouldHaveText(message);
    }

    @Before(order = 1)
    public void clearDB() {
        userRepo.deleteAll();
        orderRepo.deleteAll();
        jFactory.getDataRepository().clear();
    }

    @假如("存在如下订单:")
    public void 存在如下订单(DataTable table) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        table.asMaps().forEach(map -> orderRepo.save(objectMapper.convertValue(map, Order.class)));
    }

    @当("查询订单时")
    public void 查询订单时() {
        welcomePage.goToOrders();
    }

    @那么("显示如下订单")
    public void 显示如下订单(DataTable table) {
        table.asList().forEach(browser::shouldHaveText);
    }

    @Before("@ui-login")
    public void uiLogin() {
        存在用户名为和密码为的用户("j", "j");
        以用户名为和密码为登录时("j", "j");
        登录成功("j");
    }

    @Before(value = "@ui-login", order = 1)
    public void startRecording(Scenario scenario) {
        seleniumVideoApi.startRecording(scenario.getName());
    }

    @After(value = "@ui-login")
    public void endRecording() {
        seleniumVideoApi.endRecording();
    }

    @当("用如下数据录入订单:")
    public void 用如下数据录入订单(DataTable table) {
        查询订单时();
        orderPage.addOrder(table.asMaps().get(0));
    }

    @那么("{string}最终应为:")
    public void 最终应为(String queryExpression, String dalExpression) {
        await().ignoreExceptions().untilAsserted(() -> {
            jData.should(queryExpression, dalExpression);
        });
    }

    @Autowired
    private RestfulStep restfulStep;

    @Before(order = 1)
    public void setBaseUrl() {
        restfulStep.setBaseUrl("http://127.0.0.1:10081/api/");
    }
}
