package hbd.cakedecorating.repository.food;

import hbd.cakedecorating.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {

}
