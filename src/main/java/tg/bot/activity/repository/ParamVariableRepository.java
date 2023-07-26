package tg.bot.activity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.bot.activity.model.entity.ParamVariable;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParamVariableRepository extends JpaRepository<ParamVariable, Long> {

    Optional<ParamVariable> getParamVariableById(Long id);
    List<ParamVariable> findAllByParamType(String paramType);

}
