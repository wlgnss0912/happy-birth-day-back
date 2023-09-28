package hbd.cakedecorating.api.repository.diningTable;

import hbd.cakedecorating.api.model.DiningTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiningTableRepository extends JpaRepository<DiningTable, Long> {
    DiningTable findByUrl(String url);
}
