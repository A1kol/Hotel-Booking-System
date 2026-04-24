package prime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prime.entity.Room;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, String> {
    Optional<Room> findByRoomNumber(String roomNumber);
}
