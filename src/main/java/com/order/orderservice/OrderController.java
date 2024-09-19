package com.order.orderservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.shared.Application;
import org.springframework.http.*;
import java.util.*;
import 	org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
class AuthenticateUser
{ 
	private String userName;
	private String password;
	private String role;
	public AuthenticateUser() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	
}
class JwtResponse
{
	private String token;
	
	public JwtResponse(String token) 
	{
		this.token=token;
	}
	public String getToken() { 
		return token;
	}
}
@RestController
public class OrderController {
	
//@Autowired
//DiscoveryClient discoveryClient;


@Autowired
RestTemplate restTemplate;

private static final String URL="http://localhost:8080/itemslist";
@PostMapping("/items")
public ResponseEntity<?> getItems(@RequestBody AuthenticateUser authenticate) 
{


HttpHeaders headers=new HttpHeaders();
headers.setContentType(MediaType.APPLICATION_JSON);
HttpEntity<AuthenticateUser> request = new HttpEntity<>(authenticate, headers);
ResponseEntity<?> response = restTemplate.postForEntity(URL+"/api/auth", request,Object.class);
Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

//Create a new JSONObject from the LinkedHashMap
JSONObject jsonObject = new JSONObject(responseBody);
String token = jsonObject.getString("token");

System.out.println(token);

String itemsUrl=URL+"/getitems";
headers=new HttpHeaders();
headers.set("Authorization", "Bearer "+token);
HttpEntity<Void> requestEntity=new HttpEntity<>(headers);
ResponseEntity<List<Item>> itemsResponse = restTemplate.exchange(itemsUrl, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<Item>>() {});
List<Item> items = itemsResponse.getBody();
return itemsResponse;

}
	
}
