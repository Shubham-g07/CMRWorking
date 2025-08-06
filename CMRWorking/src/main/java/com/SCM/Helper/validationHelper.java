package com.SCM.Helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.SCM.Entities.User;
import com.SCM.Services.CaptchaValidator;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class validationHelper {

	@Autowired
	CaptchaValidator captchaValidator;
	
	
	
	public boolean isInValid(User user, BindingResult result) {
		if(result.hasErrors()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	public boolean isValidCaptcha(String token,HttpServletRequest request) {
	
		String rip = request.getRemoteAddr();
		boolean captchaResult = false;
		
		try {
			captchaResult = captchaValidator.verifyCaptcha(token, rip);
			return captchaResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return captchaResult;
		
	}
		
}
