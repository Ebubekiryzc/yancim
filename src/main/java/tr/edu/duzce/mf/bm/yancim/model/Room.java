package tr.edu.duzce.mf.bm.yancim.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "max_player_count", nullable = false)
    private Long maxPlayerCount;

    @Column(name = "address_details", nullable = false)
    private String addressDetails;

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.TIME)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.TIME)
    private Date endDate;


    @ManyToOne
    @JoinColumn(name = "game_type_id", referencedColumnName = "id", nullable = false)
    private GameType gameType;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id", nullable = false)
    private City city;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomUser> roomUsers;
}
