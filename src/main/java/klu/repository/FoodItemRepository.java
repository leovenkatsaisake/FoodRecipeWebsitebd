package klu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import klu.model.FoodItem;

import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, String> {

    @Query("SELECT f FROM FoodItem f WHERE f.category = :category")
    List<FoodItem> findByCategory(@Param("category") String category);

    @Query("SELECT f FROM FoodItem f WHERE f.cuisine = :cuisine")
    List<FoodItem> findByCuisine(@Param("cuisine") String cuisine);

    @Query("SELECT f FROM FoodItem f WHERE f.difficulty = :difficulty")
    List<FoodItem> findByDifficulty(@Param("difficulty") String difficulty);

    @Query("SELECT f FROM FoodItem f WHERE f.createdBy = :email")
    List<FoodItem> findByCreatedBy(@Param("email") String email);

    @Query("SELECT f FROM FoodItem f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(f.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<FoodItem> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT DISTINCT f.category FROM FoodItem f WHERE f.category IS NOT NULL AND f.category != '' ORDER BY f.category")
    List<String> findAllCategories();

    @Query("SELECT DISTINCT f.cuisine FROM FoodItem f WHERE f.cuisine IS NOT NULL AND f.cuisine != '' ORDER BY f.cuisine")
    List<String> findAllCuisines();

    @Query("SELECT f FROM FoodItem f ORDER BY f.rating DESC")
    List<FoodItem> findTopRated();
}
