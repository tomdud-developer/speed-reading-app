package com.src.speedreadingapp.security.config;

import com.src.speedreadingapp.jpa.appuser.AppUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component("userEndpointSecurity")
public class UserEndpointSecurity {
        private final AppUserService appUserService;
        public boolean hasUserId(Authentication authentication, Long userId) {
            String principalName = (String) authentication.getPrincipal();
            Long id = appUserService.findByUsername(principalName).getId();
            String appUserID = String.valueOf(id);
            String supposedPrincipalId = String.valueOf(userId);
            boolean isAuthenticated = principalName != null && appUserID.equals(supposedPrincipalId);
            if(isAuthenticated)
                log.info("Checking user endpoint. Security alert: userId=" + userId + " is user from token");
            else
                log.info("Checking user endpoint. Security alert: userId=" + userId + " is not user from token");

            return isAuthenticated;
        }
}
