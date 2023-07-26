package tg.bot.activity.service.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tg.bot.activity.common.enums.PaymentTypeEnum;
import tg.bot.activity.model.entity.Payment;
import tg.bot.activity.repository.PaymentRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public void save(Payment payment) {
        paymentRepository.save(payment);
    }

    @Override
    public Payment findById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment with id[%s] not found".formatted(paymentId)));
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment findByType(PaymentTypeEnum paymentType) {
        return paymentRepository.findByType(paymentType);
    }
}
