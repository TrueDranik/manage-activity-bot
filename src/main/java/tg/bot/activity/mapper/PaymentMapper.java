package tg.bot.activity.mapper;

import com.bot.sup.model.dto.PaymentDto;
import com.bot.sup.model.dto.PaymentUpdateDto;
import com.bot.sup.model.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper extends BaseMapper<Payment, PaymentDto> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "type", ignore = true)
    Payment dtoToDomain(PaymentUpdateDto paymentDto);
}
