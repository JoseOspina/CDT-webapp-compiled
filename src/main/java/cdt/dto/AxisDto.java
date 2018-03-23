package cdt.dto;

import java.util.ArrayList;
import java.util.List;

public class AxisDto {
	
	String id;
	String title;
	String description;
	private Boolean includeInPlot;
	
	List<QuestionDto> questions = new ArrayList<QuestionDto>();
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getIncludeInPlot() {
		return includeInPlot;
	}
	public void setIncludeInPlot(Boolean includeInPlot) {
		this.includeInPlot = includeInPlot;
	}
	public List<QuestionDto> getQuestions() {
		return questions;
	}
	public void setQuestions(List<QuestionDto> questions) {
		this.questions = questions;
	}
	
}
