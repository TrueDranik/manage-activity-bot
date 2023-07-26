package tg.bot.activity.service.instructor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tg.bot.activity.mapper.InstructorMapper;
import tg.bot.activity.model.dto.tg.InstructorDto;
import tg.bot.activity.model.entity.Instructor;
import tg.bot.activity.repository.InstructorRepository;
import tg.bot.activity.service.files.photo.PhotoService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final InstructorMapper instructorMapper;
    private final PhotoService photoService;

    public void save(Instructor instructor) {
        instructorRepository.save(instructor);
    }

    public Instructor findInstructorsById(Long instructorId) {
        return instructorRepository.findById(instructorId)
                .orElseThrow(() -> new EntityNotFoundException("Instructor with Id [%s] not found".formatted(instructorId)));
    }

    public List<Instructor> findAll() {
        return instructorRepository.findAll();
    }

    public InstructorDto findById(Long id) {
        return instructorMapper.domainToDto(findInstructorsById(id));
    }

    public List<InstructorDto> findAllDtos() {
        List<Instructor> instructors = findAll();
        return instructorMapper.domainsToDtos(instructors);
    }

    public boolean existsByTelegramId(Long telegramId) {
        return instructorRepository.existsByTelegramId(telegramId);
    }

    @Transactional
    public void deleteInstructor(Instructor instructor) {
        long idPhotoToDelete = instructor.getPhoto().getId();
        instructor.setPhoto(null);
        instructorRepository.save(instructor);
        photoService.deletePhotoById(idPhotoToDelete);
        instructorRepository.delete(instructor);
    }
}
