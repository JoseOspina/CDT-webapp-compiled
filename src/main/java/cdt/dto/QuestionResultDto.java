package cdt.dto;

import java.util.ArrayList;
import java.util.List;

public class QuestionResultDto {
	
	public String questionId;
	public String questionText;
	public String questionType;
	public double weight;
	
	public List<String> answersTexts = new ArrayList<String>();
	public List<Integer> answersRates = new ArrayList<Integer>();
	
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public List<String> getAnswersTexts() {
		return answersTexts;
	}
	public void setAnswersTexts(List<String> answersTexts) {
		this.answersTexts = answersTexts;
	}
	public List<Integer> getAnswersRates() {
		return answersRates;
	}
	public void setAnswersRates(List<Integer> answersRates) {
		this.answersRates = answersRates;
	}
	
}
