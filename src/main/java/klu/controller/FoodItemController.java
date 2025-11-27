package klu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import klu.model.FoodItem;
import klu.model.FoodItemManager;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fooditems")
@CrossOrigin(origins = "*")
public class FoodItemController {

    @Autowired
    private FoodItemManager foodItemManager;

    @GetMapping
    public ResponseEntity<?> getAllFoodItems() {
        try {
            List<FoodItem> foodItems = foodItemManager.getAllFoodItems();
            return ResponseEntity.ok(foodItems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving food items: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFoodItemById(@PathVariable String id) {
        try {
            Optional<FoodItem> foodItem = foodItemManager.getFoodItemById(id);
            if (foodItem.isPresent()) {
                return ResponseEntity.ok(foodItem.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Food item not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving food item: " + e.getMessage());
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getFoodItemsByCategory(@PathVariable String category) {
        try {
            List<FoodItem> foodItems = foodItemManager.getFoodItemsByCategory(category);
            return ResponseEntity.ok(foodItems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving food items by category: " + e.getMessage());
        }
    }

    @GetMapping("/cuisine/{cuisine}")
    public ResponseEntity<?> getFoodItemsByCuisine(@PathVariable String cuisine) {
        try {
            List<FoodItem> foodItems = foodItemManager.getFoodItemsByCuisine(cuisine);
            return ResponseEntity.ok(foodItems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving food items by cuisine: " + e.getMessage());
        }
    }

    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<?> getFoodItemsByDifficulty(@PathVariable String difficulty) {
        try {
            List<FoodItem> foodItems = foodItemManager.getFoodItemsByDifficulty(difficulty);
            return ResponseEntity.ok(foodItems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving food items by difficulty: " + e.getMessage());
        }
    }

    @GetMapping("/creator/{email}")
    public ResponseEntity<?> getFoodItemsByCreator(@PathVariable String email) {
        try {
            List<FoodItem> foodItems = foodItemManager.getFoodItemsByCreator(email);
            return ResponseEntity.ok(foodItems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving food items by creator: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchFoodItems(@RequestParam String keyword) {
        try {
            List<FoodItem> foodItems = foodItemManager.searchFoodItems(keyword);
            return ResponseEntity.ok(foodItems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error searching food items: " + e.getMessage());
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        try {
            List<String> categories = foodItemManager.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving categories: " + e.getMessage());
        }
    }

    @GetMapping("/cuisines")
    public ResponseEntity<?> getAllCuisines() {
        try {
            List<String> cuisines = foodItemManager.getAllCuisines();
            return ResponseEntity.ok(cuisines);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving cuisines: " + e.getMessage());
        }
    }

    @GetMapping("/top-rated")
    public ResponseEntity<?> getTopRatedFoodItems() {
        try {
            List<FoodItem> foodItems = foodItemManager.getTopRatedFoodItems();
            return ResponseEntity.ok(foodItems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving top rated food items: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/ratings")
    public ResponseEntity<String> addRating(@PathVariable String id, @RequestBody Integer rating) {
        String result = foodItemManager.addRatingToFoodItem(id, rating);
        String[] parts = result.split("::", 2);
        int statusCode = Integer.parseInt(parts[0]);
        String message = parts[1];
        return ResponseEntity.status(statusCode).body(message);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<String> addComment(@PathVariable String id, @RequestBody java.util.Map<String, Object> payload) {
        try {
            // Build Comment manually to avoid binding issues for Embeddable types
            klu.model.Comment comment = new klu.model.Comment();
            if (payload.containsKey("id")) {
                Object v = payload.get("id");
                if (v instanceof Number) comment.setId(((Number) v).longValue());
                else comment.setId(Long.parseLong(v.toString()));
            } else {
                comment.setId(null);
            }
            if (payload.containsKey("text")) comment.setText(payload.get("text").toString());
            if (payload.containsKey("time")) comment.setTime(payload.get("time").toString());

            String result = foodItemManager.addCommentToFoodItem(id, comment);
            String[] parts = result.split("::", 2);
            int statusCode = Integer.parseInt(parts[0]);
            String message = parts[1];
            return ResponseEntity.status(statusCode).body(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("400::Invalid comment payload");
        }
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getComments(@PathVariable String id) {
        Optional<FoodItem> item = foodItemManager.getFoodItemById(id);
        if (item.isPresent()) {
            return ResponseEntity.ok(item.get().getComments());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Food item not found");
    }

    @GetMapping("/{id}/ratings")
    public ResponseEntity<?> getRatings(@PathVariable String id) {
        Optional<FoodItem> item = foodItemManager.getFoodItemById(id);
        if (item.isPresent()) {
            return ResponseEntity.ok(item.get().getRatings());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Food item not found");
    }

    @PutMapping("/{id}/comments/{commentId}")
    public ResponseEntity<String> editComment(@PathVariable String id, @PathVariable Long commentId, @RequestBody java.util.Map<String, Object> payload) {
        try {
            String newText = payload.containsKey("text") ? payload.get("text").toString() : null;
            if (newText == null) return ResponseEntity.badRequest().body("400::Missing text field");
            String result = foodItemManager.editCommentOnFoodItem(id, commentId, newText);
            String[] parts = result.split("::", 2);
            int statusCode = Integer.parseInt(parts[0]);
            String message = parts[1];
            return ResponseEntity.status(statusCode).body(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("500::Error editing comment");
        }
    }

    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable String id, @PathVariable Long commentId) {
        String result = foodItemManager.deleteCommentFromFoodItem(id, commentId);
        String[] parts = result.split("::", 2);
        int statusCode = Integer.parseInt(parts[0]);
        String message = parts[1];
        return ResponseEntity.status(statusCode).body(message);
    }

    @PostMapping
    public ResponseEntity<List<FoodItem>> createFoodItems(@RequestBody List<FoodItem> items) {
        List<FoodItem> savedItems = foodItemManager.addFoodItems(items);
        return ResponseEntity.ok(savedItems);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateFoodItem(@PathVariable String id, @RequestBody FoodItem foodItem) {
        String result = foodItemManager.updateFoodItem(id, foodItem);
        String[] parts = result.split("::", 2);
        int statusCode = Integer.parseInt(parts[0]);
        String message = parts[1];

        return ResponseEntity.status(statusCode).body(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFoodItem(@PathVariable String id) {
        String result = foodItemManager.deleteFoodItem(id);
        String[] parts = result.split("::", 2);
        int statusCode = Integer.parseInt(parts[0]);
        String message = parts[1];

        return ResponseEntity.status(statusCode).body(message);
    }
}
