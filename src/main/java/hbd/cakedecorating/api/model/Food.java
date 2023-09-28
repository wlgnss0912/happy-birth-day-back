package hbd.cakedecorating.api.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "food")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long id;

    @Column(name = "img_name")
    private String imgName;

    @Column(name = "img_path")
    private String imgPath;

    @Column(name = "cre_social_id")
    private String creSocialId;

    @Column(name = "cre_date")
    private LocalDateTime creDate;

    @Builder
    public Food(Long id, String imgName, String imgPath, String creSocialId, LocalDateTime creDate) {
        this.id = id;
        this.imgName = imgName;
        this.imgPath = imgPath;
        this.creSocialId = creSocialId;
        this.creDate = creDate;
    }

}
