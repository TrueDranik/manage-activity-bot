package tg.bot.activity;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import tg.bot.activity.common.enums.PaymentTypeEnum;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class PaymentTypeEnumTest extends AbstractTest{

    @Test
    void getTitles() {
        PaymentTypeEnum.getTitles();
        PaymentTypeEnum.getTitles();
        Map<String, String> titles = PaymentTypeEnum.getTitles();
        assertEquals(4, titles.size());
    }
}