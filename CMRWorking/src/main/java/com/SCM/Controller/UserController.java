package com.SCM.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.SCM.Entities.Contacts;
import com.SCM.Entities.User;
import com.SCM.Repository.contactRepository;
import com.SCM.Repository.userRepository;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	userRepository userrepo;

	@Autowired
	contactRepository contactrepo;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	// a handler for the common data to put in the model so every time we don't have
	// to do the same thing
	@ModelAttribute
	public void commonData(Model m, Principal principal) {

		String name = principal.getName();
		System.out.println("UserName = " + name);
		User userByName = userrepo.getUserByUserName(name);
		m.addAttribute("user", userByName);

	}

	@GetMapping("/index")
	public String dashboard(@RequestParam(value = "loginSuccess", required = false) String success, Model m,
			Principal principal) {
		if (success != null) {
			m.addAttribute("toastType", "success");
			m.addAttribute("loginSuccess", "Login Successful!!!");
		}

		m.addAttribute("title", "User Dashboard");
		
		
		String name = principal.getName();
		
		User user = userrepo.getUserByUserName(name);
		
		Set<Contacts> contact = user.getContact();
		
		m.addAttribute("user", user);
		
		long favContact = 0;
		
		if( user != null && user.getContact() != null) {
			favContact = user.getContact().stream()
						.filter(c->c.isCIsFav())
						.count();
		}
		
		m.addAttribute("favContact", favContact);
		m.addAttribute("contact", contact);
		
		

		return "normal/user_dashboard";
	}

	@GetMapping("/profile")
	public String profile(Model m, Principal principal) {

		m.addAttribute("title", "Profile - Smart Contact Manager");

		String name = principal.getName();

		User user = userrepo.getUserByUserName(name);

		m.addAttribute("user", user);

		return "normal/profile";
	}

	@GetMapping("/addContact")
	public String addContact(Model m) {

		m.addAttribute("title", "Add Contact - Smart Contact Manager");
		m.addAttribute("contact", new Contacts());

		return "normal/addContact";
	}

	// add contact controller

	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contacts contact, Model m, Principal principal,
			@RequestParam("cImgFile") MultipartFile imgfile) {

		try {
			String name = principal.getName();

			User user = userrepo.getUserByUserName(name);

			contact.setUser(user);

			// file processing and uploading

			if (imgfile.isEmpty()) {
				System.out.println("File is empty");

				contact.setCImgName("contact.png");

			} else {

				// uploading file to the folder

				File file = new ClassPathResource("static/Images").getFile();

				String uniqueFilename = System.currentTimeMillis() + "_" + imgfile.getOriginalFilename();

				Path path = Paths.get(file.getAbsolutePath() + File.separator + uniqueFilename);

				contact.setCImgName(uniqueFilename);

				Files.copy(imgfile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				System.out.println("Image uploaded successfully");

			}

			user.getContact().add(contact);

			userrepo.save(user);

			System.out.println(user);

			user.getContact().forEach(c -> {
				System.out.println(c.getCName());
			});

			m.addAttribute("contact", new Contacts());

			m.addAttribute("contactResponse", "Contact Added Successfully");
			m.addAttribute("toastType", "success");

		} catch (Exception e) {
			e.printStackTrace();

			m.addAttribute("contactResponse", "Something Went Wrong!!!");
			m.addAttribute("toastType", "error");

		}

		return "normal/addContact";
	}

	@GetMapping("/showContacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model m, Principal principal,
			@ModelAttribute("user") User user) {
		m.addAttribute("title", "Add Contacts");

		Pageable pageable = PageRequest.of(page, 3);

		Page<Contacts> contactsByUser = this.contactrepo.findContactsByUser(user.getUId(), pageable);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", contactsByUser.getTotalPages());
		m.addAttribute("userContacts", contactsByUser);

		return "normal/show_contacts";
	}

	@GetMapping("/contactProfile/{index}")
	public String contactProfile(@PathVariable Integer index, Model m, Principal principal, @ModelAttribute User user) {

		Contacts contact = this.contactrepo.findById(index).get();

		if (contact.getUser().getUId() == user.getUId()) {
			m.addAttribute("contact", contact);
		}

		return "normal/contactProfile";
	}

	@GetMapping("/deleteContact/{index}")
	public String deleteContact(@PathVariable Integer index, @ModelAttribute User user,
			RedirectAttributes redirectAttrs) {

		Contacts contact = contactrepo.findById(index).get();

		if (contact.getUser().getUId() == user.getUId()) {
			contactrepo.delete(contact);
			redirectAttrs.addFlashAttribute("deletedMsg", "Contact Deleted!!!");
			redirectAttrs.addFlashAttribute("toastType", "success");
		} else {
			redirectAttrs.addFlashAttribute("deletedMsg", "Access denied!!!");
			redirectAttrs.addFlashAttribute("toastType", "error");
		}

		return "redirect:/user/showContacts/0";

	}

	@PostMapping("/updateContact/{index}")
	public String updateContact(@PathVariable Integer index, Model m) {

		m.addAttribute("title", "Update Contact");

		Contacts contact = contactrepo.findById(index).get();

		m.addAttribute("updateContact", contact);

		return "normal/updateContact";

	}

	@PostMapping("/process-update")
	public String update(@ModelAttribute("updateContact") Contacts contact, RedirectAttributes redirectAttrs, Model m,
			@RequestParam("cImgFile") MultipartFile imgFile, Principal principal) {
		System.out.println("Inside the updatecontact post mapping");

		Contacts existingContact = contactrepo.findById(contact.getCId()).get();

		try {
			// handling the image
			if (!imgFile.isEmpty()) {
				// file work
				// rewrite
				// delete new photo
				File deleteFile = new ClassPathResource("static/Images").getFile();
				File file1 = new File(deleteFile, existingContact.getCImgName());
				file1.delete();
				// update new photo
				File file = new ClassPathResource("static/Images").getFile();

				String uniqueFilename = System.currentTimeMillis() + "_" + imgFile.getName();

				Path path = Paths.get(file.getAbsolutePath() + File.separator + uniqueFilename);

				Files.copy(imgFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				contact.setCImgName(uniqueFilename);

				System.out.println(uniqueFilename);

			} else {
				contact.setCImgName(existingContact.getCImgName());
			}

			User user = this.userrepo.getUserByUserName(principal.getName());
			contact.setUser(user);

			contactrepo.save(contact);

			redirectAttrs.addFlashAttribute("updateMsg", "Contact Updated!!!!!");
			redirectAttrs.addFlashAttribute("toastType", "success");
		} catch (Exception e) {
			logger.error("Error occurred while updating contact image", e);
		}

		return "redirect:/user/contactProfile/" + contact.getCId();
	}

}