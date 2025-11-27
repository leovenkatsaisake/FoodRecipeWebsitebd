package klu.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import klu.repository.UsersRepository;

import java.util.Optional;

@Service
public class UsersManager {

	@Autowired
	private UsersRepository UR;

	@Autowired
	private EmailManager EM;

	@Autowired
	private JWTManager JWT;

	public String addUser(Users U) {
		try {
			if (U.getEmail() == null || U.getEmail().trim().isEmpty()) {
				return "400::Email is required";
			}
			if (U.getPassword() == null || U.getPassword().trim().isEmpty()) {
				return "400::Password is required";
			}
			if (U.getFullname() == null || U.getFullname().trim().isEmpty()) {
				return "400::Full name is required";
			}

			if (UR.validateEmail(U.getEmail()) > 0) {
				return "409::Email already exists";
			}

			UR.save(U);
			return "200::User registered successfully";
		} catch (Exception e) {
			return "500::Error registering user: " + e.getMessage();
		}
	}

	public String recoverPassword(String email) {
		try {
			if (email == null || email.trim().isEmpty()) {
				return "400::Email is required";
			}

			Optional<Users> userOptional = UR.findById(email);
			if (userOptional.isEmpty()) {
				return "404::Email not registered";
			}

			Users U = userOptional.get();
			String message = String.format(
				"Dear %s,\n\nYour password is: %s\n\nRegards,\nFood Recipe Team",
				U.getFullname(),
				U.getPassword()
			);

			return EM.sendEmail(U.getEmail(), "Food Recipe: Password Recovery", message);
		} catch (Exception e) {
			return "500::Error recovering password: " + e.getMessage();
		}
	}

	public String validateCredentials(String email, String password) {
		try {
			if (email == null || email.trim().isEmpty()) {
				return "400::Email is required";
			}
			if (password == null || password.trim().isEmpty()) {
				return "400::Password is required";
			}

			if (UR.validateCresentials(email, password) > 0) {
				String token = JWT.generateToken(email);
				return "200::" + token;
			}

			return "401::Invalid credentials";
		} catch (Exception e) {
			return "500::Error validating credentials: " + e.getMessage();
		}
	}

	public String getFullName(String token) {
		try {
			if (token == null || token.trim().isEmpty()) {
				return "400::Token is required";
			}

			String emailid = JWT.validateToken(token);
			if ("401".equals(emailid)) {
				return "401::Invalid token";
			}

			Optional<Users> userOptional = UR.findById(emailid);
			if (userOptional.isEmpty()) {
				return "404::User not found";
			}

			Users U = userOptional.get();
			return "200::" + U.getFullname();
		} catch (Exception e) {
			return "500::Error getting user info: " + e.getMessage();
		}
	}

	public Optional<Users> getUserByEmail(String email) {
		return UR.findById(email);
	}

	public String saveRecipeForUser(String email, String foodItemId) {
		try {
			Optional<Users> userOptional = UR.findById(email);
			if (userOptional.isEmpty()) return "404::User not found";
			Users user = userOptional.get();
			if (user.getSavedRecipes() == null) user.setSavedRecipes(new java.util.ArrayList<>());
			if (!user.getSavedRecipes().contains(foodItemId)) user.getSavedRecipes().add(foodItemId);
			UR.save(user);
			return "200::Recipe saved";
		} catch (Exception e) {
			return "500::Error saving recipe: " + e.getMessage();
		}
	}

	public String unsaveRecipeForUser(String email, String foodItemId) {
		try {
			Optional<Users> userOptional = UR.findById(email);
			if (userOptional.isEmpty()) return "404::User not found";
			Users user = userOptional.get();
			if (user.getSavedRecipes() != null) {
				user.getSavedRecipes().removeIf(id -> id.equals(foodItemId));
				UR.save(user);
			}
			return "200::Recipe unsaved";
		} catch (Exception e) {
			return "500::Error unsaving recipe: " + e.getMessage();
		}
	}
}
