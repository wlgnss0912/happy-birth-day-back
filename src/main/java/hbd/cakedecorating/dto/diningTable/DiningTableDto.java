package hbd.cakedecorating.dto.diningTable;

import hbd.cakedecorating.api.model.DiningTable;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class DiningTableDto {

    @Getter
    public static class DiningTableRequestDto {

        private Long id;
        private String url;
        private Long userId;
        private LocalDateTime creDate;

        public DiningTable toEntity(Long userId) {
            return DiningTable.builder()
                    .id(id)
                    .url(url)
                    .creDate(creDate)
                    .build();
        }
    }
}
