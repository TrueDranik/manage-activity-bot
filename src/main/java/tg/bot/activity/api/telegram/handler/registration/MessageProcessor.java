package tg.bot.activity.api.telegram.handler.registration;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface MessageProcessor {

    BotApiMethod<?> processInputMessage(Message message, Object entity) throws IOException, ExecutionException, InterruptedException;

    BotApiMethod<?> processInvalidInputMessage(Long chatId);

    boolean isMessageInvalid(Message message);
}
