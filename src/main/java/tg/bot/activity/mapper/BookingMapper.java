package tg.bot.activity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tg.bot.activity.model.dto.tg.BookingCreateDto;
import tg.bot.activity.model.dto.tg.BookingDto;
import tg.bot.activity.model.dto.tg.BookingUpdateDto;
import tg.bot.activity.model.dto.tg.ClientCreateDto;
import tg.bot.activity.model.entity.Booking;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper extends BaseMapper<Booking, BookingDto> {

    @Override
    List<BookingDto> domainsToDtos(List<Booking> domains);

    @Override
    @Mapping(source = "schedule", target = "scheduleId")
    BookingDto domainToDto(Booking domain);

    ClientCreateDto dtoToDto(BookingCreateDto bookingCreateDto);

    @Mapping(source = "clientId", target = "client")
    Booking dtoToDomain(BookingUpdateDto bookingUpdateDto);

    Booking dtoToDomain(BookingCreateDto bookingCreateDto);
}
