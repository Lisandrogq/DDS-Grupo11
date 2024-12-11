package org.grupo11.Utils;

import java.io.IOException;

import org.grupo11.Logger;
import org.grupo11.Config.Env;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.helpers.mail.*;
import com.sendgrid.helpers.mail.objects.*;

public class SendGrid {
    public static Response SendMail(String fromMail, String toMail, String subject, String message) {
        Email from = new Email(fromMail);
        Email to = new Email(toMail);
        Content content = new Content("text/plain", message);

        Mail mail = new Mail(from, subject, to, content);

        com.sendgrid.SendGrid sg = new com.sendgrid.SendGrid(Env.getSendGridApiKey());
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            return response;
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.error("Error while sending mail", ex);
            return null;
        }
    }
}
