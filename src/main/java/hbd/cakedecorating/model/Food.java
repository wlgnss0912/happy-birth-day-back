package hbd.cakedecorating.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "food")
public class Food {

    @Id
    @GeneratedValue
    @Column(name = "food_id")
    private Long id;

    @Column(name = "img_name")
    private String imaName;

    @Column(name = "img_path")
    private String imgPath;

    @Column(name = "cre_social_id")
    private LocalDateTime creSocialId;
}
