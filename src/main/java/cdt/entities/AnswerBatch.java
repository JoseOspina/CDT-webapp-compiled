package cdt.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name="answer_batches")
public class AnswerBatch {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private Poll poll;
	
	@Enumerated(EnumType.STRING)
	private ResponderType responderType;
	
	@ManyToOne
	private AppUser answeredBy;
	
	@OneToMany(mappedBy = "batch")
	private List<Answer> answers = new ArrayList<Answer>();
	

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
	
	public ResponderType getResponderType() {
		return responderType;
	}

	public void setResponderType(ResponderType responderType) {
		this.responderType = responderType;
	}

	public AppUser getAnsweredBy() {
		return answeredBy;
	}

	public void setAnsweredBy(AppUser answeredBy) {
		this.answeredBy = answeredBy;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	
}
