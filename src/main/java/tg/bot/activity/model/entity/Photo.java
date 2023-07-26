package tg.bot.activity.model.entity;

import lombok.*;

import java.sql.Timestamp;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;
    private Timestamp downloadDateTime;
    private String nameFromRequest;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "smallPhoto_id")
    private PhotoSmallSize smallPhoto;
    @JoinColumn(name = "largePhoto_id")
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private PhotoLargeSize largePhoto;
}
