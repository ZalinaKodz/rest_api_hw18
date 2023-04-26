package tests;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.models.LoginBodyModel;
import tests.models.UserBodyResponse;
import tests.models.UserData;
import tests.models.UserSupport;


import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static tests.Specs.*;


public class ReqresApiTests {

    @Test
    @DisplayName("Проверка, что в списке есть пользователь с определенным id")
    void checkUserFromListGroovy() {
        step("Make request", () ->
        given()
                .filter(withCustomTemplates())
                .spec(Specs.request)
                .when()
                .get("/users?page=2")
                .then()
                .spec(responseSpec)
                .log().body()
                .body("data.findAll{it.id =~/./}.id.flatten()",
                        hasItem(9)));
    }

    @Test
    @DisplayName("Проверка на соответствие ожидаемому тексту в ответе списка пользователей")
    void checkTextInUserList() {
        UserSupport support = step("Make request", () ->
                 given(request)
                .filter(withCustomTemplates())
                .spec(request)
                .when()
                .get("/users?page=2")
                .then()
                .spec(responseSpec)
                .extract().as(UserSupport.class));

        step("Verify text in response", () ->
        assertEquals("To keep ReqRes free, contributions towards server costs are appreciated!", support.getSupport().getText()));
    }

    @Test
    @DisplayName("Получение данных пользователя")
    void checkSingleUser() {
        UserData data = step("Make request", () ->
                given(request)
                .filter(withCustomTemplates())
                .spec(request)
                .when()
                .get("/users/2")
                .then()
                .spec(responseSpec)
                .extract().as(UserData.class));

        step("Verify  user`s id", () ->
        assertEquals(2, data.getUser().getId()));

        step("Verify  user`s email", () ->
        assertEquals("janet.weaver@reqres.in", data.getUser().getEmail()));
    }

    @Test
    @DisplayName("Создание пользователя")
    void createUser() {

        LoginBodyModel data = new LoginBodyModel();
        data.setName("morpheus");
        data.setJob("leader");

        UserBodyResponse user = step("Make post request", () ->
                given(request)
                .filter(withCustomTemplates())
                .spec(request)
                .body(data)
                .when()
                .post("/users")
                .then()
                .spec(responseCreateUser)
                .extract().as(UserBodyResponse.class));

        step("Verify  user`s name", () ->
               assertThat(user.getName()).isEqualTo("morpheus"));

        step("Verify user`s job", () ->
               assertThat(user.getJob()).isEqualTo("leader"));
    }

    @Test
    @DisplayName("Изменение данных пользователя")
    void updateUser() {

        LoginBodyModel data = new LoginBodyModel();
        data.setName("morpheus");
        data.setJob("zion resident");

        UserBodyResponse user = step("Make put request", () ->
                given(request)
                .filter(withCustomTemplates())
                .spec(request)
                .body(data)
                .when()
                .put("/users/2")
                .then()
                .spec(responseSpec)
                .extract().as(UserBodyResponse.class));

        step("Verify  user`s name", () ->
        assertThat(user.getName()).isEqualTo("morpheus"));
        step("Verify  user`s name", () ->
        assertThat(user.getJob()).isEqualTo("zion resident"));

    }

    @Test
    @DisplayName("Удаления  пользователя")
    void deleteUser() {

        step("Make delete request", () ->
        given(request)
                .filter(withCustomTemplates())
                .spec(request)
                .log().uri()
                .when()
                .delete("/users/2")
                .then()
                .spec(responseDeleteUser));
    }
}
