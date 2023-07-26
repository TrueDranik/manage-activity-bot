package tg.bot.activity.service.booking;

import com.bot.sup.common.enums.PaymentStatusEnum;
import com.bot.sup.exception.NotEnoughFreePlacesException;
import com.bot.sup.mapper.BookingMapper;
import com.bot.sup.model.dto.tg.BookingCreateDto;
import com.bot.sup.model.dto.tg.BookingDto;
import com.bot.sup.model.dto.tg.BookingUpdateDto;
import com.bot.sup.model.entity.Booking;
import com.bot.sup.model.entity.Client;
import com.bot.sup.model.entity.Schedule;
import com.bot.sup.repository.BookingRepository;
import com.bot.sup.repository.ClientRepository;
import com.bot.sup.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;

    private final ScheduleRepository scheduleRepository;

    private final ClientRepository clientRepository;

    @Override
    public List<BookingDto> getBookingByScheduleId(Long scheduleId) {
        List<Booking> bookings = bookingRepository.findBookingByScheduleId(scheduleId);
        return bookingMapper.domainsToDtos(bookings);
    }

    @Override
    public List<BookingDto> getBookingByScheduleIdByPaymentStatus(Long scheduleId, String paymentStatus) {
        List<Booking> bookings = bookingRepository.findBookingByScheduleIdByPaymentStatus(scheduleId, paymentStatus);
        return bookingMapper.domainsToDtos(bookings);
    }

    @Override
    @Transactional
    public BookingDto createBooking(BookingCreateDto bookingcreateDto) {
        Client client = clientRepository.findByPhoneNumber(bookingcreateDto.getPhoneNumber());
        if (client != null) {
            int freePlace = scheduleRepository.getSchedulesById(bookingcreateDto.getScheduleId()).getAmountFreePlaces();
            if (freePlace <= 0) {
                throw new IllegalArgumentException("Free place is 0");
            }
            Schedule schedule = scheduleRepository.getSchedulesById(bookingcreateDto.getScheduleId());
            Booking booking = bookingMapper.dtoToDomain(bookingcreateDto);
            booking.setClient(client);
            booking.setSchedule(schedule);
            booking.setInsTime(LocalDate.now());
            booking.setModifTime(LocalDate.now());
            schedule.setAmountFreePlaces(schedule.getAmountFreePlaces() - (bookingcreateDto.getInvitedUsers() + bookingcreateDto.getInvitedChildren()));
            try {
                scheduleRepository.save(schedule);
            } catch (OptimisticLockException ex) {
                throw new RuntimeException("Something went wrong while updating schedule. Please try later");
            }
            bookingRepository.save(booking);
            return bookingMapper.domainToDto(booking);
        }
        throw new EntityNotFoundException("Client with phone number " + bookingcreateDto.getPhoneNumber() + " not found");
    }

    @Transactional
    @Override
    public BookingDto updateBooking(BookingUpdateDto bookingUpdateDto) {
        Booking bookingToUpdate = bookingRepository.findBookingById(bookingUpdateDto.getId());
        if (bookingToUpdate == null) {
            throw new EntityNotFoundException("Booking with Id " + bookingUpdateDto.getId() + " not found");
        }
        Boolean isActiveFromDb = bookingToUpdate.getIsActive();
        Boolean isActiveUpdated = bookingUpdateDto.getIsActive();

        bookingToUpdate = bookingMapper.dtoToDomain(bookingUpdateDto);
        bookingToUpdate.setModifTime(LocalDate.now());
        Schedule schedule = scheduleRepository.getSchedulesById(bookingUpdateDto.getScheduleId());
        bookingToUpdate.setSchedule(schedule);

        int bookedPlacesFromUpdatedBooking = bookingToUpdate.getInvitedUsers() + bookingToUpdate.getInvitedChildren();

        if (!Objects.equals(isActiveFromDb, isActiveUpdated) && Boolean.FALSE.equals(bookingToUpdate.getIsActive())) {
            UpdateAmountFreePlacesInSchedule(schedule, schedule.getAmountFreePlaces() + bookedPlacesFromUpdatedBooking);
        }
        if (!Objects.equals(isActiveFromDb, isActiveUpdated) && Boolean.TRUE.equals(bookingToUpdate.getIsActive())) {
            validateBookedPlaces(bookedPlacesFromUpdatedBooking, schedule.getAmountFreePlaces());
            UpdateAmountFreePlacesInSchedule(schedule, schedule.getAmountFreePlaces() - bookedPlacesFromUpdatedBooking);
        }
        bookingRepository.save(bookingToUpdate);
        return bookingMapper.domainToDto(bookingToUpdate);
    }

    private void validateBookedPlaces(int bookedPlacesFromCurrentBooking, int amountFreePlacesOnSchedule) {
        if (bookedPlacesFromCurrentBooking > amountFreePlacesOnSchedule) {
            throw new NotEnoughFreePlacesException("Not enough free places");
        }
    }
    private void UpdateAmountFreePlacesInSchedule(Schedule schedule, int updatedFrePlaces) {
        try {
            schedule.setAmountFreePlaces(updatedFrePlaces);
            scheduleRepository.save(schedule);
        } catch (OptimisticLockException ex) {
            throw new RuntimeException("Something went wrong while updating schedule. Please try later");
        }
    }

    @Override
    public EnumMap<PaymentStatusEnum, Integer> getBookingAmountsByScheduleIdByPaymentStatus(Long scheduleId) {
        EnumMap<PaymentStatusEnum, Integer> bookingsAmounts = new EnumMap<>(PaymentStatusEnum.class);
        PaymentStatusEnum[] statuses = PaymentStatusEnum.values();
        for (PaymentStatusEnum status : statuses) {
            bookingsAmounts.put(status, bookingRepository.countAllByScheduleIdAndPaymentStatus(scheduleId, status.name()));
        }

        return bookingsAmounts;
    }

    public Integer getAmountBookedPlacesByRoute(Long id) {
        return bookingRepository.getCountBookedPlacesByRoutId(id);
    }

    @Override
    public Integer getAmountBookedPlacesByActivity(Long id) {
        return bookingRepository.getCountBookedPlacesByActivityId(id);
    }


}
