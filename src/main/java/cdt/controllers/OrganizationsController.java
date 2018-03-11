package cdt.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cdt.dto.AnswerDto;
import cdt.dto.GetResult;
import cdt.dto.OrganizationDto;
import cdt.dto.PollDetailsDto;
import cdt.dto.PollDto;
import cdt.dto.PostResult;
import cdt.entities.PollAudience;
import cdt.entities.ResponderType;

@RestController
@RequestMapping("/1")
public class OrganizationsController extends BaseController {
	
	@RequestMapping(path = "/organization/create",  method = RequestMethod.POST)
    public PostResult createOrganization(@RequestBody OrganizationDto organizationDto) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled for users only", null);
		}
		
		return organizationService.create(organizationDto, getLoggedUserId());
	}
	
	@RequestMapping(path = "/organization/{organizationId}",  method = RequestMethod.GET)
    public GetResult<OrganizationDto> getOrganization(@PathVariable(name="organizationId") String orgIdStr) {
		
		if (getLoggedUser() == null) {
			return new GetResult<OrganizationDto>("error", "endpoint enabled for users only", null);
		}
		
		UUID orgId = UUID.fromString(orgIdStr);
		
		if (!organizationService.isAdmin(orgId, getLoggedUserId())) {
			return new GetResult<OrganizationDto>("error", "endpoint enabled for admins only", null);
		}
		
		return organizationService.get(orgId);
	}
	
	@RequestMapping(path = "/organization/{organizationId}/hasTemplates",  method = RequestMethod.GET)
    public GetResult<Boolean> hasTemplates(@PathVariable(name="organizationId") String orgIdStr) {
		
		if (getLoggedUser() == null) {
			return new GetResult<Boolean>("error", "endpoint enabled for users only", null);
		}
		
		UUID orgId = UUID.fromString(orgIdStr);
		
		return new GetResult<Boolean>("success", "", organizationService.hasTemplates(orgId));
	}
	
	@RequestMapping(path = "/organization/{organizationId}/templates",  method = RequestMethod.GET)
    public GetResult<List<PollDto>> getTemplates(@PathVariable(name="organizationId") String orgIdStr) {
		
		if (getLoggedUser() == null) {
			return new GetResult<List<PollDto>>("error", "endpoint enabled for users only", null);
		}
		
		UUID orgId = UUID.fromString(orgIdStr);
		
		return organizationService.getTemplates(orgId);
	}
	
	@RequestMapping(path = "/organization/{organizationId}/poll",  method = RequestMethod.POST)
    public PostResult createPoll(
    		@PathVariable(name="organizationId") String orgIdStr, 
    		@RequestBody PollDto pollDto) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled for users only", null);
		}
		
		UUID orgId = UUID.fromString(orgIdStr);
		
		return organizationService.createPoll(orgId, pollDto, getLoggedUserId());
	}
	
	@RequestMapping(path = "/organization/{organizationId}/polls",  method = RequestMethod.GET)
    public GetResult<List<PollDto>> getPolls(
    		@PathVariable(name="organizationId") String orgIdStr) {
		
		if (getLoggedUser() == null) {
			return new GetResult<List<PollDto>>("error", "endpoint enabled for users only", null);
		}
		
		UUID orgId = UUID.fromString(orgIdStr);
		
		if (!organizationService.isAdmin(orgId, getLoggedUserId())) {
			return new GetResult<List<PollDto>>("error", "endpoint enabled for admins only", null);
		}
		
		return organizationService.getPollsList(orgId);
	}
	
	@RequestMapping(path = "/poll/{pollId}",  method = RequestMethod.GET)
	public GetResult<PollDto> getPoll(
    		@PathVariable(name="pollId") String pollIdStr) {
		
		UUID pollId = UUID.fromString(pollIdStr);
		return organizationService.getPoll(pollId);
	}
	
	@RequestMapping(path = "/poll/{pollId}",  method = RequestMethod.DELETE)
	public PostResult deletePoll(
    		@PathVariable(name="pollId") String pollIdStr) {
		
		UUID pollId = UUID.fromString(pollIdStr);
		UUID orgId = organizationService.getOrganizationIdFromPollId(pollId);
		UUID userId = getLoggedUserId();
		
		if (userId == null) {
			return new PostResult("error", "endpoint enabled for users only", null);
		}
		
		if (!organizationService.isAdmin(orgId, userId)) {
			return new PostResult("error", "endpoint enabled for admins only", null);
		}
		
		return organizationService.deletePoll(pollId);
	}
	
	
	@RequestMapping(path = "/poll/{pollId}/details",  method = RequestMethod.GET)
	public GetResult<PollDetailsDto> getPollDetails(
    		@PathVariable(name="pollId") String pollIdStr) {
		
		UUID pollId = UUID.fromString(pollIdStr);
		UUID orgId = organizationService.getOrganizationIdFromPollId(pollId);
		UUID userId = getLoggedUserId();
		
		if (userId == null) {
			return new GetResult<PollDetailsDto>("error", "endpoint enabled for users only", null);
		}
		
		if (!organizationService.isAdmin(orgId, userId)) {
			return new GetResult<PollDetailsDto>("error", "endpoint enabled for admins only", null);
		}
		
		return organizationService.getPollDetails(pollId);
	}
	
	@RequestMapping(path = "/poll/{pollId}/answer",  method = RequestMethod.POST)
	public PostResult answerPoll(
    		@PathVariable(name="pollId") String pollIdStr, 
    		@RequestBody List<AnswerDto> answersDto) {
		
		UUID pollId = UUID.fromString(pollIdStr);
		
		ResponderType responderType = null;
		
		if (organizationService.getPollAudience(pollId) == PollAudience.ANYONE_WITH_LINK) {
			responderType = ResponderType.ANONYMOUS;
		}
		
		if (responderType != null) {
			return organizationService.answerPoll(pollId, answersDto, responderType);
		} else {
			return new PostResult("error", "not authorized to answer this poll", null);
		}
	}
	
	@RequestMapping(path = "/poll/{pollId}/makeTemplate",  method = RequestMethod.PUT)
	public PostResult makeTemplate(
    		@PathVariable(name="pollId") String pollIdStr,
    		@RequestParam(name ="isTemplate") Boolean isTemplate) {
		
		UUID pollId = UUID.fromString(pollIdStr);
		
		UUID orgId = organizationService.getOrganizationIdFromPollId(pollId);
		UUID userId = getLoggedUserId();
		
		if (userId == null) {
			return new PostResult("error", "endpoint enabled for users only", null);
		}
		
		if (!organizationService.isAdmin(orgId, userId)) {
			return new PostResult("error", "endpoint enabled for admins only", null);
		}
		
		return organizationService.makeTemplate(pollId, isTemplate);
	}
	
	@RequestMapping(path = "/poll/{pollId}/makePublicTemplate",  method = RequestMethod.PUT)
	public PostResult makePublicTemplate(
    		@PathVariable(name="pollId") String pollIdStr,
    		@RequestParam(name ="isPublic") Boolean isTemplate) {
		
		UUID pollId = UUID.fromString(pollIdStr);
		
		UUID orgId = organizationService.getOrganizationIdFromPollId(pollId);
		UUID userId = getLoggedUserId();
		
		if (userId == null) {
			return new PostResult("error", "endpoint enabled for users only", null);
		}
		
		if (!organizationService.isAdmin(orgId, userId)) {
			return new PostResult("error", "endpoint enabled for admins only", null);
		}
		
		return organizationService.makeTemplatePublic(pollId, isTemplate);
	}
}
