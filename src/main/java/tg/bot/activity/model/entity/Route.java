package tg.bot.activity.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Getter
@Setter
@Entity
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30)
    private String name;
    @Column(length = 300)
    private String startPointCoordinates;
    @Column(length = 30)
    private String startPointName;
    @Column(length = 300)
    private String finishPointCoordinates;
    @Column(length = 30)
    private String finishPointName;
    @Column(length = 300)
    private String mapLink;
    @Column(length = 30)
    private String length;
    private Boolean isActive;
    @OneToOne
    private Photo photo;
}
