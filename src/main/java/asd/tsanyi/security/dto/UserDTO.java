package asd.tsanyi.security.dto;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import asd.tsanyi.security.enums.Roles;

public class UserDTO {
	private String address;
	@Email
	private String email;
	@NotBlank
	private String name;
	private String email_token;
	private String phone;
	@NotBlank
	private String username;
	@NotBlank
	private String password;
	private Set<Roles> roles;

//	public UserDTO(String address, String email, String email_token, String name, String phone, String temp_password, String username, Set<Roles> roles) {
//		this.address = address;
//		this.email = email;
//		this.email_token = email_token;
//		this.name = name;
//		this.phone = phone;
//		this.temp_password = temp_password;
//		this.username = username;
//		this.roles = roles;
//	}
	
	@Override
	public String toString() {
		return "UserDTO [" + (address != null ? "address=" + address + ", " : "") + (email != null ? "email=" + email + ", " : "") + (name != null ? "name=" + name + ", " : "")
				+ (email_token != null ? "email_token=" + email_token + ", " : "") + (phone != null ? "phone=" + phone + ", " : "") + (username != null ? "username=" + username + ", " : "")
				+ (password != null ? "password=" + password + ", " : "") + (roles != null ? "roles=" + roles : "") + "]";
	}


	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail_token() {
		return email_token;
	}

	public void setEmail_token(String email_token) {
		this.email_token = email_token;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<Roles> getRoles() {
		return roles;
	}

	public void setRoles(Set<Roles> roles) {
		this.roles = roles;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
