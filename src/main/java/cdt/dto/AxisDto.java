package cdt.dto;

import java.util.ArrayList;
import java.util.List;

public class AxisDto {
	
	String id;
	String title;
	String description;
	List<QuestionDto> questions = new ArrayList<QuestionDto>();
	Boolean custom;

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
	public List<QuestionDto> getQuestions() {
		return questions;
	}
	public void setQuestions(List<QuestionDto> questions) {
		this.questions = questions;
	}
	public Boolean getCustom() {
		return custom;
	}
	public void setCustom(Boolean custom) {
		this.custom = custom;
	}
	
}
