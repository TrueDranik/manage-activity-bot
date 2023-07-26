package tg.bot.activity.mapper;

import org.mapstruct.Mapper;
import tg.bot.activity.model.dto.tg.ClientDto;
import tg.bot.activity.model.entity.Client;

@Mapper(componentModel = "spring")
public interface ClientMapper extends BaseMapper<Client, ClientDto> {
}
