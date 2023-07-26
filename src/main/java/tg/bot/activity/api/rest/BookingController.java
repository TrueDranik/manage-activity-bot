package tg.bot.activity.api.rest;

import com.bot.sup.common.enums.PaymentStatusEnum;
import com.bot.sup.model.dto.tg.BookingCreateDto;
import com.bot.sup.model.dto.tg.BookingDto;
import com.bot.sup.model.dto.tg.BookingUpdateDto;
import com.bot.sup.service.booking.BookingService;
import com.bot.sup.service.client.ClientService;
import com.bot.sup.service.schedule.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/booking")
@Tag(name = "Бронь")
public class BookingController {
    private final BookingService bookingService;
    private final ClientService clientService;
    private final ScheduleService scheduleService;

    @GetMapping("/bookedPlaces/route")
    @Operation(summary = "Получить количесвто записавшихся на все расписания на опрелеленный маршрут")
    @ResponseStatus(HttpStatus.OK)
    public Integer getAmountBookedPlacesByRoute(@RequestParam Long routeId) {
        return bookingService.getAmountBookedPlacesByRoute(routeId);
    }

    @GetMapping("/bookedPlaces/activity")
    @Operation(summary = "Получить количесвто записавшихся на все расписания на опрелеленный маршрут")
    @ResponseStatus(HttpStatus.OK)
    public Integer getAmountBookedPlacesByActivity(@RequestParam Long activityId) {
        return bookingService.getAmountBookedPlacesByActivity(activityId);
    }

    @GetMapping("{scheduleId}")
    @Operation(summary = "Получить записи по scheduleId")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getBookingByScheduleId(@PathVariable(name = "scheduleId") Long scheduleId) {
        return bookingService.getBookingByScheduleId(scheduleId);
    }

    @GetMapping
    @Operation(summary = "Получить записи по scheduleId и paymentStatus ")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getBookingByScheduleIdByPaymentStatus(@RequestParam(value = "id") String id,
                                                                  @RequestParam(value = "paymentStatus") String paymentStatus) {
        return bookingService.getBookingByScheduleIdByPaymentStatus(Long.valueOf(id), paymentStatus);
    }


    @GetMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public Map<PaymentStatusEnum, String> getPaymentStatus() {
        return PaymentStatusEnum.getTitles();
    }

    @GetMapping("/freePlace/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    public Integer getFreePlace(@PathVariable(name = "scheduleId") Long scheduleId) {
        return scheduleService.getFrePlacesByScheduleId(scheduleId);
    }

    @GetMapping("/sortedBooking/{scheduleId}")
    @ResponseStatus(HttpStatus.OK)
    public Map<PaymentStatusEnum, Integer> getBookings(@PathVariable Long scheduleId) {
        return bookingService.getBookingAmountsByScheduleIdByPaymentStatus(scheduleId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать ноую бронь")
    public BookingDto createBooking(@RequestBody BookingCreateDto bookingCreateDto) {
        clientService.createClient(bookingCreateDto);
        return bookingService.createBooking(bookingCreateDto);
    }

    @PutMapping
    @Operation(summary = "Обновление брони")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto updateBooking(@RequestBody BookingUpdateDto bookingUpdateDto) {
        return bookingService.updateBooking(bookingUpdateDto);
    }
}
