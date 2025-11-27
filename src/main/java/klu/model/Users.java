package klu.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "users")
public class Users {

	@Column(name = "fullname", nullable = false, length = 255)
	private String fullname;

	@Id
	@Column(name = "email", nullable = false, unique = true, length = 255)
	private String email;

	@Column(name = "password", nullable = false, length = 255)
	private String password;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@ElementCollection
	@CollectionTable(name = "user_saved_recipes", joinColumns = @JoinColumn(name = "user_email"))
	@Column(name = "food_item_id")
	private List<String> savedRecipes;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<String> getSavedRecipes() {
		if (savedRecipes == null) savedRecipes = new ArrayList<>();
		return savedRecipes;
	}

	public void setSavedRecipes(List<String> savedRecipes) {
		this.savedRecipes = savedRecipes;
	}
}
