package hbd.cakedecorating.api.repository.food;

import hbd.cakedecorating.api.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {

}
