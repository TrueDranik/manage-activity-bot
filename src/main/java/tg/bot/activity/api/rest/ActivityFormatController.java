package tg.bot.activity.api.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tg.bot.activity.model.dto.tg.ActivityFormatDto;
import tg.bot.activity.service.activity.format.ActivityFormatService;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/activity_format")
@RequiredArgsConstructor
@Tag(name = "Формат активности")
public class ActivityFormatController {

    private final ActivityFormatService activityFormatService;

    @GetMapping
    @Operation(summary = "Получить список всех форматов активности")
    public List<ActivityFormatDto> getAllActivityFormat() {
        return activityFormatService.getAllActivityFormat();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить формат активности по id")
    public ActivityFormatDto getActivityFormatById(@PathVariable(name = "id") Long id) {
        return activityFormatService.getActivityFormatById(id);
    }

    @PostMapping
    @Operation(summary = "Создать новый формат активности")
    public ActivityFormatDto createActivityFormat(@RequestBody ActivityFormatDto activityFormatDto) {
        return activityFormatService.createActivityFormat(activityFormatDto);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Изменить существующий формат активности")
    public ActivityFormatDto updateActivityFormat(@PathVariable(name = "id") Long id,
                                                  @RequestBody ActivityFormatDto activityFormatDto) {
        return activityFormatService.updateActivityFormat(id, activityFormatDto);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Удалить существующий формат активности")
    public void deleteActivityFormat(@PathVariable(name = "id") Long id) {
        activityFormatService.deleteActivityFormat(id);
    }
}
