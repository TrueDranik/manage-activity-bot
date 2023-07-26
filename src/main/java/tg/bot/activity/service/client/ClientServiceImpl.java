package tg.bot.activity.service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tg.bot.activity.mapper.BookingMapper;
import tg.bot.activity.mapper.ClientMapper;
import tg.bot.activity.model.dto.tg.BookingCreateDto;
import tg.bot.activity.model.dto.tg.ClientCreateDto;
import tg.bot.activity.model.dto.tg.ClientDto;
import tg.bot.activity.model.entity.Client;
import tg.bot.activity.repository.ClientRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final BookingMapper bookingMapper;
    private final ClientMapper clientMapper;
    private final ClientRepository clientRepository;

    public void save(Client client) {
        clientRepository.save(client);
    }

    public Client findById(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Clint with id[" + clientId + "] not found"));
    }

    public List<Client> findClientByScheduleId(Long scheduleId) {
        return clientRepository.findClientByScheduleId(scheduleId);
    }

    @Override
    public ClientDto createClient(BookingCreateDto bookingCreateDto) {
        ClientCreateDto clientCreateDto = bookingMapper.dtoToDto(bookingCreateDto);
        Client client = clientRepository.findByPhoneNumber(clientCreateDto.getPhoneNumber());

        if (client == null) {
            Client createdClient = new Client();
            createdClient.setFirstName(clientCreateDto.getFirstName());
            createdClient.setLastName(clientCreateDto.getLastName());
            createdClient.setPhoneNumber(clientCreateDto.getPhoneNumber());
            clientRepository.save(createdClient);
            return clientMapper.domainToDto(createdClient);
        }

        return clientMapper.domainToDto(client);
    }
}
