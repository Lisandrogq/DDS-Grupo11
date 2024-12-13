package org.grupo11.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;

import org.grupo11.Logger;
import org.grupo11.Config.Env;

public class GithubAPI {
    public static final String baseUrl = "https://api.github.com";

    public static class GithubEmailResponseItem {
        public String email;
        public boolean primary;
        public String visibility;
        public boolean verified;
    }

    public static List<GithubEmailResponseItem> getEmails(String token) throws Exception {
        String json = Fetcher.getWithAuthorization(baseUrl + "/user/emails", "Bearer " + token)
                .body()
                .string();
        List<GithubEmailResponseItem> res = JSON.parse(json, new TypeReference<List<GithubEmailResponseItem>>() {
        });
        return res;
    }

    public static String getTokenFromCode(String code) {
        Map<String, String> body = new HashMap<>();
        body.put("client_id", Env.getGithubOAuthClientId());
        body.put("client_secret", Env.getGithubOAuthSecret());
        body.put("code", code);
        body.put("accept", "json");
        try {
            String response = Fetcher.post("https://github.com/login/oauth/access_token", JSON.stringify(body))
                    .body().string();
            Logger.info("RESPONSE {}", response);
            String token = extractAccessToken(response);
            return token;

        } catch (Exception e) {
            Logger.error("ERROR IN GET TOKEN", e);
            return null;
        }
    }

    public static String getPrimaryEmail(List<GithubEmailResponseItem> emails) {
        for (GithubEmailResponseItem emailItem : emails) {
            if (emailItem.primary)
                return emailItem.email;
        }
        return null;
    }

    static String extractAccessToken(String response) {
        String[] params = response.split("&");
        for (String param : params) {
            if (param.startsWith("access_token=")) {
                return param.split("=")[1];
            }
        }
        return null;
    }

}
