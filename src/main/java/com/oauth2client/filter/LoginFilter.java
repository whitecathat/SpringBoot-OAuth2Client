package com.oauth2client.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.oauth2client.consts.ConfigConsts;

@Configuration
public class LoginFilter extends WebMvcConfigurerAdapter {

	public final static String SESSION_KEY = "user";
	
    @Bean
    public LoginInterceptor getSecurityInterceptor() {
        return new LoginInterceptor();
    }
    
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        addInterceptor.excludePathPatterns("/callback");

        addInterceptor.addPathPatterns("/**");
    }
    
    private class LoginInterceptor extends HandlerInterceptorAdapter {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {
            HttpSession session = request.getSession();
            if (session.getAttribute(SESSION_KEY) != null) {
            	return true;
            }

            String url = ConfigConsts.REQUEST_AUTHORIZE_URI + "?" 
            				+ "client_id=" + ConfigConsts.CLIENT_ID 
            				+ "&redirect_uri=" + ConfigConsts.REDIRECT_URI
            				+ "&response_type=" + ResponseType.CODE.toString();
            System.out.println(url);
            response.sendRedirect(url);
            return false;
        }
    }
}
