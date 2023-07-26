package tg.bot.activity.api.rest;

import com.bot.sup.model.dto.tg.InstructorDto;
import com.bot.sup.service.instructor.InstructorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/instructor")
@Tag(name = "Инструктор")
public class InstructorController {
    private final InstructorService instructorService;

    @GetMapping()
    @Operation(summary = "Получить всех инструткоров")
    @ResponseStatus(HttpStatus.OK)
    public List<InstructorDto> getAllInstructor() {
        return instructorService.findAllDtos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить инструткора по id")
    @ResponseStatus(HttpStatus.OK)
    public InstructorDto getById(@PathVariable(name = "id") Long id) {
        return instructorService.findById(id);
    }
}