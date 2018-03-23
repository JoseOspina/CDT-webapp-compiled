package cdt.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import cdt.dto.PollConfigDto;

@Entity
@Table(name="polls_config")
public class PollConfig {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@OneToOne
	private Poll poll;
	
	@Enumerated(EnumType.STRING)
	private PollAudience audience;
	
	private Boolean notificationsSent;
	
	public PollConfigDto toDto() {
		PollConfigDto dto = new PollConfigDto();
		dto.setAudience(audience.toString());
		
		return dto;
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Poll getPoll() {
		return poll;
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
	}

	public PollAudience getAudience() {
		return audience;
	}

	public void setAudience(PollAudience audience) {
		this.audience = audience;
	}

	public Boolean getNotificationsSent() {
		return notificationsSent;
	}

	public void setNotificationsSent(Boolean notificationsSent) {
		this.notificationsSent = notificationsSent;
	}
	
}
