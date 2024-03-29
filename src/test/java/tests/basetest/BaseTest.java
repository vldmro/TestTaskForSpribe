package tests.basetest;

import framework.configuration.ConfigManager;
import framework.utils.ApiUtils;
import framework.utils.CleanUtils;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    @BeforeClass
    protected void setUpApiLogging(){
        RestAssured.filters(new RequestLoggingFilter(LogDetail.URI), new ResponseLoggingFilter(LogDetail.BODY),
                new ResponseLoggingFilter(LogDetail.HEADERS));
    }

    @BeforeMethod
    protected void setUp(){
        ApiUtils.setBaseUrl(ConfigManager.getConfigValue().getBaseApiUrl());
    }

    @AfterMethod
    protected void tearDown(){
        CleanUtils.cleanData();
    }
}
