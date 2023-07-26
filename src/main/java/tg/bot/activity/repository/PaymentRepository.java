package tg.bot.activity.repository;

import com.bot.sup.common.enums.PaymentTypeEnum;
import com.bot.sup.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByType(PaymentTypeEnum paymentType);
}
