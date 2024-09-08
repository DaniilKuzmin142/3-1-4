package com.example.demo;

import model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	private final RestTemplate restTemplate = new RestTemplate();
	private final String url = "http://94.198.50.185:7081/api/users";
	private String sessionId;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) {

		getUsers();
		saveUser(new User(3L, "James", "Brown", (byte) 25));
		updateUser(new User(3L, "Thomas", "Shelby", (byte) 25));
		deleteUser(3L);
	}

	private void getUsers() {
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		HttpHeaders headers = response.getHeaders();
		sessionId = headers.getFirst(HttpHeaders.SET_COOKIE);
		System.out.println("Session ID: " + sessionId);
	}

	private void saveUser(User user) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.COOKIE, sessionId);
		HttpEntity<User> request = new HttpEntity<>(user, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
		System.out.println("Response after saving user: " + response.getBody());
	}

	private void updateUser(User user) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.COOKIE, sessionId);
		HttpEntity<User> request = new HttpEntity<>(user, headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
		System.out.println("Response after updating user: " + response.getBody());
	}

	private void deleteUser(Long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.COOKIE, sessionId);
		HttpEntity<Void> request = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(url + "/" + id, HttpMethod.DELETE, request, String.class);
		System.out.println("Response after deleting user: " + response.getBody());
	}
}

