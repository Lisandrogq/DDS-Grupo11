package org.grupo11.Enums;

import org.grupo11.Utils.OAuth.GoogleOAuth;
import org.grupo11.Utils.OAuth.OAuthValidateResponse;

public enum AuthProviders {
    Google {
        @Override
        public OAuthValidateResponse validateToken(String token) {
            return GoogleOAuth.validate(token);
        };
    },
    Github {
        @Override
        public OAuthValidateResponse validateToken(String token) {
            return null;
        };
    };

    public abstract OAuthValidateResponse validateToken(String token);
}
