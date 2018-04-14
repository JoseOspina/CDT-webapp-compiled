package cdt.dto;

import java.util.ArrayList;
import java.util.List;

public class PollDetailsDto {
	
	public List<AnswerBatchDto> answerBatches = new ArrayList<AnswerBatchDto>();

	public List<AnswerBatchDto> getAnswerBatches() {
		return answerBatches;
	}

	public void setAnswerBatches(List<AnswerBatchDto> answerBatches) {
		this.answerBatches = answerBatches;
	}
		
}
