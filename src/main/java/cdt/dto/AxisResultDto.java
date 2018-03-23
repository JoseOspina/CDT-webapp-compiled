package cdt.dto;

import java.util.ArrayList;
import java.util.List;

public class AxisResultDto {
	
	public AxisDto axis;
	public List<QuestionResultDto> questionResults = new ArrayList<QuestionResultDto>();
	
	public AxisDto getAxis() {
		return axis;
	}
	public void setAxis(AxisDto axis) {
		this.axis = axis;
	}
	public List<QuestionResultDto> getQuestionResults() {
		return questionResults;
	}
	public void setQuestionResults(List<QuestionResultDto> questionResults) {
		this.questionResults = questionResults;
	}
	
}
