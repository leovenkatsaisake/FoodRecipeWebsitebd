// ...existing code...
package klu.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import klu.repository.FoodItemRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FoodItemManager {

    @Autowired
    private FoodItemRepository foodItemRepository;

    public String addFoodItem(FoodItem foodItem) {
        try {
            if (foodItem.getName() == null || foodItem.getName().trim().isEmpty()) {
                return "400::Food item name is required";
            }

            foodItemRepository.save(foodItem);
            return "200::Food item added successfully";
        } catch (Exception e) {
            return "500::Error adding food item: " + e.getMessage();
        }
    }

    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll();
    }

    public Optional<FoodItem> getFoodItemById(String id) {
        return foodItemRepository.findById(id);
    }

    public List<FoodItem> getFoodItemsByCategory(String category) {
        return foodItemRepository.findByCategory(category);
    }

    public List<FoodItem> getFoodItemsByCuisine(String cuisine) {
        return foodItemRepository.findByCuisine(cuisine);
    }

    public List<FoodItem> getFoodItemsByDifficulty(String difficulty) {
        return foodItemRepository.findByDifficulty(difficulty);
    }

    public List<FoodItem> getFoodItemsByCreator(String email) {
        return foodItemRepository.findByCreatedBy(email);
    }

    public List<FoodItem> searchFoodItems(String keyword) {
        return foodItemRepository.searchByKeyword(keyword);
    }

    public List<String> getAllCategories() {
        return foodItemRepository.findAllCategories();
    }

    public List<String> getAllCuisines() {
        return foodItemRepository.findAllCuisines();
    }

    public List<FoodItem> getTopRatedFoodItems() {
        return foodItemRepository.findTopRated();
    }

    public String updateFoodItem(String id, FoodItem updatedFoodItem) {
        try {
            Optional<FoodItem> existingItem = foodItemRepository.findById(id);

            if (existingItem.isEmpty()) {
                return "404::Food item not found";
            }

            FoodItem foodItem = existingItem.get();

            if (updatedFoodItem.getName() != null) foodItem.setName(updatedFoodItem.getName());
            if (updatedFoodItem.getDescription() != null) foodItem.setDescription(updatedFoodItem.getDescription());
            if (updatedFoodItem.getCategory() != null) foodItem.setCategory(updatedFoodItem.getCategory());
            if (updatedFoodItem.getCuisine() != null) foodItem.setCuisine(updatedFoodItem.getCuisine());
            if (updatedFoodItem.getIngredients() != null) foodItem.setIngredients(updatedFoodItem.getIngredients());
            if (updatedFoodItem.getInstructions() != null) foodItem.setInstructions(updatedFoodItem.getInstructions());
            if (updatedFoodItem.getPrepTime() != null) foodItem.setPrepTime(updatedFoodItem.getPrepTime());
            if (updatedFoodItem.getCookTime() != null) foodItem.setCookTime(updatedFoodItem.getCookTime());
            if (updatedFoodItem.getServings() != null) foodItem.setServings(updatedFoodItem.getServings());
            if (updatedFoodItem.getDifficulty() != null) foodItem.setDifficulty(updatedFoodItem.getDifficulty());
            if (updatedFoodItem.getImageUrl() != null) foodItem.setImageUrl(updatedFoodItem.getImageUrl());
            if (updatedFoodItem.getVideoUrl() != null) foodItem.setVideoUrl(updatedFoodItem.getVideoUrl());
            if (updatedFoodItem.getCalories() != null) foodItem.setCalories(updatedFoodItem.getCalories());
            if (updatedFoodItem.getRating() != null) foodItem.setRating(updatedFoodItem.getRating());

            if (updatedFoodItem.getRatings() != null) foodItem.setRatings(updatedFoodItem.getRatings());
            if (updatedFoodItem.getComments() != null) foodItem.setComments(updatedFoodItem.getComments());

            foodItemRepository.save(foodItem);
            return "200::Food item updated successfully";
        } catch (Exception e) {
            return "500::Error updating food item: " + e.getMessage();
        }
    }

    public String deleteFoodItem(String id) {
        try {
            if (!foodItemRepository.existsById(id)) {
                return "404::Food item not found";
            }

            foodItemRepository.deleteById(id);
            return "200::Food item deleted successfully";
        } catch (Exception e) {
            return "500::Error deleting food item: " + e.getMessage();
        }
    }
    public List<FoodItem> addFoodItems(List<FoodItem> items) {
        return foodItemRepository.saveAll(items);
    }

    // Add a numeric rating (1-5) to a food item
    public String addRatingToFoodItem(String id, Integer ratingValue) {
        try {
            Optional<FoodItem> existingItem = foodItemRepository.findById(id);
            if (existingItem.isEmpty()) return "404::Food item not found";

            FoodItem foodItem = existingItem.get();
            if (foodItem.getRatings() == null) foodItem.setRatings(new java.util.ArrayList<>());
            foodItem.getRatings().add(ratingValue);
            // update average rating field for convenience
            double avg = foodItem.getRatings().stream().mapToInt(Integer::intValue).average().orElse(0.0);
            foodItem.setRating(avg);
            foodItemRepository.save(foodItem);
            return "200::Rating added";
        } catch (Exception e) {
            return "500::Error adding rating: " + e.getMessage();
        }
    }

    // Add a comment to a food item
    public String addCommentToFoodItem(String id, Comment comment) {
        try {
            Optional<FoodItem> existingItem = foodItemRepository.findById(id);
            if (existingItem.isEmpty()) return "404::Food item not found";

            FoodItem foodItem = existingItem.get();
            if (foodItem.getComments() == null) foodItem.setComments(new java.util.ArrayList<>());
            // assign a timestamp-based id if none
            if (comment.getId() == null) comment.setId(System.currentTimeMillis());
            foodItem.getComments().add(comment);
            foodItemRepository.save(foodItem);
            return "200::Comment added";
        } catch (Exception e) {
            return "500::Error adding comment: " + e.getMessage();
        }
    }

    // Edit an existing comment by comment id
    public String editCommentOnFoodItem(String id, Long commentId, String newText) {
        try {
            Optional<FoodItem> existingItem = foodItemRepository.findById(id);
            if (existingItem.isEmpty()) return "404::Food item not found";

            FoodItem foodItem = existingItem.get();
            if (foodItem.getComments() == null) return "404::Comment not found";

            boolean found = false;
            for (Comment c : foodItem.getComments()) {
                if (c.getId() != null && c.getId().equals(commentId)) {
                    c.setText(newText);
                    // update time to now
                    c.setTime(java.time.Instant.now().toString());
                    found = true;
                    break;
                }
            }

            if (!found) return "404::Comment not found";

            foodItemRepository.save(foodItem);
            return "200::Comment updated";
        } catch (Exception e) {
            return "500::Error editing comment: " + e.getMessage();
        }
    }

    // Delete a comment by comment id
    public String deleteCommentFromFoodItem(String id, Long commentId) {
        try {
            Optional<FoodItem> existingItem = foodItemRepository.findById(id);
            if (existingItem.isEmpty()) return "404::Food item not found";

            FoodItem foodItem = existingItem.get();
            if (foodItem.getComments() == null) return "404::Comment not found";

            boolean removed = foodItem.getComments().removeIf(c -> c.getId() != null && c.getId().equals(commentId));
            if (!removed) return "404::Comment not found";

            foodItemRepository.save(foodItem);
            return "200::Comment deleted";
        } catch (Exception e) {
            return "500::Error deleting comment: " + e.getMessage();
        }
    }
}
