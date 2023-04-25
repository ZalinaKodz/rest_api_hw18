package tests;

import org.junit.jupiter.api.Test;
import tests.models.UserData;
import tests.models.UserSupport;


import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static tests.Specs.*;


public class ReqresApiTests {

    @Test
    void checkUserFromListGroovy() {
        given()
                .spec(Specs.request)
                .when()
                .get("/users?page=2")
                .then()
                .spec(responseSpec)
                .log().body()
                .body("data.findAll{it.id =~/./}.id.flatten()",
                        hasItem(9));
    }

    @Test
    void checkTextInUserList() {
        UserSupport support = given()
                .spec(request)
                .when()
                .get("/users?page=2")
                .then()
                .spec(responseSpec)
                .log().body()
                .extract().as(UserSupport.class);
        assertEquals("To keep ReqRes free, contributions towards server costs are appreciated!", support.getSupport().getText());
    }

    @Test
    void checkSingleUser() {
        UserData data = given()
                .spec(request)
                .when()
                .get("/users/2")
                .then()
                .spec(responseSpec)
                .log().body()
                .extract().as(UserData.class);
        assertEquals(2, data.getUser().getId());
        assertEquals("janet.weaver@reqres.in", data.getUser().getEmail());
    }

    @Test
    void createUser() {
        String body = "{ \"name\": \"morpheus\",\"job\": \"leader\" }";

        given()
                .spec(request)
                .body(body)
                .when()
                .post("/users")
                .then()
                .spec(responseCreateUser)
                .log().body()
                .body("name", is("morpheus"))
                .body("job", is("leader"));
    }

    @Test
    void updateUser() {
        String body = "{ \"name\": \"morpheus\",\"job\": \"zion resident\" }";

        given()
                .spec(request)
                .body(body)
                .when()
                .put("/users/2")
                .then()
                .spec(responseSpec)
                .log().body()
                .body("name", is("morpheus"))
                .body("job", is("zion resident"));
    }

    @Test
    void deleteUser() {
        given()
                .spec(request)
                .log().uri()
                .when()
                .delete("/users/2")
                .then()
                .spec(responseDeleteUser);
    }
}
