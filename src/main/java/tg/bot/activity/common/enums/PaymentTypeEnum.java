package tg.bot.activity.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ToString
@Getter
@RequiredArgsConstructor
public enum PaymentTypeEnum {

    BANK("BANK", "Банк"),
    TRANSFER("TRANSFER", "Перевод");

    private final String type;
    private final String titleRus;

    private static final Map<String, String> titles;

    static {
        titles = Arrays.stream(PaymentTypeEnum.values())
                .collect(Collectors.toMap(PaymentTypeEnum::getType, PaymentTypeEnum::getTitleRus));
    }

    public static Map<String, String> getTitles() {
        return titles;
    }

    public static PaymentTypeEnum convertToEnum(String titleEng){
        return Stream.of(PaymentTypeEnum.values())
                .filter(status -> status.getType().equals(titleEng))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static String convertToTitleEng(PaymentTypeEnum paymentType){
        return paymentType.type;
    }
}
