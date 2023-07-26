package tg.bot.activity.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum CommentStatusEnum {

    NOT_VIEWED("NOT_VIEWED", "Не просмотрен"),
    BLOCKED("BLOCKED", "Заблокирован"),
    PUBLISHED("PUBLISHED", "Опубликован");

    private final String titleEng;
    private final String titleRus;

    private static final Map<String, String> titles;

    static {
        titles = Arrays.stream(CommentStatusEnum.values())
                .collect(Collectors.toMap(CommentStatusEnum::getTitleEng, CommentStatusEnum::getTitleRus));
    }

    public static Map<String, String> getTitles() {
        return titles;
    }

    public static CommentStatusEnum convertToEnum(String titleEng) {
        return Stream.of(CommentStatusEnum.values())
                .filter(comment -> comment.getTitleEng().equals(titleEng))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
