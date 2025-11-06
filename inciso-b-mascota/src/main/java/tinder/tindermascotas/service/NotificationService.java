package tinder.tindermascotas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void send(String body, String title, String mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@tinder-mascotas.com");
        message.setTo(mail);
        message.setSubject(title);
        message.setText(body);

        mailSender.send(message);
    }
}
