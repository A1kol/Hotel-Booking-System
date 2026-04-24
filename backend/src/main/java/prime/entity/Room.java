package prime.entity;

import jakarta.persistence.*;
import lombok.*;
import prime.enums.Type;

@Entity
@Table(name = "rooms")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "room_number", unique = true)
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false, name = "price_per_day")
    private Integer pricePerNight;

    @Column(nullable = false)
    private Integer capacity;
}
