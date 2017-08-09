package com.oauth2client.controller;


import javax.servlet.http.HttpSession;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oauth2client.consts.ConfigConsts;

@Controller
public class HomeController {
	
	public String token = null;
	@RequestMapping("/index")
	public String index(Model model) {
		model.addAttribute("token", token);
		return "index";
	}
	
	@RequestMapping("/callback")
	public String callback(String code, HttpSession session) throws OAuthSystemException, OAuthProblemException {
		if (!StringUtils.isEmpty(code)) {
			OAuthClientRequest request = OAuthClientRequest
	                .tokenLocation(ConfigConsts.REQUEST_ACCESSTOKEN_URI)
	                .setClientId(ConfigConsts.CLIENT_ID)
	                .setClientSecret(ConfigConsts.CLIENT_SECRET)
	                .setGrantType(GrantType.AUTHORIZATION_CODE)
	                .setCode(code)
	                .setRedirectURI(ConfigConsts.REDIRECT_URI)
	                .buildBodyMessage();

	        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

	        OAuthAccessTokenResponse oauthResponse = oAuthClient.accessToken(request);
	        token = oauthResponse.getAccessToken();
		}
		session.setAttribute("user", token);
		return "redirect:/index";
	}
}
