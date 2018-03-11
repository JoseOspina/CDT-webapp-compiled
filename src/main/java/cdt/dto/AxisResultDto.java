package cdt.dto;

import java.util.ArrayList;
import java.util.List;

public class AxisResultDto {
	
	public String axisId;
	public String axisTitle;
	
	public List<QuestionResultDto> questionResults = new ArrayList<QuestionResultDto>();
	
	public String getAxisId() {
		return axisId;
	}
	public void setAxisId(String axisId) {
		this.axisId = axisId;
	}
	public String getAxisTitle() {
		return axisTitle;
	}
	public void setAxisTitle(String axisTitle) {
		this.axisTitle = axisTitle;
	}
	public List<QuestionResultDto> getQuestionResults() {
		return questionResults;
	}
	public void setQuestionResults(List<QuestionResultDto> questionResults) {
		this.questionResults = questionResults;
	}
	
}
