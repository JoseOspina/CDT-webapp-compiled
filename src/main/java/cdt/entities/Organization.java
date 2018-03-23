package cdt.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import cdt.dto.OrganizationDto;

@Entity
@Table(name="organizations")
public class Organization {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	private String name;
	
	@Enumerated(EnumType.STRING)
	private OrganizationStatus status;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String description;
	
	@ManyToOne
	private AppUser creator;
	
	@ManyToMany
	private List<AppUser> admins = new ArrayList<AppUser>();
	
	@OneToMany(mappedBy = "organization")
	private List<Member> members = new ArrayList<Member>();
	
	@Column(name = "creation_date")
	private Timestamp creationDate;
	

	public OrganizationDto toDto() {
		OrganizationDto dto = new OrganizationDto();
		
		dto.setId(id.toString());
		dto.setName(name);
		dto.setDescription(description);
		
		return dto;
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public OrganizationStatus getStatus() {
		return status;
	}

	public void setStatus(OrganizationStatus status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AppUser getCreator() {
		return creator;
	}

	public void setCreator(AppUser creator) {
		this.creator = creator;
	}
	
	public List<AppUser> getAdmins() {
		return admins;
	}

	public void setAdmins(List<AppUser> admins) {
		this.admins = admins;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}
	
	
}
