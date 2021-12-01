package io.challenge.supervisor;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Supervisor {

	private long id;
	private String phone;
	private char jurisdiction;
	private UUID identificationNumber;
	private String firstName, lastName;

	@Override
	public String toString() {
		return String.format("%c - %s, %s", jurisdiction, lastName, firstName);
	}

}
