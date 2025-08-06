package com.SCM.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.SCM.Entities.User;
import com.SCM.Helper.validationHelper;
import com.SCM.Repository.userRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class mainController {

	@Autowired
	private BCryptPasswordEncoder passEncoder;

	@Autowired
	private userRepository userrepo;

	@Autowired
	private validationHelper validationhelper;

	// Home page Mapping

	@GetMapping("/")
	public String home(Model m) {
		m.addAttribute("title", "Home - Contact Manager System");
		return "home";
	}

	// About page mapping

	@GetMapping("/about")
	public String about(Model m) {
		m.addAttribute("title", "About - Contact Manager System");
		return "about";
	}

	// Signup page mapping

	@GetMapping("/signup")
	public String signup(Model m) {
		m.addAttribute("title", "Signup - Contact Manager System");

		if (!m.containsAttribute("user")) {
			m.addAttribute("user", new User());
		}

		return "signup";
	}

	// post mapping for posting data on DB

	@PostMapping("/signup")
	public String handleSignup(@Valid @ModelAttribute("user") User user, BindingResult result,
			@RequestParam(name = "cf-turnstile-response", required = true) String token, HttpServletRequest request,
			@RequestParam("uImgFile") MultipartFile imageFile, Model m, RedirectAttributes redirectAttrs) {
		boolean cresult = validationhelper.isValidCaptcha(token, request);

		user.setURole("ROLE_USER");
		user.setUEnable(true);
		user.setUPassword(passEncoder.encode(user.getUPassword()));

		try {

			if (imageFile.isEmpty()) {
				System.out.println("File is empty");

				user.setUImgName("userImage.jpeg");

			} else {

				// uploading file to the folder

				File file = new ClassPathResource("static/Images").getFile();

				String uniqueFilename = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();

				Path path = Paths.get(file.getAbsolutePath() + File.separator + uniqueFilename);

				user.setUImgName(uniqueFilename);

				Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				System.out.println("Image uploaded successfully");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (validationhelper.isInValid(user, result)) {
			m.addAttribute("user", user);
			System.out.println("Errors:- " + result.toString());
			return "signup";
		}

		if (!cresult) {
			redirectAttrs.addFlashAttribute("captchaError", "CAPTCHA failed! Try again.");
			redirectAttrs.addFlashAttribute("toastType", "error");
			redirectAttrs.addFlashAttribute("user", user);
			return "redirect:/signup";
		}

		User savedUser = this.userrepo.save(user);
		System.out.println("User :- " + savedUser);

		redirectAttrs.addFlashAttribute("toastType", "success");
		redirectAttrs.addFlashAttribute("captchaSuccess", "Signup Successful!!!");
		return "redirect:/login";
	}

	// login page mapping

	@GetMapping("/login")
	public String login(@RequestParam(value = "logout", required = false) String logout,
			@RequestParam(value = "error", required = false) String error,
			@ModelAttribute("captchaError") String captchaError, RedirectAttributes redirectAtt, Model m) {
		m.addAttribute("title", "Login - Contact Manager System");

		if (logout != null && logout.equals("true")) {
			m.addAttribute("logoutMessage", "You've been logged out!!!");
			m.addAttribute("toastType", "success");
			System.out.println("inside get mapping error if = " + logout);
		}

		if (error != null && error.equals("true")) {
			m.addAttribute("toastType", "error");
			m.addAttribute("loginError", "Invalid Credentials");
			System.out.println("DEBUG: loginError in model before rendering: '" + m.getAttribute("loginError") + "'");
			System.out.println(
					"Model attributes for login page: loginError='" + m.getAttribute("loginError") + "', captchaError='"
							+ m.getAttribute("captchaError") + "', toastType='" + m.getAttribute("toastType") + "'");
			System.out.println("inside get mapping error if = " + error);
		}

		if (captchaError != null && !captchaError.isEmpty()) {
			m.addAttribute("toastType", "error");
			m.addAttribute("captchaError", "Captcha verification Failed!!");
			System.out.println("inside get mapping error if = " + captchaError.toString());
		}

		return "/login";
	}

}
