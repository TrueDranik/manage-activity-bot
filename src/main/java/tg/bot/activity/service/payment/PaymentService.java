package tg.bot.activity.service.payment;

import org.springframework.stereotype.Service;
import tg.bot.activity.common.enums.PaymentTypeEnum;
import tg.bot.activity.model.entity.Payment;

import java.util.List;

@Service
public interface PaymentService {

    void save(Payment payment);

    Payment findById(Long paymentId);

    List<Payment> findAll();

    Payment findByType(PaymentTypeEnum paymentType);
}
