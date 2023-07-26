package tg.bot.activity.service.client;

import tg.bot.activity.model.dto.tg.BookingCreateDto;
import tg.bot.activity.model.dto.tg.ClientDto;

public interface ClientService {

    ClientDto createClient(BookingCreateDto bookingCreateDto);
}
