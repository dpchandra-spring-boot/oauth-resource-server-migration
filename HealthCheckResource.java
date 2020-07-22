/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saddlepoint.dataview;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dpurna
 */
@RestController
public class HealthCheckResource {

    @RequestMapping("/check")
    public String testCall() {
        try {
            return "SUCCESS.. Server is Running!";
        } catch (Exception e) {
            return e.toString();
        }
    }

    @RequestMapping("/token")
    public String getAuthDetails() {
        try {
            Authentication authentication
                    = SecurityContextHolder
                    .getContext()
                    .getAuthentication();
            if (authentication == null) {
                return "Not yet authenticated.";
            } else {

                OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();

                StringBuffer sb = new StringBuffer();
                sb.append("User : " + authentication.getPrincipal().toString());
                sb.append("\nAccess Token : " + details.getTokenValue());
                sb.append("\nToken Type : " + details.getTokenType());
                return sb.toString();
            }
        } catch (Exception e) {
            return e.toString();
        }
    }
}
