package cdt.dto;

import java.util.ArrayList;
import java.util.List;

public class AnswerBatchDto {
	
	List<AnswerDto> answers = new ArrayList<AnswerDto>();

	public List<AnswerDto> getAnswers() {
		return answers;
	}

	public void setAnswers(List<AnswerDto> answers) {
		this.answers = answers;
	}
	
}
