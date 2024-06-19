package it.aboutbits;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RealmProvider;
import org.keycloak.models.UserModel;
import org.json.JSONObject;

public class MyEventListenerProvider implements EventListenerProvider {

    private static final Logger log = Logger.getLogger(MyEventListenerProvider.class);

    private final KeycloakSession session;
    private final RealmProvider model;

    public MyEventListenerProvider(KeycloakSession session) {
        this.session = session;
        this.model = session.realms();
    }

    @Override
    public void onEvent(Event event) {
        // Check if the event type is registration
        if (event.getType() == EventType.REGISTER) {
            // Obtain the realm and user model based on the event
            RealmModel realm = this.model.getRealm(event.getRealmId());
            UserModel newRegisteredUser = this.session.users().getUserById(realm, event.getUserId());
    
            // Prepare the JSON payload to include only email, first name, and last name
            String email = newRegisteredUser.getEmail();
            String firstName = newRegisteredUser.getFirstName();
            String lastName = newRegisteredUser.getLastName();
            String json = String.format(
                "{\"email\":\"%s\", \"firstname\":\"%s\", \"lastname\":\"%s\"}",
                email, firstName, lastName);
    
            // Log the JSON for debugging
            System.out.println("Webhook POST Response Code start: " + json);
    
            // Send the JSON payload to the webhook URL
            sendWebhook("webhook", json);
        } else {
            // Log or handle other types of events if necessary
            System.out.println("Ignored Event Type: " + event.getType());
        }
    }
        

    private void sendWebhook(String webhookUrl, String json) {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(webhookUrl);

            // Set up the request headers
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Accept", "application/json");

            // Set up the request body
            StringEntity entity = new StringEntity(json.toString());
            // System.out.println("json" + entity);

            post.setEntity(entity);
            System.out.println("Webhook POST Response Code start: " + json);

            // Execute the request
            HttpResponse response = client.execute(post);
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");

            log.info("Response: " + responseString);
        } catch (Exception e) {
            log.error("Error sending webhook", e);
        }

    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {

    }

    @Override
    public void close() {

    }
}
