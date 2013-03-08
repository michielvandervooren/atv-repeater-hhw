package models.access;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import models.access.utils.Hashing;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.Play;
import play.data.validation.Password;
import play.db.jpa.Model;
import controllers.CRUD.Exclude;
import controllers.access.LoginId;

@Entity(name="AbstractUser")
public abstract class User extends Model {

	public String username;
	
	@Password
	@Transient
	public String password;
	
	@Exclude
	private String passwordHash;
	
	@Exclude
	private String salt;
	
	@ManyToMany
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
	public List<Role> roles;
	
	public boolean hasPermission(String permissionName) {
		for (Role role : roles) {
			for (Permission permission : role.permissions) {
				if (permission.name.equals(permissionName)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean authenticate(String password) {
		return Hashing.hash(password, salt, Play.secretKey).equals(this.passwordHash);
	}
	
	@PrePersist
	@PreUpdate
	public void setSaltAndPasswordHash() {
		this.salt = Hashing.salt(256);
		if (password != null && password.trim().length() > 0) {
			this.passwordHash = Hashing.hash(password, this.salt, Play.secretKey);;
		}
	}
	
	public static User findByLogin(String loginId) {
		String idField = getLoginIdField();
		String query = idField == null ? "byUsername" : "by" + StringUtils.capitalize(idField);
		return find(query, loginId).first();
	}
	
	private static String getLoginIdField() {
		List<Class> assignable = Play.classloader.getAssignableClasses(User.class);
		Class runtimeUserClass = assignable.size() == 0 ? User.class : assignable.get(0);
		
		for (Field f : runtimeUserClass.getDeclaredFields()) {
			if (f.getAnnotation(LoginId.class) != null && f.getType().equals(String.class)) {
				return f.getName();
			}
		}
		return null;
	}
}
