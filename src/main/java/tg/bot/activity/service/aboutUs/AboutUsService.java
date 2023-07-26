package tg.bot.activity.service.aboutUs;

import org.springframework.stereotype.Service;
import tg.bot.activity.model.dto.AboutUsDto;
import tg.bot.activity.model.dto.AboutUsUpdateDto;
import tg.bot.activity.model.entity.AboutUs;

import java.util.Optional;

@Service
public interface AboutUsService {

    void save(AboutUs aboutUs);

    Optional<AboutUs> findById(Long aboutUsId);

    AboutUsDto getAboutUs();

    AboutUsDto updateInfo(AboutUsUpdateDto dto);


}
