package cdt.dto;

import java.util.ArrayList;
import java.util.List;

public class PollDto {
	
	String id;
	String title;
	String description;
	Boolean isTemplate;
	Boolean isPublicTemplate;
	Boolean custom;
	
	List<AxisDto> axes = new ArrayList<AxisDto>();
	PollConfigDto config;
	
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
	public Boolean getIsTemplate() {
		return isTemplate;
	}
	public void setIsTemplate(Boolean isTemplate) {
		this.isTemplate = isTemplate;
	}
	public Boolean getIsPublicTemplate() {
		return isPublicTemplate;
	}
	public void setIsPublicTemplate(Boolean isPublicTemplate) {
		this.isPublicTemplate = isPublicTemplate;
	}
	public List<AxisDto> getAxes() {
		return axes;
	}
	public void setAxes(List<AxisDto> axes) {
		this.axes = axes;
	}
	public PollConfigDto getConfig() {
		return config;
	}
	public void setConfig(PollConfigDto config) {
		this.config = config;
	}
	public Boolean getCustom() {
		return custom;
	}
	public void setCustom(Boolean custom) {
		this.custom = custom;
	}
	
}
