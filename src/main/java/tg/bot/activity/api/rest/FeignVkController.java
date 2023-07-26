package tg.bot.activity.api.rest;

import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tg.bot.activity.model.dto.vk.VkCommentDto;
import tg.bot.activity.model.dto.vk.VkUserInfoDto;
import tg.bot.activity.util.FeignVkServiceUtil;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "vk-service")
public class FeignVkController {

    private final FeignVkServiceUtil feignVkServiceUtil;

    /**
     * Возвращает статусы оплаты (константы).
     */
    @GetMapping("comments/status")
    public Map<String, String> getCommentStatus() {
        return feignVkServiceUtil.getCommentStatus();
    }

    /**
     * Вовзращает список комментариев согласно переданному статусу.
     */
    @GetMapping("comments/{commentStatus}")
    public Page<VkCommentDto> getCommentsByStatus(@PathVariable("commentStatus") String commentStatus,
                                                  @ParameterObject Pageable pageable) {
        List<VkCommentDto> commentsByStatus = feignVkServiceUtil.getCommentsByStatus(commentStatus, pageable);
        int total = commentsByStatus.size();

        return new PageImpl<>(commentsByStatus, pageable, total);
    }

    /**
     * Обновить статус комментария
     */
    @PutMapping("comments/{commentId}")
    public void updateCommentStatus(@PathVariable Long commentId, @RequestParam String commentStatus) {
        feignVkServiceUtil.updateCommentStatus(commentId, commentStatus);
    }

    /**
     * Получить User по Id
     */
    @GetMapping("users/{userId}")
    public VkUserInfoDto getUser(@PathVariable Long userId) {
        return feignVkServiceUtil.getUser(userId);
    }
}
