package domashka.student;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RestTests {

    private int studentId;

    private CreateStudentRequest generateCreateStudentRequest(){
        List<Integer> marks = new ArrayList<>();
        marks.add(3);
        marks.add(4);
        marks.add(5);
        return new CreateStudentRequest()
                .setName("Anna")
                .setMarks(marks);
    }

    @Test
    @DisplayName("Создание студента без указания Id")
    public void createStudent (){
        CreateStudentRequest createStudentRequest = generateCreateStudentRequest();
        Response response =
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when()
                .body(createStudentRequest)
                .post("http://localhost:8080/student")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .extract().response();

        studentId = response.getBody().as(Integer.class);

        RestAssured.given().log().all()
                .baseUri("http://localhost:8080/student/"+studentId)
                .when().delete()
                .then()
                .statusCode(200);
    }


    @Test
    @DisplayName("Создание студента без имени")
    public void createNamelessStudent (){
        CreateStudentRequest createStudentRequest = generateCreateStudentRequest();
        createStudentRequest.setName(null);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when()
                .body(createStudentRequest)
                .post("http://localhost:8080/student")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Обновление студента")
    public void updateStudent (){
        CreateStudentRequest createStudentRequest = generateCreateStudentRequest();
        Response response =
                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .when()
                        .body(createStudentRequest)
                        .post("http://localhost:8080/student")
                        .then()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().response();
        studentId = response.getBody().as(Integer.class);

        createStudentRequest.setId(String.valueOf(studentId))
                .setMarks(new ArrayList<>(Arrays.asList(3,3,3)));
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when()
                .body(createStudentRequest)
                .post("http://localhost:8080/student")
                .then()
                .statusCode(201);

        RestAssured.given().log().all()
                .baseUri("http://localhost:8080/student/"+studentId)
                .when().get()
                .then().log().body()
                .body("marks", Matchers.equalTo(new ArrayList<>(Arrays.asList(3,3,3))));

        RestAssured.given()
                .baseUri("http://localhost:8080/student/"+studentId)
                .when().delete()
                .then()
                .statusCode(200);

    }
    @Test
    @DisplayName("Получение студента с несуществующим Id")
    public void getStudentFail() {
        RestAssured.given().log().all()
                .baseUri("http://localhost:8080/student/-1")
                .when().get()
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Получение студента")
    public void getStudent() {
        CreateStudentRequest createStudentRequest = generateCreateStudentRequest();
        Response response =
                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .when()
                        .body(createStudentRequest)
                        .post("http://localhost:8080/student")
                        .then()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().response();
        studentId = response.getBody().as(Integer.class);

        RestAssured.given().log().all()
                .baseUri("http://localhost:8080/student/"+studentId)
                .when().get()
                .then().log().body()
                .statusCode(200)
                .extract().body().as(CreateStudentRequest.class);

        RestAssured.given()
                .baseUri("http://localhost:8080/student/"+studentId)
                .when().delete()
                .then()
                .statusCode(200);
    }

    
    @Test
    @DisplayName("Удаление студента")
    public void deleteStudent() {
        CreateStudentRequest createStudentRequest = generateCreateStudentRequest();
        Response response =
                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .when()
                        .body(createStudentRequest)
                        .post("http://localhost:8080/student")
                        .then()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().response();
        studentId = response.getBody().as(Integer.class);

        RestAssured.given().log().all()
                .baseUri("http://localhost:8080/student/"+studentId)
                .when().get()
                .then().log().body()
                .statusCode(200);

        RestAssured.given().log().all()
                .baseUri("http://localhost:8080/student/"+studentId)
                .when().delete()
                .then()
                .statusCode(200);

        RestAssured.given()
                .baseUri("http://localhost:8080/student/"+studentId)
                .when().get()
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Удаление студента с несуществующим ID")
    public void deleteStudentFail(){
        RestAssured.given().log().all()
                .baseUri("http://localhost:8080/student/-1")
                .when().delete()
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Получение пустого списка студентов")
    public void getEmptyArrayOfStudents(){
                RestAssured.given().log().all()
                        .baseUri("http://localhost:8080/topStudent")
                        .when().get()
                        .then().log().body()
                        .statusCode(200)
                        .body(Matchers.emptyString());
    }

    @Test
    @DisplayName("Получение пустого списка студентов без оценок")
    public void getEmptyArrayOfStudentsWithoutMarks(){
        CreateStudentRequest createStudentRequest = generateCreateStudentRequest();
        createStudentRequest.setMarks(null);
        Response response =
                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .when()
                        .body(createStudentRequest)
                        .post("http://localhost:8080/student")
                        .then()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().response();
        studentId = response.getBody().as(Integer.class);

        RestAssured.given().log().all()
                .baseUri("http://localhost:8080/topStudent")
                .when().get()
                .then().log().body()
                .statusCode(200)
                .body(Matchers.emptyString());

        RestAssured.given()
                .baseUri("http://localhost:8080/student/"+studentId)
                .when().delete()
                .then()
                .statusCode(200);

    }

    @Test
    @DisplayName("Получение лучшего студента")
    public void getTopStudent(){
        CreateStudentRequest createStudentRequest = generateCreateStudentRequest();
        createStudentRequest.setMarks(new ArrayList<>(Arrays.asList(3,3,3)));
        Response response =
                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .when()
                        .body(createStudentRequest)
                        .post("http://localhost:8080/student")
                        .then()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().response();
        studentId = response.getBody().as(Integer.class);

        CreateStudentRequest createStudentRequest1 = generateCreateStudentRequest();
        createStudentRequest1.setMarks(new ArrayList<>(Arrays.asList(4,4,4)));
        Response response1 =
                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .when()
                        .body(createStudentRequest1)
                        .post("http://localhost:8080/student")
                        .then()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().response();
        int studentId1 = response1.getBody().as(Integer.class);

        CreateStudentRequest createStudentRequest2 = generateCreateStudentRequest();
        createStudentRequest2.setMarks(new ArrayList<>(Arrays.asList(4,4,4,4)));
        Response response2 =
                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .when()
                        .body(createStudentRequest2)
                        .post("http://localhost:8080/student")
                        .then()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().response();
        int studentId2 = response2.getBody().as(Integer.class);

        RestAssured.given().log().all()
                .baseUri("http://localhost:8080/topStudent")
                .when().get()
                .then().log().body()
                .statusCode(200)
                .body("id", Matchers.equalTo(Arrays.asList(studentId2)));

        RestAssured.given()
                .baseUri("http://localhost:8080/student/"+studentId)
                .when().delete()
                .then()
                .statusCode(200);

        RestAssured.given()
                .baseUri("http://localhost:8080/student/"+studentId1)
                .when().delete()
                .then()
                .statusCode(200);

        RestAssured.given()
                .baseUri("http://localhost:8080/student/"+studentId2)
                .when().delete()
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Получение нескольких лучших студентов")
    public void getTopStudents(){
        CreateStudentRequest createStudentRequest = generateCreateStudentRequest();
        createStudentRequest.setMarks(new ArrayList<>(Arrays.asList(3,3,3)));
        Response response =
                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .when()
                        .body(createStudentRequest)
                        .post("http://localhost:8080/student")
                        .then()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().response();
        studentId = response.getBody().as(Integer.class);

        CreateStudentRequest createStudentRequest1 = generateCreateStudentRequest();
        createStudentRequest1.setMarks(new ArrayList<>(Arrays.asList(4,4,4,4)));
        Response response1 =
                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .when()
                        .body(createStudentRequest1)
                        .post("http://localhost:8080/student")
                        .then()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().response();
        int studentId1 = response1.getBody().as(Integer.class);

        CreateStudentRequest createStudentRequest2 = generateCreateStudentRequest();
        createStudentRequest2.setMarks(new ArrayList<>(Arrays.asList(4,4,4,4)));
        Response response2 =
                RestAssured.given().log().all()
                        .contentType(ContentType.JSON)
                        .when()
                        .body(createStudentRequest2)
                        .post("http://localhost:8080/student")
                        .then()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().response();
        int studentId2 = response2.getBody().as(Integer.class);

        RestAssured.given().log().all()
                .baseUri("http://localhost:8080/topStudent")
                .when().get()
                .then().log().body()
                .statusCode(200)
                .body("id", Matchers.equalTo(Arrays.asList(studentId1, studentId2)));

        RestAssured.given()
                .baseUri("http://localhost:8080/student/"+studentId)
                .when().delete()
                .then()
                .statusCode(200);

        RestAssured.given()
                .baseUri("http://localhost:8080/student/"+studentId1)
                .when().delete()
                .then()
                .statusCode(200);

        RestAssured.given()
                .baseUri("http://localhost:8080/student/"+studentId2)
                .when().delete()
                .then()
                .statusCode(200);
    }
}
