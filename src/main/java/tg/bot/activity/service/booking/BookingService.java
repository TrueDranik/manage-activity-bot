package tg.bot.activity.service.booking;

import org.springframework.stereotype.Service;
import tg.bot.activity.common.enums.PaymentStatusEnum;
import tg.bot.activity.model.dto.tg.BookingCreateDto;
import tg.bot.activity.model.dto.tg.BookingDto;
import tg.bot.activity.model.dto.tg.BookingUpdateDto;

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
