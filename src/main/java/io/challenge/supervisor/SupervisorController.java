package io.challenge.supervisor;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class SupervisorController {

	@Autowired
	private SupervisorService supervisorService;

	@GetMapping("/supervisors")
	public List<String> getSupervisor() {
		List<String> formatedSupervisors = supervisorService.getSupervisors().stream()
				.map(Supervisor::toString)
				.collect(Collectors.toList());
		return formatedSupervisors;
	}

	// Error response "These are required parameters [firstName, lastName,
	// supervisor]"
	@PostMapping("/submit")
	public ResponseEntity<ObjectNode> registerUser(@RequestBody ObjectNode json) {

		ResponseEntity<ObjectNode> registerEntity = supervisorService.registerUser(json);
		System.out.println(registerEntity.getBody());
		return registerEntity;
	}

	@ExceptionHandler
	public ResponseEntity<String> handle(HttpMessageNotReadableException e) {
		System.err.println("Error parsing body!\n");
		System.err.println(e.getMessage());
		e.getStackTrace();
		return new ResponseEntity<>("Body parsing error", HttpStatus.BAD_REQUEST);
	}

}
