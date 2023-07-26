package tg.bot.activity.service.aboutUs;

import com.bot.sup.model.dto.AboutUsDto;
import com.bot.sup.model.dto.AboutUsUpdateDto;
import com.bot.sup.model.entity.AboutUs;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface AboutUsService {
    void save(AboutUs aboutUs);
    Optional<AboutUs> findById(Long aboutUsId);
    AboutUsDto getAboutUs();

    AboutUsDto updateInfo(AboutUsUpdateDto dto);


}
