package tg.bot.activity.api.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tg.bot.activity.model.dto.tg.ActivityTypeDto;
import tg.bot.activity.service.activity.type.ActivityTypeService;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/activity_type")
@RequiredArgsConstructor
@Tag(name = "Тип активности")
public class ActivityTypeController {

    private final ActivityTypeService activityTypeService;

    @GetMapping
    @Operation(summary = "Получить список всех типов активности", method = "GET")
    public List<ActivityTypeDto> getAllActivityType() {
        return activityTypeService.getAllActivityType();
    }

    @GetMapping("/byFormat/{formatId}")
    @Operation(summary = "Получить список всех типов активности по id формата активности", method = "GET")
    public List<ActivityTypeDto> getAllActivityTypeByFormatId(@PathVariable(name = "formatId") Long formatId) {
        return activityTypeService.getAllActivityTypeByActivityFormatId(formatId);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Получить тип активности по id")
    public ActivityTypeDto getActivityTypeById(@PathVariable(name = "id") Long id) {
        return activityTypeService.getActivityTypeById(id);
    }

    @PostMapping
    @Operation(summary = "Создать новый тип активности")
    public ActivityTypeDto createActivityType(@RequestBody ActivityTypeDto activityTypeDto) {
        return activityTypeService.createActivityType(activityTypeDto);
    }

    @PutMapping("{id}")
    @Operation(summary = "Изменить существующий тип активности")
    public ActivityTypeDto updateActivityType(@PathVariable(name = "id") Long id,
                                              @RequestBody ActivityTypeDto activityTypeDto) {
        return activityTypeService.updateActivityType(id, activityTypeDto);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Удалить существующий тип активности")
    public void deleteActivityType(@PathVariable(name = "id") Long id) {
        activityTypeService.deleteActivityType(id);
    }
}
