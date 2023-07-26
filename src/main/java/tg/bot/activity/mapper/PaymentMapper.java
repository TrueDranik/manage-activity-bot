package tg.bot.activity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tg.bot.activity.model.dto.PaymentDto;
import tg.bot.activity.model.dto.PaymentUpdateDto;
import tg.bot.activity.model.entity.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper extends BaseMapper<Payment, PaymentDto> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", ignore = true)
    Payment dtoToDomain(PaymentUpdateDto paymentDto);
}
