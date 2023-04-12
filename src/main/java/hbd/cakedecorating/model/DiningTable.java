package hbd.cakedecorating.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "diningtable")
public class DiningTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    //==생성 메서드==/
    public static DiningTable createDiningTable(User user, String personalUrl) {
        DiningTable diningTable = new DiningTable();
        diningTable.setUser(user);
        diningTable.setUrl(personalUrl);
        diningTable.setCreDate(LocalDateTime.now());
        return diningTable;
    }

    @Builder
    public DiningTable(Long id, User user, String url, LocalDateTime creDate, List<Letter> letters) {
        this.id = id;
        this.user = user;
        this.url = url;
        this.creDate = creDate;
        this.letters = letters;
    }
}
