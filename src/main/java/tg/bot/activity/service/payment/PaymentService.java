package tg.bot.activity.service.payment;

import com.bot.sup.common.enums.PaymentTypeEnum;
import com.bot.sup.model.entity.Payment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentService {
    void save(Payment payment);

    Payment findById(Long paymentId);

    List<Payment> findAll();

    Payment findByType(PaymentTypeEnum paymentType);
}
