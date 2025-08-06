package com.SCM.Services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CaptchaValidator {

    private static final String SECRET_KEY = "0x4AAAAAABmdP8N1h44VLBE-LHCGhr2kFSU";
    private static final String VERIFY_URL = "https://challenges.cloudflare.com/turnstile/v0/siteverify";

    public boolean verifyCaptcha(String token, String remoteIp) {
        try {
        	
        	if(token == null || token.trim().isEmpty()) {
        		System.out.println("captcha token is empty");
        		return false;
        	}
        	
        	// Step 1: Create a RestTemplate object to make an HTTP POST request to Cloudflare's verification API
    	            	
            RestTemplate restTemplate = new RestTemplate();

         // Step 2: Create HTTP headers(that helps to define content type) and set the content type to application/x-www-form-urlencoded
    	    // This tells Cloudflare we're sending a standard HTML form data POST request
    	    
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

         // Step 3: Prepare the POST body data as key-value pairs (like HTML form fields)
    	    
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            
         // Add your site’s secret key provided by Cloudflare (do NOT expose this publicly)
    	    
            body.add("secret", SECRET_KEY);
         
            // Add the user-submitted CAPTCHA token (sent from the frontend form)
    	    
            body.add("response", token);
            
         // Optionally add the user's IP address (helps Cloudflare with verification)
    	    
            body.add("remoteip", remoteIp);

         // Step 4: Combine the headers and body into an HTTP request object
    	    
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

         // Step 5: Send POST request to Cloudflare Turnstile verification API
    	    // The API returns a JSON response indicating whether the CAPTCHA was solved correctly
    	    
            ResponseEntity<String> response = restTemplate.postForEntity(
                VERIFY_URL,  // Cloudflare's verification endpoint
                requestEntity, // our headers + body
                String.class  // we want the response as a raw JSON string
            );
            
            ObjectMapper mapper = new ObjectMapper(); // creates a JSON parser.
            JsonNode jsonnode = mapper.readTree(response.getBody()); //parses the response body (which is a JSON string) into a JsonNode.
            boolean result = jsonnode.get("success").asBoolean();

            return result;
        } catch (Exception e) {
            System.out.println("CAPTCHA verification failed: " + e.getMessage());
            return false;
        }
    }
	
	
}
