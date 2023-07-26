package tg.bot.activity.mapper;

import com.bot.sup.model.dto.tg.InstructorDto;
import com.bot.sup.model.entity.Instructor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InstructorMapper extends BaseMapper<Instructor, InstructorDto>{
}
