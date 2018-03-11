package cdt.dto;

import java.util.List;

public class PollDetailsDto {
	
	public Integer numberOfAnswers;
	public List<AxisResultDto> axesResults;

	public Integer getNumberOfAnswers() {
		return numberOfAnswers;
	}

	public void setNumberOfAnswers(Integer numberOfAnswers) {
		this.numberOfAnswers = numberOfAnswers;
	}

	public List<AxisResultDto> getAxesResults() {
		return axesResults;
	}

	public void setAxesResults(List<AxisResultDto> axesResults) {
		this.axesResults = axesResults;
	}
	
}
