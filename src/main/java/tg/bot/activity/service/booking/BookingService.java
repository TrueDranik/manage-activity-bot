package tg.bot.activity.service.booking;

import com.bot.sup.common.enums.PaymentStatusEnum;
import com.bot.sup.model.dto.tg.BookingCreateDto;
import com.bot.sup.model.dto.tg.BookingDto;
import com.bot.sup.model.dto.tg.BookingUpdateDto;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;

@Service
public interface BookingService {
    List<BookingDto> getBookingByScheduleId(Long scheduleId);

    List<BookingDto> getBookingByScheduleIdByPaymentStatus(Long scheduleId, String paymentStatus);

    Integer getAmountBookedPlacesByRoute(Long id);

    Integer getAmountBookedPlacesByActivity(Long id);

    BookingDto createBooking(BookingCreateDto bookingCreateDto);

    BookingDto updateBooking(BookingUpdateDto bookingUpdateDto);

    EnumMap<PaymentStatusEnum, Integer> getBookingAmountsByScheduleIdByPaymentStatus(Long scheduleId);

}
