package hbd.cakedecorating.repository.diningTable;

import hbd.cakedecorating.model.DiningTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiningTableRepository extends JpaRepository<DiningTable, Long> {
    DiningTable findByUrl(String url);
}
