package com.src.speedreadingapp.security.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("userEndpointSecurity")
public class UserEndpointSecurity {
        public boolean hasUserId(Authentication authentication, Long userId) {
            String principalName = (String) authentication.getPrincipal();
            String supposedPrincipal = String.valueOf(userId);
            return principalName != null &&
                    principalName.equals(supposedPrincipal);
        }
}
