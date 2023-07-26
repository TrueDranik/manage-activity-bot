package tg.bot.activity.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@Getter
@RequiredArgsConstructor
public enum ParamTypeEnum {

    ABOUTUS("aboutUs");

    private final String title;

    private static final Map<ParamTypeEnum, String> titles = new HashMap<>(ParamTypeEnum.values().length);

     static  {
        for (ParamTypeEnum value : ParamTypeEnum.values()) {
            titles.put(value, value.getTitle());
        }

    }
    public static Map<ParamTypeEnum, String> getTitles() {
         return titles;
    }


}
