package ru.effectivemobile.taskManager.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import ru.effectivemobile.taskManager.model.dto.task.TaskRequestDto;
import ru.effectivemobile.taskManager.model.dto.task.TaskUpdateDto;
import ru.effectivemobile.taskManager.model.enums.task.TaskPriority;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class TaskRestControllerIntegrationTest extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    private String jwtToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

//        jwtToken = JwtTokenUtil.generateToken("testuser", "ROLE_USER");
    }

    @Test
    void testCreateTask() {
        TaskRequestDto taskRequest = TaskRequestDto.builder()
                .title("Test Task")
                .description("Test Description")
                .assigneeId(1L)
                .priority(TaskPriority.LOW)
                .build();

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .body(taskRequest)
                .when()
                .post("/api/v1/tasks")
                .then()
                .statusCode(200)
                .body("title", equalTo("Test Task"))
                .body("description", equalTo("Test Description"));
    }

    @Test
    void testGetTaskById() {
        TaskRequestDto taskRequest = TaskRequestDto.builder()
                .title("Test Task")
                .description("Test Description")
                .assigneeId(1L)
                .priority(TaskPriority.LOW)
                .build();
        long taskId = given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .body(taskRequest)
                .when()
                .post("/api/v1/tasks")
                .then()
                .extract()
                .path("id");

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get("/api/v1/tasks/" + taskId)
                .then()
                .statusCode(200)
                .body("id", equalTo((int) taskId))
                .body("title", equalTo("Test Task"));
    }

    @Test
    void testUpdateTask() {
        TaskRequestDto taskRequest = TaskRequestDto.builder()
                .title("Test Task")
                .description("Test Description")
                .assigneeId(1L)
                .priority(TaskPriority.LOW)
                .build();
        long taskId = given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .body(taskRequest)
                .when()
                .post("/api/v1/tasks")
                .then()
                .extract()
                .path("id");

        TaskUpdateDto updateRequest = TaskUpdateDto.builder()
                .title("Test Task")
                .description("Test Description")
                .assigneeId(1L)
                .priority(TaskPriority.LOW)
                .build();

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .patch("/api/v1/tasks/" + taskId)
                .then()
                .statusCode(200)
                .body("title", equalTo("Updated Task"))
                .body("description", equalTo("Updated Description"));
    }

    @Test
    void testDeleteTask() {
        TaskRequestDto taskRequest = TaskRequestDto.builder()
                .title("Test Task")
                .description("Test Description")
                .assigneeId(1L)
                .priority(TaskPriority.LOW)
                .build();
        long taskId = given()
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .body(taskRequest)
                .when()
                .post("/api/v1/tasks")
                .then()
                .extract()
                .path("id");

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .delete("/api/v1/tasks/" + taskId)
                .then()
                .statusCode(204);

        given()
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get("/api/v1/tasks/" + taskId)
                .then()
                .statusCode(404);
    }
}
