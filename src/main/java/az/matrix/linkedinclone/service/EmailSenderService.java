package az.matrix.linkedinclone.service;

import az.matrix.linkedinclone.dto.request.EmailRequest;
import az.matrix.linkedinclone.enums.EmailTemplate;

import java.util.Map;

public interface EmailSenderService {
//    void sendEmail(EmailRequest email);

    void sendEmail(String receiver, EmailTemplate template, Map<String, String> placeholders);
}
