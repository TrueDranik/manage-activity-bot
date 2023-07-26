package tg.bot.activity.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_id")
    private Activity activity;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private Integer participants;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "route_id")
    private Route route;
    private Boolean isActive;
    @OneToMany
    @JoinColumn(name = "schedule_id")
    private Set<Booking> booking;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "schedule_instructor",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "instructor_id")
    )
    private Set<Instructor> instructor;
    private Integer amountFreePlaces;
    @Type(type = "dbtimestamp")
    @Version
    private Date modifTime;

    @Override
    public String toString() {
        return activity.getName() +
                " \t\uD83D\uDDD3" + eventDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) +
                " \uD83D\uDD53" + eventTime + "\n";
    }
}
