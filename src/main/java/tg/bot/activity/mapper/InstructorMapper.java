package tg.bot.activity.mapper;

import org.mapstruct.Mapper;
import tg.bot.activity.model.dto.tg.InstructorDto;
import tg.bot.activity.model.entity.Instructor;

@Mapper(componentModel = "spring")
public interface InstructorMapper extends BaseMapper<Instructor, InstructorDto> {
}
