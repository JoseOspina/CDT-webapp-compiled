package cdt.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import cdt.dto.AppUserDto;

@Entity
@Table( name = "app_users" )
public class AppUser {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ElementCollection
	@CollectionTable(name="auth_ids", joinColumns=@JoinColumn(name="user_id"))
	@Column(name="auth0_id")
	private List<String> auth0Ids = new ArrayList<String>();
	
	@Column(name = "email", unique = true)
	private String email;
	
	@Column(name = "nickname", unique = true)
	private String nickname;
	
	@Column(name = "email_notifications_enabled")
	private Boolean emailNotificationsEnabled;
	
	
	public AppUserDto toDto() {
		AppUserDto dto = new AppUserDto();
		
		dto.setId(id.toString());
		dto.setEmail(email);
		dto.setNickname(nickname);
		
		return dto;
	}
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public List<String> getAuth0Ids() {
		return auth0Ids;
	}
	public void setAuth0Ids(List<String> auth0Ids) {
		this.auth0Ids = auth0Ids;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Boolean getEmailNotificationsEnabled() {
		return emailNotificationsEnabled;
	}
	public void setEmailNotificationsEnabled(Boolean emailNotificationsEnabled) {
		this.emailNotificationsEnabled = emailNotificationsEnabled;
	}
		
}
