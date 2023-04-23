package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;


public class ReqresApiTests {
    public static final String BASE_URL = "https://reqres.in/api";

    @Test
    void checkUserInListUsers() {
        given()
                .log().uri()
                .when()
                .get(BASE_URL + "/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("total", is(12))
                .body("data[2].id", is(9))
                .body("data[2].email", is("tobias.funke@reqres.in"))
                .body("data[2].first_name", is("Tobias"))
                .body("data[2].last_name", is("Funke"))
                .body("data[2].avatar", is("https://reqres.in/img/faces/9-image.jpg"))
                .body(matchesJsonSchemaInClasspath("schema/list_users_schema.json"));

    }
    @Test
    void getSingleUser() {
        given()
                .log().uri()
                .when()
                .get(BASE_URL + "/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.id", is(2))
                .body("data.email", is("janet.weaver@reqres.in"));

    }

    @Test
    void createUser() {
        String body = "{ \"name\": \"morpheus\",\"job\": \"leader\" }";

        given()
                .log().uri()
                .body(body)
                .contentType(JSON)
                .when()
                .post(BASE_URL + "/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"));
    }

    @Test
    void updateUser() {
        String body = "{ \"name\": \"morpheus\",\"job\": \"zion resident\" }";

        given()
                .log().uri()
                .body(body)
                .contentType(JSON)
                .when()
                .put(BASE_URL + "/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is("morpheus"))
                .body("job", is("zion resident"));
    }

    @Test
    void deleteUser() {
        given()
                .log().uri()
                .when()
                .delete(BASE_URL + "/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }
}
