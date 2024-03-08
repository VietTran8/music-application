package vn.edu.tdtu.musicapplication.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import vn.edu.tdtu.musicapplication.dtos.MailDetails;
import vn.edu.tdtu.musicapplication.enums.EProductType;
import vn.edu.tdtu.musicapplication.models.Bill;
import vn.edu.tdtu.musicapplication.repository.BillRepository;
import vn.edu.tdtu.musicapplication.service.mail.MailService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class BillService {
    private final BillRepository billRepository;
    private final Configuration config;
    private final MailService mailService;

    public Bill saveBill(Bill bill){
        return billRepository.save(bill);
    }

    public void sendBillToUser(String subject, Bill bill){
        MailDetails mailDetails = new MailDetails();
        mailDetails.setSubject(subject.toUpperCase());
        mailDetails.setSendTo(bill.getUser().getEmail());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        Map<String, Object> model = new HashMap<>();
        model.put("billNumber", bill.getId());
        model.put("name", bill.getUser().getUsername());
        model.put("userEmail", bill.getUser().getEmail());
        model.put("createDate", bill.getCreatedDate().format(formatter));
        model.put("paymentMethod", bill.getPaymentMethod());
        model.put("transactionId", bill.getTransactionId());
        model.put("productType", bill.getProductType().getDescription());
        model.put("amount", bill.getAmount());
        if(bill.getProductType() == EProductType.ADS) {
            model.put("productName", bill.getAdProduct().getAPackage().getName());

            LocalDateTime beginDate = bill.getAdProduct().getBoughtDate(),
                    expDate = bill.getAdProduct().getExpirationDate();

            model.put("beginDate", beginDate.format(formatter));
            model.put("expDate", expDate.format(formatter));
        }else{
            model.put("productName", bill.getPackageProduct().getMPackage().getName());

            LocalDateTime beginDate = bill.getPackageProduct().getBoughtDate(),
                    expDate = bill.getPackageProduct().getExpirationDate();

            model.put("beginDate", beginDate.format(formatter));
            model.put("expDate", expDate.format(formatter));
        }

        try{
            Template t = config.getTemplate("bill.html");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            mailDetails.setText(html);

            mailService.sendMail(mailDetails);
        }catch (Exception e){
            log.error(e.getMessage());
        }

    }
}
