package io.challenge.supervisor;

import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.RawValue;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import io.challenge.exceptions.SupervisorException;

@Service
public class SupervisorService {

	private final String AWS_URL = "https://o3m5qixdng.execute-api.us-east-1.amazonaws.com";
	private final WebClient webClient = WebClient.create(AWS_URL);

	private ObjectMapper objectMapper = new ObjectMapper();

	private final ObjectNode errorJson = objectMapper.createObjectNode();
	private final ResponseEntity<ObjectNode> errorEntity = new ResponseEntity<>(errorJson, HttpStatus.BAD_REQUEST);

	List<Supervisor> supervisors;

	private static final Comparator<Supervisor> jurisdictionComparator = (s1, s2) -> Character.compare(
			s1.getJurisdiction(),
			s2.getJurisdiction());
	private static final Comparator<Supervisor> lastNameComparator = (s1, s2) -> s1.getLastName()
			.compareTo(s2.getLastName());
	private static final Comparator<Supervisor> firstComparator = (s1, s2) -> s1.getFirstName()
			.compareTo(s2.getFirstName());

	public SupervisorService() {

		errorJson.put("error", "firstName, lastName, and supervisor required!");
		supervisors = webClient.get()
				.uri("/api/managers")
				.retrieve()
				.bodyToFlux(Supervisor.class)
				.collectList()
				.block();
	}

	public void sortSupervisors() {
		supervisors = supervisors.stream()
				.sorted(jurisdictionComparator
						.thenComparing(lastNameComparator
								.thenComparing(firstComparator)))
				.toList();
	}

	public List<Supervisor> getSupervisors() {
		sortSupervisors();
		return supervisors.stream()
				.filter(s -> Character.isAlphabetic(s.getJurisdiction()))
				.toList();
	}

	public ResponseEntity<ObjectNode> registerUser(ObjectNode json) {

		try {
			JsonNode firstName = json.get("user-firstName");
			JsonNode lastName = json.get("user-lastName");
			JsonNode email = json.get("user-email");
			JsonNode phoneNumber = json.get("user-phoneNumber");

			ObjectNode responseJson = objectMapper.createObjectNode();
			ResponseEntity<ObjectNode> responseEntity = new ResponseEntity<>(responseJson, HttpStatus.OK);

			responseJson.set("user-firstName", firstName);
			responseJson.set("user-lastName", lastName);

			if (firstName == null || lastName == null) {
				throw new NullPointerException();
			}

			if (email != null) {
				responseJson.set("user-email", email);
			}

			if (phoneNumber != null) {
				responseJson.set("user-phoneNumber", phoneNumber);
			}

			try {
				// convert json to object
				String supervisorJson = json.get("supervisor").toPrettyString();

				Supervisor supervisor = objectMapper.readValue(supervisorJson,
						Supervisor.class);

				if (!(supervisors.stream().anyMatch(s -> s.equals(supervisor)))) {
					throw new SupervisorException("Invalid supervisor: " + supervisorJson);
				}

				responseJson.putRawValue("supervisor", new RawValue(objectMapper.writeValueAsString(supervisor)));

				return responseEntity;

			} catch (NullPointerException e) {
				System.err.println("Missing supervisor");
				e.printStackTrace();

				return errorEntity;
			} catch (JsonProcessingException e) {
				errorJson.put("error", "JSON Parsing Exeception");
				System.err.println("JSON Parsing Error");
				e.printStackTrace();

				return errorEntity;
			} catch (SupervisorException e) {
				errorJson.put("error", "Invalid supervisor name");
				System.err.println("JSON Parsing Error");
				e.printStackTrace();

				return errorEntity;

			}
		} catch (NullPointerException e) {
			System.err.println("Missing firstName or lastName");
			e.printStackTrace();

			return errorEntity;
		}

	}

}
