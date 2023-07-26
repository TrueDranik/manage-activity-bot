package tg.bot.activity.mapper;

import com.bot.sup.model.dto.tg.BookingCreateDto;
import com.bot.sup.model.dto.tg.BookingDto;
import com.bot.sup.model.dto.tg.BookingUpdateDto;
import com.bot.sup.model.dto.tg.ClientCreateDto;
import com.bot.sup.model.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
