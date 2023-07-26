package tg.bot.activity.api.rest;

import com.bot.sup.model.dto.AboutUsDto;
import com.bot.sup.model.dto.AboutUsUpdateDto;
import com.bot.sup.service.aboutUs.AboutUsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController("aboutUsController")
@RequestMapping(value = "/aboutUs")
@Tag(name = "О нас")
public class AboutUsController {

    private final AboutUsService aboutUsService;

    @GetMapping
    @Operation(summary = "Получить информацию О нас")
    @ResponseStatus(HttpStatus.OK)
    public AboutUsDto getAboutUsInfo() {
        return aboutUsService.getAboutUs();

    }

    @PutMapping
    @Operation(summary = "Обновить информацию О нас")
    @ResponseStatus(HttpStatus.CREATED)
    public AboutUsDto updateAboutUs(@RequestBody AboutUsUpdateDto updateDto) {

        return aboutUsService.updateInfo(updateDto);
    }

}
