package cdt.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import cdt.entities.Poll;
import cdt.entities.PollCredential;

@Service
public class EmailService extends BaseService {
	
	@Autowired
	private SendGrid sg;
	
	@Autowired
	protected Environment env;
	
	public void sendPollsNotifications(List<Poll> polls) throws IOException {
		
		/* segment all notifications into subarrays of those of the same 
		 * activity type, one email with multiple personalizations per activity type */
		for (Poll poll : polls) {
			try {
				if (!poll.getConfig().getNotificationsSent()) {
					sendPollNotifications(poll);
				}
			} catch (Exception ex) {
				poll.getConfig().setNotificationsSent(true);
				pollConfigRepository.save(poll.getConfig());
				System.out.println("Error sending notifications");
			}
			
			poll.getConfig().setNotificationsSent(true);
			pollConfigRepository.save(poll.getConfig());
		}
	}
	
	private void sendPollNotifications(Poll poll) throws IOException {
		if(env.getProperty("cdt.webapp.send-email-enabled").equalsIgnoreCase("true")) {
				
			Request request = new Request();
			Mail mail = preparePollEmail(poll);
			
			if (mail != null) {
				try {
					request.method = Method.POST;
					request.endpoint = "mail/send";
					request.body = mail.build();
					
					Response response = sg.api(request);
					
					if(response.statusCode == 202) {
						System.out.println("emails sent!");
					} else {
						System.out.println(response.body);
					}
					
				} catch (IOException ex) {
					throw ex;
				}
			}
		}
	}
	
	private Mail preparePollEmail(Poll poll) {
		Mail mail = new Mail();
		
		Email fromEmail = new Email();
		fromEmail.setName(env.getProperty("cdt.webapp.from-mail-name"));
		fromEmail.setEmail(env.getProperty("cdt.webapp.from-mail"));
		mail.setFrom(fromEmail);
		mail.setSubject("Invitation to fill a survey from " + poll.getOrganization().getName());
	
		for(PollCredential credential : poll.getCredentials()) {
			
			Personalization personalization = new Personalization();
			
			Email toEmail = new Email();
			toEmail.setEmail(credential.getMember().getEmail());
			
			String pollUrl = env.getProperty("cdt.webapp.baseurl") +"/#/app/answer/" + poll.getId().toString() + "?secret=" + credential.getSecret();
			
			personalization.addTo(toEmail);
			personalization.addSubstitution("$ORGANIZATION_NAME$", poll.getOrganization().getName());
			personalization.addSubstitution("$POLL_TITLE$", poll.getTitle());
			personalization.addSubstitution("$POLL_DESCRIPTION$", poll.getDescription());
			personalization.addSubstitution("$POLL_ANSWER_URL$", pollUrl);
			
			mail.addPersonalization(personalization);
		}
		
		mail.setTemplateId(env.getProperty("cdt.webapp.initiative-activity-template"));
		
		return mail;
	}
}
