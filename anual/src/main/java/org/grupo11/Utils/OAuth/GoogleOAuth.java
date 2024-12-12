package org.grupo11.Utils.OAuth;

import java.util.Collections;

import org.grupo11.Config.Env;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

public class GoogleOAuth {
    private static final HttpTransport transport = new NetHttpTransport();
    private static final JsonFactory jsonFactory = new GsonFactory();

    public static OAuthValidateResponse validate(String tokenId) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(Env.getGoogleOAuthClientId()))
                .build();
        try {
            GoogleIdToken idToken = verifier.verify(tokenId);

            if (idToken != null) {
                Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
                String name = (String) payload.get("name");
                return new OAuthValidateResponse(email, emailVerified, name);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
