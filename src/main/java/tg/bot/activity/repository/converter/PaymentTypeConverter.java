package tg.bot.activity.repository.converter;

import tg.bot.activity.common.enums.PaymentTypeEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PaymentTypeConverter implements AttributeConverter<PaymentTypeEnum, String> {

    @Override
    public String convertToDatabaseColumn(PaymentTypeEnum attribute) {
        return PaymentTypeEnum.convertToTitleEng(attribute);
    }

    @Override
    public PaymentTypeEnum convertToEntityAttribute(String dbData) {
        return PaymentTypeEnum.convertToEnum(dbData);
    }
}
