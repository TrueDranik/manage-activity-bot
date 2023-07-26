package tg.bot.activity.service.client;

import com.bot.sup.model.dto.tg.BookingCreateDto;
import com.bot.sup.model.dto.tg.ClientDto;

public interface ClientService {
    ClientDto createClient(BookingCreateDto bookingCreateDto);
}
