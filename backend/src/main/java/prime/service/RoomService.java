package prime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import prime.tools.Mapper;
import prime.dto.RoomDTO;
import prime.repository.RoomRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final Mapper mapper;

    public void addExecute(RoomDTO roomDTO) {
        roomRepository.save(mapper.toRoom(roomDTO));
    }

    public List<RoomDTO> viewAllExecute() {
        return roomRepository.findAll()
                .stream()
                .map(Mapper::toRoomDTO)
                .toList();
    }
}
