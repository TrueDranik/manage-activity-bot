package tg.bot.activity.util;

import com.bot.sup.api.telegram.handler.registration.MessageProcessor;
import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@UtilityClass
public class MessageProcessorUtil {
    public static BotApiMethod<?> messageProcessorCheck(MessageProcessor messageProcessor, Message message, Long chatId, Object entity) {
        if (messageProcessor.isMessageInvalid(message)) {
            return messageProcessor.processInvalidInputMessage(chatId);
        }

        try {
            return messageProcessor.processInputMessage(message, entity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
