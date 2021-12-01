package io.challenge.lightfeather.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.RawValue;
import io.challenge.lightfeather.Application;
import io.challenge.supervisor.Supervisor;
import java.util.List;
import java.util.UUID;
import net.minidev.json.JSONObject;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@DisplayName("Simple Test")
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.DEFINED_PORT)
class ApplicationTests {

  private static final String AWS_URL =
      "https://o3m5qixdng.execute-api.us-east-1.amazonaws.com/api/managers";
  private final String BASE_URL = "http://localhost:8080/api";
  private static final JSONObject expectedJson = new JSONObject();
  private static final ObjectMapper mapper = new ObjectMapper();
  private static final WebClient webClient = WebClient.create();
  private static Mono<List> supervisors;

  @BeforeAll
  public static void initializeSupervisors() {
    supervisors = webClient.get().uri(AWS_URL).retrieve().bodyToMono(List.class);
  }

  @BeforeEach
  public void initializeUser() {
    expectedJson.put("user-firstName", "Harry");
    expectedJson.put("user-lastName", "Pita");
    expectedJson.put("user-email", "hp@gmail.com");
    expectedJson.put("user-phoneNumber", "123-333-6563");
    expectedJson.put("supervisor", supervisors.block().get(0));
  }

  @Test
  public void testAWSEndpoint() {
    assertEquals(50, supervisors.block().size());
  }

  @Test
  public void testAllSupervisors() {
    assertEquals(
        35,
        webClient
            .get()
            .uri(BASE_URL + "/supervisors")
            .retrieve()
            .bodyToMono(List.class)
            .block()
            .size());
  }

  @Test
  public void testValidUserRegistration() throws JSONException {
    JSONObject responseJson =
        webClient
            .post()
            .uri(BASE_URL + "/submit")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(expectedJson.toJSONString())
            .retrieve()
            .bodyToMono(JSONObject.class)
            .block();

    System.out.println("Response: " + responseJson.toJSONString());
    JSONAssert.assertEquals(
        expectedJson.toJSONString().replaceAll("\"(\\d{1})\"", "$1"),
        responseJson.toJSONString(),
        false);
  }

  @Test
  public void testMissingEmail() throws JSONException {
    expectedJson.remove("user-email");
    System.out.println("Expected: " + expectedJson.toJSONString());
    testValidUserRegistration();
  }

  @Test
  public void testMissingPhone() throws JSONException {
    expectedJson.remove("user-phoneNumber");
    testValidUserRegistration();
  }

  @Test
  public void testMissingFirstName() {
    expectedJson.remove("user-firstName");

    assertThrows(
        WebClientResponseException.class,
        () -> {
          webClient
              .post()
              .uri(BASE_URL + "/submit")
              .contentType(MediaType.APPLICATION_JSON)
              .bodyValue(expectedJson.toJSONString())
              .retrieve()
              .bodyToMono(JSONObject.class)
              .block();
        });
  }

  @Test
  public void testMissingLastName() {
    expectedJson.remove("user-lastName");

    assertThrows(
        WebClientResponseException.class,
        () -> {
          webClient
              .post()
              .uri(BASE_URL + "/submit")
              .contentType(MediaType.APPLICATION_JSON)
              .bodyValue(expectedJson.toJSONString())
              .retrieve()
              .bodyToMono(JSONObject.class)
              .block();
        });
  }

  @Test
  public void testMissingSupervisor() {
    expectedJson.remove("supervisor");

    assertThrows(
        WebClientResponseException.class,
        () -> {
          webClient
              .post()
              .uri(BASE_URL + "/submit")
              .contentType(MediaType.APPLICATION_JSON)
              .bodyValue(expectedJson.toJSONString())
              .retrieve()
              .bodyToMono(JSONObject.class)
              .block();
        });
  }

  @Test
  public void testInvalidSupervisor() throws JsonProcessingException {
    UUID newUuid = UUID.randomUUID();
    Supervisor newSupervisor = new Supervisor(1, "111-222-3333", 'a', newUuid, "Tonya", "Paterson");

    expectedJson.put("supervisor", new RawValue(mapper.writeValueAsString(newSupervisor)));

    assertThrows(
        WebClientResponseException.class,
        () -> {
          webClient
              .post()
              .uri(BASE_URL + "/submit")
              .contentType(MediaType.APPLICATION_JSON)
              .bodyValue(expectedJson.toJSONString())
              .retrieve()
              .bodyToMono(JSONObject.class)
              .block();
        });
  }
}
