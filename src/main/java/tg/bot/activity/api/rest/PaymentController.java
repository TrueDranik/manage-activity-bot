package tg.bot.activity.api.rest;

import com.bot.sup.common.enums.PaymentTypeEnum;
import com.bot.sup.mapper.PaymentMapper;
import com.bot.sup.model.dto.PaymentDto;
import com.bot.sup.model.dto.PaymentUpdateDto;
import com.bot.sup.model.entity.Payment;
import com.bot.sup.model.entity.Payment_;
import com.bot.sup.service.payment.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/payments")
@Tag(name = "Типы оплаты")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @GetMapping("/type")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> getPaymentType() {
        return PaymentTypeEnum.getTitles();
    }

    @GetMapping("/{paymentType}")
    @ResponseStatus(HttpStatus.OK)
    public PaymentDto getPaymentByType(@PathVariable String paymentType) {
        Payment payment = paymentService.findByType(PaymentTypeEnum.convertToEnum(paymentType));

        return paymentMapper.domainToDto(payment);
    }

    @PutMapping("/{paymentId}")
    @ResponseStatus(HttpStatus.OK)
    public PaymentDto updatePayment(@PathVariable Long paymentId, @RequestBody PaymentUpdateDto paymentUpdateDto) {
        Payment paymentFromDb = paymentService.findById(paymentId);
        Payment paymentForUpdate = paymentMapper.dtoToDomain(paymentUpdateDto);

        BeanUtils.copyProperties(paymentForUpdate, paymentFromDb, Payment_.ID, Payment_.TYPE);
        paymentService.save(paymentFromDb);

        return paymentMapper.domainToDto(paymentFromDb);
    }
}
