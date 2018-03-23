package cdt.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import cdt.dto.AxisDto;

@Entity
@Table(name="axes")
public class Axis {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private Poll poll;
	
	private String title;
	
	private Boolean includeInPlot;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String description;
	
	@ManyToMany
	@OrderColumn(name = "questions_order")
	private List<Question> questions = new ArrayList<Question>();

	
	public AxisDto toDto() {
		AxisDto dto = new AxisDto();
		
		dto.setId(id.toString());
		dto.setTitle(title);
		dto.setDescription(description);
		dto.setIncludeInPlot(includeInPlot);
		
		for (Question question : questions) {
			dto.getQuestions().add(question.toDto());
		}
		
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public Boolean getIncludeInPlot() {
		return includeInPlot;
	}

	public void setIncludeInPlot(Boolean includeInPlot) {
		this.includeInPlot = includeInPlot;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
		
}
