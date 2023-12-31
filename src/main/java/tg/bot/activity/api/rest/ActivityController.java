package tg.bot.activity.api.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
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
import tg.bot.activity.model.ActivityRequestParams;
import tg.bot.activity.model.dto.tg.ActivityDto;
import tg.bot.activity.service.activity.ActivityService;

import java.util.List;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController("activityController")
@RequestMapping(value = "/activity")
@Tag(name = "Активность")
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    @Operation(summary = "Получить список всех активностей")
    public List<ActivityDto> getAllActivity(@ParameterObject ActivityRequestParams params) {
        return activityService.getAllActivity(params);
    }

    @GetMapping("{id}")
    @Operation(summary = "Получить активность по id")
    public ActivityDto getActivityById(@Parameter(description = "Id") @PathVariable(name = "id") Long id) {
        return activityService.getActivityById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать новую активность")
    public ActivityDto createActivity(@RequestBody ActivityDto activityDto) {
        return activityService.createActivity(activityDto);
    }

    @PutMapping("{id}")
    @Operation(summary = "Изменить существующую активность")
    public ActivityDto updateActivity(@PathVariable(name = "id") Long id,
                                      @RequestBody ActivityDto activityDto) {
        return activityService.updateActivity(id, activityDto);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Изменить активность")
    public void deleteActivity(@PathVariable(name = "id") Long id) {
        activityService.deleteActivity(id);
    }
}
