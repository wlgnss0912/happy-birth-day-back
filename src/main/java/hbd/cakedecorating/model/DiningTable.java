package hbd.cakedecorating.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "diningtable")
public class DiningTable {

    @Id
    @GeneratedValue
    @Column(name = "table_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String url;

    @Column(name = "cre_date")
    private LocalDateTime creDate;

    @OneToMany(mappedBy = "diningTable")
    private List<Letter> letters = new ArrayList<>();

    public DiningTable updateUrl(String url) {
        this.url = url;
        return this;
    }

}
