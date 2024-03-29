package framework.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class ApiUtils {

    public static void setBaseUrl(String baseUrl){
        RestAssured.baseURI = baseUrl;
    }

    public static Response methodGet(String endpoint){
        log.info("Send GET request" + endpoint);
        return RestAssured.get(endpoint);
    }

    public static Response methodGetWithAdditionalPath(String param, Map<String, Object> params, String endpoint){
        log.info("Send GET request with params" + endpoint);
        return RestAssured.given().basePath(endpoint).queryParams(params).get(param);
    }

    public static Response methodPost(ContentType contentType,String endpoint, Object requestBody){
        log.info("Send POST request" + endpoint + requestBody);
        return RestAssured.given()
                .contentType(contentType)
                .body(requestBody)
                .when()
                .post(endpoint);
    }

    public static Response methodPatchWithPathParams(ContentType contentType, Object requestBody, String endpoint){
        log.info("Send PATCH request" + endpoint + requestBody);
        return RestAssured.given()
                .contentType(contentType)
                .basePath(endpoint)
                .body(requestBody)
                .patch();
    }

    public static Response methodDeleteWithPathParamAndBody(ContentType contentType, String param, Object requestBody, String endpoint){
        log.info("Send DELETE request" + endpoint + requestBody);
        return RestAssured.given()
                .contentType(contentType)
                .basePath(endpoint)
                .body(requestBody)
                .delete(param);
    }
}
