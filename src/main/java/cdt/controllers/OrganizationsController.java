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
import cdt.dto.AppUserDto;
import cdt.dto.GetResult;
import cdt.dto.MemberDto;
import cdt.dto.OrganizationDto;
import cdt.dto.PollDetailsDto;
import cdt.dto.PollDto;
import cdt.dto.PostResult;
import cdt.entities.PollAudience;

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
	
	
	@RequestMapping(path = "/organization/delete/{organizationId}",  method = RequestMethod.DELETE)
    public PostResult deleteOrganization(@PathVariable(name="organizationId") String orgIdStr) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled for users only", null);
		}
		
		UUID orgId = UUID.fromString(orgIdStr);
		
		if (!organizationService.isAdmin(orgId, getLoggedUserId())) {
			return new PostResult("error", "endpoint enabled for admins only", null);
		}
		
		return organizationService.delete(orgId);
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
    public GetResult<List<PollDto>> getTemplates(
    		@PathVariable(name="organizationId") String orgIdStr,
    		@RequestParam(name="searchPublic", defaultValue="true") Boolean searchPublic) {
		
		if (getLoggedUser() == null) {
			return new GetResult<List<PollDto>>("error", "endpoint enabled for users only", null);
		}
		
		UUID orgId = UUID.fromString(orgIdStr);
		
		return organizationService.getTemplates(orgId, searchPublic);
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
	
	@RequestMapping(path = "/poll/{pollId}",  method = RequestMethod.PUT)
    public PostResult editPoll(
    		@PathVariable(name="pollId") String pollIdStr, 
    		@RequestBody PollDto pollDto) {
		
		UUID pollId = UUID.fromString(pollIdStr);
		UUID orgId = organizationService.getOrganizationIdFromPollId(pollId);
		UUID userId = getLoggedUserId();
		
		if (userId == null) {
			return new PostResult("error", "endpoint enabled for users only", null);
		}
		
		if (!organizationService.isAdmin(orgId, userId)) {
			return new PostResult("error", "endpoint enabled for admins only", null);
		}
		
		return organizationService.editPoll(pollId, pollDto);
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
    		@RequestParam(name="secret", defaultValue="") String secret,
    		@RequestBody List<AnswerDto> answersDto) {
		
		UUID pollId = UUID.fromString(pollIdStr);
		
		if (organizationService.getPollAudience(pollId) == PollAudience.ANY_MEMBER) {
			if (!organizationService.checkSecret(pollId, secret)) {
				return new PostResult("error", "not authorized to fill this survey", null);
			}
		}
		
		return organizationService.answerPoll(pollId, answersDto, secret);
		
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
	
	@RequestMapping(path = "/organization/{organizationId}/members",  method = RequestMethod.GET)
    public GetResult<List<MemberDto>> getMembers(
    		@PathVariable(name="organizationId") String orgIdStr) {
		
		if (getLoggedUser() == null) {
			return new GetResult<List<MemberDto>>("error", "endpoint enabled for users only", null);
		}
		
		UUID orgId = UUID.fromString(orgIdStr);
		
		if (!organizationService.isAdmin(orgId, getLoggedUserId())) {
			return new GetResult<List<MemberDto>>("error", "endpoint enabled for admins only", null);
		}
		
		return organizationService.getMembersList(orgId);
	}
	
	@RequestMapping(path = "/organization/{organizationId}/member",  method = RequestMethod.PUT)
    public PostResult addMember(
    		@PathVariable(name="organizationId") String orgIdStr,
    		@RequestBody MemberDto memberDto ) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled for users only", null);
		}
		
		UUID orgId = UUID.fromString(orgIdStr);
		
		if (!organizationService.isAdmin(orgId, getLoggedUserId())) {
			return new PostResult("error", "endpoint enabled for admins only", null);
		}
		
		return organizationService.addMember(orgId, memberDto);
	}
	
	@RequestMapping(path = "/member/{memberId}",  method = RequestMethod.DELETE)
    public PostResult deleteMember(
    		@PathVariable(name="memberId") String memberIdStr) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled for users only", null);
		}
		
		UUID memberId = UUID.fromString(memberIdStr);
		UUID orgId = organizationService.getOrganizationIdFromMemberId(memberId);
		
		if (!organizationService.isAdmin(orgId, getLoggedUserId())) {
			return new PostResult("error", "endpoint enabled for admins only", null);
		}
		
		return organizationService.deleteMember(memberId);
	}
	
	@RequestMapping(path = "/poll/{pollId}/secretValid/{secret}",  method = RequestMethod.GET)
    public GetResult<Boolean> checkSecret(
    		@PathVariable(name="pollId") String pollIdStr,
    		@PathVariable(name="secret") String secret) {
		
		return new GetResult<Boolean>("success", "valdility checked", organizationService.checkSecret(UUID.fromString(pollIdStr), secret));
	}
	
	@RequestMapping(path = "/organization/{organizationId}/admins",  method = RequestMethod.GET)
    public GetResult<List<AppUserDto>> getAdmins(
    		@PathVariable(name="organizationId") String orgIdStr) {
		
		if (getLoggedUser() == null) {
			return new GetResult<List<AppUserDto>>("error", "endpoint enabled for users only", null);
		}
		
		UUID orgId = UUID.fromString(orgIdStr);
		
		if (!organizationService.isAdmin(orgId, getLoggedUserId())) {
			return new GetResult<List<AppUserDto>>("error", "endpoint enabled for admins only", null);
		}
		
		return organizationService.getAdmins(orgId);
	}
	
	@RequestMapping(path = "/organization/{organizationId}/admin",  method = RequestMethod.PUT)
    public PostResult addAdmin(
    		@PathVariable(name="organizationId") String orgIdStr,
    		@RequestBody AppUserDto adminDto ) {
		
		if (getLoggedUser() == null) {
			return new PostResult("error", "endpoint enabled for users only", null);
		}
		
		UUID orgId = UUID.fromString(orgIdStr);
		
		if (!organizationService.isAdmin(orgId, getLoggedUserId())) {
			return new PostResult("error", "endpoint enabled for admins only", null);
		}
		
		return organizationService.addAdmin(orgId, adminDto.getEmail());
	}
	
	@RequestMapping(path = "/organization/{organizationId}/admin/{adminId}",  method = RequestMethod.DELETE)
    public PostResult deleteAdmin(
    		@PathVariable(name="organizationId") String orgIdStr,
    		@PathVariable(name="adminId") String adminIdStr) {
		
		UUID orgId = UUID.fromString(orgIdStr);
		
		if (!organizationService.isAdmin(orgId, getLoggedUserId())) {
			return new PostResult("error", "endpoint enabled for admins only", null);
		}
		
		return organizationService.deleteAdmin(orgId, UUID.fromString(adminIdStr));
	}
}
