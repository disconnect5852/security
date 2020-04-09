package asd.tsanyi.security.db.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import asd.tsanyi.security.dto.UserDTO;

@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private boolean active;
	private String address;
	private long created;
    @Column(nullable = false, updatable = false)
    private Date created_at;
    private Long deleted;
    private Date deleted_at;
    private boolean deleted_flag;
    @Column(unique = true)
    private String email;
    private String email_token;
    private Date last_login;
    private String name;
    private boolean next_login_change_pwd;
    private String password;
    private boolean password_expired;
    private String phone;
    private Long settlement_id;
    private String temp_password;
    private Date temp_password_expired;
    private Long updated;
    private Date updated_at;
    private String user_type;
    @Column(unique = true, nullable = false, updatable = false)
    private String username;
	@OneToMany(mappedBy = "user")
	@Cascade(CascadeType.ALL)
	private Set<UserRoles> roles;
	
	public User() {
	}
	
	
	public User(UserDTO userDTO) {
		
		this.address = userDTO.getAddress();
		this.email = userDTO.getEmail();
		this.email_token = userDTO.getEmail_token();
		this.name = userDTO.getName();
		this.phone = userDTO.getPhone();
		this.temp_password = RandomStringUtils.randomAscii(8);
		
		Calendar cal= Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 7);
		this.temp_password_expired=cal.getTime();
		this.username = userDTO.getUsername();
		this.password = userDTO.getPassword();
		
		Set<UserRoles> roles= new HashSet<UserRoles>();
		userDTO.getRoles().forEach(role -> roles.add(new UserRoles(this, role)));
		this.roles = roles;
		this.created_at=new Date();
	}
	
//	public User(String address, String email, String email_token, String name, String phone, String temp_password, String username, Set<Roles> roles) {
//		super();
//		this.address = address;
//		this.email = email;
//		this.email_token = email_token;
//		this.name = name;
//		this.phone = phone;
//		this.temp_password = temp_password;
//		this.username = username;
//		this.roles = roles;
//		this.created_at=new Date();
//		
//		this.created=0;
//	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Long getDeleted() {
		return deleted;
	}

	public void setDeleted(Long deleted) {
		this.deleted = deleted;
		this.deleted_at= new Date();
	}

	public Date getDeleted_at() {
		return deleted_at;
	}

	public void setDeleted_at(Date deleted_at) {
		this.deleted_at = deleted_at;
	}

	public boolean isDeleted_flag() {
		return deleted_flag;
	}

	public void setDeleted_flag(boolean deleted_flag) {
		this.deleted_flag = deleted_flag;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail_token() {
		return email_token;
	}

	public void setEmail_token(String email_token) {
		this.email_token = email_token;
	}

	public Date getLast_login() {
		return last_login;
	}

	public void setLast_login(Date last_login) {
		this.last_login = last_login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isNext_login_change_pwd() {
		return next_login_change_pwd;
	}

	public void setNext_login_change_pwd(boolean next_login_change_pwd) {
		this.next_login_change_pwd = next_login_change_pwd;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isPassword_expired() {
		return password_expired;
	}

	public void setPassword_expired(boolean password_expired) {
		this.password_expired = password_expired;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getSettlement_id() {
		return settlement_id;
	}

	public void setSettlement_id(Long settlement_id) {
		this.settlement_id = settlement_id;
	}

	public String getTemp_password() {
		return temp_password;
	}

	public void setTemp_password(String temp_password) {
		this.temp_password = temp_password;
	}

	public Date getTemp_password_expired() {
		return temp_password_expired;
	}

	public void setTemp_password_expired(Date temp_password_expired) {
		this.temp_password_expired = temp_password_expired;
	}

	public Long getUpdated() {
		return updated;
	}

	public void setUpdated(Long updated) {
		this.updated = updated;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<UserRoles> getRoles() {
		return roles;
	}

	public void setRoles(Set<UserRoles> roles) {
		this.roles = roles;
	}
	
	
}
