package klu.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import klu.model.Users;
import klu.model.UsersManager;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UsersController {

	@Autowired
	private UsersManager UM;

	@GetMapping
	public ResponseEntity<String> home() {
		return ResponseEntity.ok("Food Recipe Backend is running!");
	}

	@PostMapping("/signup")
	public ResponseEntity<String> signUp(@RequestBody Users U) {
		String result = UM.addUser(U);
		String[] parts = result.split("::", 2);
		int statusCode = Integer.parseInt(parts[0]);
		String message = parts[1];

		return ResponseEntity.status(statusCode).body(message);
	}

	@GetMapping("/forgotpassword/{email}")
	public ResponseEntity<String> forgotPassword(@PathVariable("email") String emailid) {
		String result = UM.recoverPassword(emailid);
		String[] parts = result.split("::", 2);
		int statusCode = Integer.parseInt(parts[0]);
		String message = parts[1];

		return ResponseEntity.status(statusCode).body(message);
	}

	@PostMapping("/signin")
	public ResponseEntity<String> signIn(@RequestBody Users U) {
		String result = UM.validateCredentials(U.getEmail(), U.getPassword());
		String[] parts = result.split("::", 2);
		int statusCode = Integer.parseInt(parts[0]);
		String message = parts[1];

		return ResponseEntity.status(statusCode).body(message);
	}

	@PostMapping("/fullname")
	public ResponseEntity<String> getFullName(@RequestBody Map<String, String> data) {
		String token = data.get("csrid");
		if (token == null || token.trim().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is required");
		}

		String result = UM.getFullName(token);
		String[] parts = result.split("::", 2);
		int statusCode = Integer.parseInt(parts[0]);
		String message = parts[1];

		return ResponseEntity.status(statusCode).body(message);
	}

	@GetMapping("/profile/{email}")
	public ResponseEntity<?> getUserProfile(@PathVariable String email) {
		try {
			return UM.getUserByEmail(email)
					.map(user -> ResponseEntity.ok(user))
					.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving user profile: " + e.getMessage());
		}
	}

	@PostMapping("/{email}/saved/{foodId}")
	public ResponseEntity<String> saveRecipe(@PathVariable String email, @PathVariable String foodId) {
		String result = UM.saveRecipeForUser(email, foodId);
		String[] parts = result.split("::", 2);
		int statusCode = Integer.parseInt(parts[0]);
		String message = parts[1];
		return ResponseEntity.status(statusCode).body(message);
	}

	@DeleteMapping("/{email}/saved/{foodId}")
	public ResponseEntity<String> unsaveRecipe(@PathVariable String email, @PathVariable String foodId) {
		String result = UM.unsaveRecipeForUser(email, foodId);
		String[] parts = result.split("::", 2);
		int statusCode = Integer.parseInt(parts[0]);
		String message = parts[1];
		return ResponseEntity.status(statusCode).body(message);
	}

	@GetMapping("/{email}/saved")
	public ResponseEntity<?> getSavedRecipes(@PathVariable String email) {
		try {
			return UM.getUserByEmail(email)
					.map(user -> ResponseEntity.ok(user.getSavedRecipes()))
					.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving saved recipes: " + e.getMessage());
		}
	}
}
