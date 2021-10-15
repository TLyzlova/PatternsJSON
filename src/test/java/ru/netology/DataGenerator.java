package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.util.Locale;
import static io.restassured.RestAssured.given;

public class DataGenerator {
    private static Faker faker = new Faker(new Locale("en"));

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private DataGenerator() {
    }

    static void sendRequest(LogInfo logInfo) {
        given()
                .spec(requestSpec)
                .body(logInfo)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static class Registration {
        private Registration() {
        }

        public static String generateLogin() {
            return faker.name().username();
        }

        public static String generatePassword() {
            return faker.internet().password();
        }

        public static LogInfo generateActiveUser() {
            LogInfo logInfo = new LogInfo(generateLogin(), generatePassword(), "active");
            sendRequest(logInfo);
            return logInfo;
        }

        public static LogInfo generateBlockedUser() {
           LogInfo logInfo = new LogInfo(generateLogin(), generatePassword(), "blocked");
            sendRequest(logInfo);
            return logInfo;
        }

        public static LogInfo generateWrongPassword(String status) {
            String login = generateLogin();
            sendRequest(new LogInfo(login, generatePassword(), status));
            return new LogInfo(login, generatePassword(), status);
        }

        public static LogInfo generateWrongLogin(String status) {
            String password = generatePassword();
            sendRequest(new LogInfo(generateLogin(), password, status));
            return new LogInfo(generateLogin(), password, status);
        }
    }
}