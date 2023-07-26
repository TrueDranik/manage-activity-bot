package tg.bot.activity.mapper;

import com.bot.sup.model.dto.tg.ClientDto;
import com.bot.sup.model.entity.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper extends BaseMapper<Client, ClientDto>{
}
