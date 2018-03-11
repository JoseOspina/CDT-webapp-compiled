package cdt.dto;

public class QuestionDto {
	
	String id;
	String text;
	String type;
	double weight;
	Boolean custom;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public Boolean getCustom() {
		return custom;
	}
	public void setCustom(Boolean custom) {
		this.custom = custom;
	}
	
}
