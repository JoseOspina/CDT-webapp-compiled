package cdt.dto;

import java.util.ArrayList;
import java.util.List;

public class AppUserDto {
	private String id;
	private List<String> auth0Ids = new ArrayList<String>();
	private String email;
	private String nickname;
	private List<OrganizationDto> organizations = new ArrayList<OrganizationDto>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public List<OrganizationDto> getOrganizations() {
		return organizations;
	}
	public void setOrganizations(List<OrganizationDto> organizations) {
		this.organizations = organizations;
	}
	
}
