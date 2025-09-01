package com.SCM.Controller;

import java.security.Principal;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.SCM.Entities.User;
import com.SCM.Repository.userRepository;
import com.SCM.Services.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/changePass")
public class forgotPassController {

	@Autowired
	private BCryptPasswordEncoder passEncoder;
	
	@Autowired
	private EmailService emailService;
	
	
	@Autowired
	private userRepository userrepo;

	@GetMapping("/forgotPass")
	public String forgotPassForm() {
		
		return "forgotPass";
	}
	
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email,RedirectAttributes redirectAttrs, HttpSession session) {
		
		System.out.println("Email from forgot pass :- " + email);
		
		Random random = new Random();
		int otp = 100000 + random.nextInt(899999);
		
		System.out.println(otp);

	    session.setAttribute("sOTP", otp);
	    session.setAttribute("userEmail", email);
		
		boolean userExists = userrepo.existsByuEmail(email);
		
		if(userExists){
		
		String subject = "OTP from SCM" ;
		
		String message = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }"
                + ".container { max-width: 600px; margin: 20px auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }"
                + ".header { text-align: center; padding-bottom: 20px; border-bottom: 1px solid #eeeeee; }"
                + ".content { padding: 20px 0; text-align: center; }"
                + ".otp-code { font-size: 32px; font-weight: bold; color: #007bff; margin: 20px 0; padding: 10px; background-color: #e9f5ff; border-radius: 5px; display: inline-block; }"
                + ".footer { text-align: center; padding-top: 20px; border-top: 1px solid #eeeeee; font-size: 12px; color: #888888; }"
                + ".warning { color: #dc3545; font-weight: bold; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='header'>"
                + "<h2>Verification Required</h2>"
                + "</div>"
                + "<div class='content'>"
                + "<p>Hello,</p>"
                + "<p>You have requested to forgot password. Please use the following One-Time Password (OTP) to complete your request:</p>"
                + "<h1 class='otp-code'>"+otp+"</h1>"
                + "<p>This OTP is valid for a short period of time. For your security, please do not share this code with anyone.</p>"
                + "</div>"
                + "<div class='footer'>"
                + "<p class='warning'>Do not reply to this email.</p>"
                + "<p>If you did not request this OTP, please ignore this email.</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
		
		
		String to = email;
		
		boolean result = emailService.sendEmail(subject, message, to);
		System.out.println(result);
		
		}else{
			
			// currently not working (POP-UP)
			session.setAttribute("email", "Invalid Email");
			session.setAttribute("toastType", "error");
			
			return "redirect:/changePass/forgotPass";
			
		}
		return "sendOTP";
	}
	
	
	@PostMapping("/otp-verify")
	public String verify(@RequestParam("otp") int otp ,HttpSession session) {
		
		int sOTP =(int) session.getAttribute("sOTP");
		
		if( sOTP != otp ) {
			
			session.setAttribute("email", "Invalid OTP");
			session.setAttribute("toastType", "error");
			
			return "redirect:/changePass/forgotPass";
		}
		
		return "resetPass";

	}
	
	
	@PostMapping("/set-password")
	public String setNewPass(@RequestParam("newPassword") String newPass, @RequestParam("confirmPassword") String confirm
			,Principal principal, RedirectAttributes redirectAttrs, HttpSession session) {
		

		System.out.println("New password of user is :- "+newPass);
		System.out.println("New password of user is :- "+confirm);
		
		String userEmail = (String) session.getAttribute("userEmail");
		
		User currentUser = userrepo.getUserByUserName(userEmail);
		 
		System.out.println("current user for changing password :- " + currentUser);
		 
		if( !newPass.equals(confirm) ) {
			redirectAttrs.addFlashAttribute("message", "Passwords do not match!");
	        redirectAttrs.addFlashAttribute("type", "error"); 
	        
	        return "redirect:/changePass/set-password";
	        
		}else{
			
			String updatedPass = passEncoder.encode(newPass);
						
			System.out.println("PASSWORD UPDATED SUCCESSFULLY updated password :- "+updatedPass);
			
			currentUser.setUPassword(updatedPass);
			
			userrepo.save(currentUser);
			
			redirectAttrs.addFlashAttribute("message", "Password has been updated successfully!");
		    redirectAttrs.addFlashAttribute("type", "success");

		}
		
		
		
		return "redirect:/login?PasswordChanged=true";
	}
	
	
	
	
}
