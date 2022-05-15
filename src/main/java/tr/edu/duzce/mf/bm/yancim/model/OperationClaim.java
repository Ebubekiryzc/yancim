package tr.edu.duzce.mf.bm.yancim.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "operation_claims")
public class OperationClaim {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "operationClaim", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserOperationClaim> userOperationClaims;

    @OneToMany(mappedBy = "operationClaim", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomUser> roomUsers;
}
