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
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import cdt.dto.PollDto;

@Entity
@Table(name="polls")
public class Poll {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private Organization organization;
	
	private String title;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String description;
	
	@ManyToOne
	private AppUser creator;
	
	@Column(name = "creation_date")
	private Timestamp creationDate;
	
	@Enumerated(EnumType.STRING)
	public PollStatus status;
	
	@ManyToMany
	@OrderColumn(name = "axes_order")
	private List<Axis> axes = new ArrayList<Axis>();
	
	private Boolean isTemplate;	
	
	private Boolean isPublicTemplate;
	
	@OneToOne(mappedBy = "poll")
	private PollConfig config;
	
	
	public PollDto toDtoLight() {
		PollDto dto = new PollDto();
		
		dto.setId(id.toString());
		dto.setTitle(title);
		dto.setDescription(description);
		dto.setIsTemplate(isTemplate);
		dto.setIsPublicTemplate(isPublicTemplate);
		dto.setCustom(false);
		
		return dto;
	}
	
	public PollDto toDto() {
		PollDto dto = toDtoLight();
		
		for (Axis axis : axes) {
			dto.getAxes().add(axis.toDto());
		}
		
		return dto;
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	
	public PollStatus getStatus() {
		return status;
	}

	public void setStatus(PollStatus status) {
		this.status = status;
	}

	public List<Axis> getAxes() {
		return axes;
	}

	public void setAxes(List<Axis> axis) {
		this.axes = axis;
	}

	public Boolean getIsTemplate() {
		return isTemplate;
	}

	public void setIsTemplate(Boolean isTemplate) {
		this.isTemplate = isTemplate;
	}
	
	public Boolean getIsPublicTemplate() {
		return isPublicTemplate;
	}

	public void setIsPublicTemplate(Boolean isPublicTemplate) {
		this.isPublicTemplate = isPublicTemplate;
	}

	public PollConfig getConfig() {
		return config;
	}

	public void setConfig(PollConfig config) {
		this.config = config;
	}
	
		
}
