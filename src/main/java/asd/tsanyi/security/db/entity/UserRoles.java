package asd.tsanyi.security.db.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import asd.tsanyi.security.enums.Roles;

@Entity
@Table(name = "user_roles")
public class UserRoles {
	@Id
	@GeneratedValue
	long id;
	@ManyToOne
	User user;	
	Roles roles;
	public UserRoles(User user, Roles roles) {
		super();
		this.user = user;
		this.roles = roles;
	}
	
	public UserRoles() {
	}

	public Roles getRoles() {
		return roles;
	}

	public void setRoles(Roles roles) {
		this.roles = roles;
	}
	
	
	
}
