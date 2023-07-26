package tg.bot.activity.util;

import com.bot.sup.model.dto.vk.VkCommentDto;
import com.bot.sup.model.dto.vk.VkUserInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(value = "vkService", url = "${integration.url.vkService}")
public interface FeignVkServiceUtil {
    @GetMapping("/comments/status")
    Map<String, String> getCommentStatus();

    @GetMapping("/comments/{commentStatus}")
    List<VkCommentDto> getCommentsByStatus(@PathVariable("commentStatus") String commentStatus, Pageable pageable);

    @PutMapping("/comments/{commentId}")
    void updateCommentStatus(@PathVariable Long commentId, @RequestParam String commentStatus);

    @GetMapping("users/{userId}")
    VkUserInfoDto getUser(@PathVariable Long userId);
}
