package cdt.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;

import cdt.dto.AnswerDto;
import cdt.dto.AppUserDto;
import cdt.dto.AxisDto;
import cdt.dto.AxisResultDto;
import cdt.dto.GetResult;
import cdt.dto.MemberDto;
import cdt.dto.OrganizationDto;
import cdt.dto.PollDetailsDto;
import cdt.dto.PollDto;
import cdt.dto.PostResult;
import cdt.dto.QuestionDto;
import cdt.dto.QuestionResultDto;
import cdt.entities.Answer;
import cdt.entities.AnswerBatch;
import cdt.entities.AppUser;
import cdt.entities.Axis;
import cdt.entities.Member;
import cdt.entities.Organization;
import cdt.entities.OrganizationStatus;
import cdt.entities.Poll;
import cdt.entities.PollAudience;
import cdt.entities.PollConfig;
import cdt.entities.PollCredential;
import cdt.entities.PollStatus;
import cdt.entities.Question;
import cdt.entities.QuestionType;

@Service
public class OrganizationService extends BaseService {

	@Transactional
	public PostResult create(OrganizationDto organizationDto, UUID creatorId) {
		
		AppUser creator = appUserRepository.findById(creatorId);
		
		Organization organization = new Organization();
		
		organization.setName(organizationDto.getName());
		organization.setDescription(organizationDto.getDescription());
		organization.setCreator(creator);
		organization.getAdmins().add(creator);
		organization.setCreationDate(new Timestamp(System.currentTimeMillis()));
		organization.setStatus(OrganizationStatus.OPEN);
		
		organization = organizationRepository.save(organization);
		
		return new PostResult("success", "organization created", organization.getId().toString());
	}
	
	@Transactional
	public PostResult delete(UUID orgId) {
		Organization organization = organizationRepository.findById(orgId);
		organization.setStatus(OrganizationStatus.DELETED);
		organization = organizationRepository.save(organization);
		
		return new PostResult("success", "organization deleted", organization.getId().toString());
	}
	
	@Transactional
	public Boolean isAdmin(UUID orgId, UUID creatorId) {
		return organizationRepository.findByIdAndAdmins_Id(orgId, creatorId) != null; 
	}
	
	@Transactional
	public GetResult<OrganizationDto> get(UUID orgId) {
		Organization org = organizationRepository.findById(orgId);
		
		OrganizationDto orgDto = org.toDto();
		
		/* add polls */
		
		return new GetResult<OrganizationDto>("success", "organization retrieved", orgDto);
	}
	
	@Transactional
	public Boolean hasTemplates(UUID orgId) {
		return pollRepository.hasTemplates(orgId);
	}
	
	@Transactional
	public GetResult<List<PollDto>> getTemplates(UUID orgId, Boolean searchPublic) {
		List<Poll> templates = pollRepository.getTemplates(orgId, searchPublic);
		List<PollDto> templateDtos = new ArrayList<PollDto>();
		
		for (Poll template : templates) {
			templateDtos.add(template.toDtoLight());
		}
		return new GetResult<List<PollDto>>("success", "templates retrieved", templateDtos);
	}
	
	@Transactional
	public PostResult createPoll(UUID orgId, PollDto pollDto, UUID creatorId) {
		
		Organization organization = organizationRepository.findById(orgId);
		
		Poll poll = new Poll();
		poll.setOrganization(organization);
		poll.setTitle(pollDto.getTitle());
		poll.setDescription(pollDto.getDescription());
		poll.setCreator(appUserRepository.findById(creatorId));
		poll.setCreationDate(new Timestamp(System.currentTimeMillis()));
		poll.setStatus(PollStatus.OPEN);
		
		poll = pollRepository.save(poll);
		
		PollConfig config = new PollConfig();
		config.setAudience(PollAudience.valueOf(pollDto.getConfig().getAudience()));
		config.setNotificationsSent(false);
		
		config.setPoll(poll);
		
		if (config.getAudience() == PollAudience.ANY_MEMBER) {
			/* prepare credentials */
			for (Member member : poll.getOrganization().getMembers()) {
				PollCredential credential = new PollCredential();
				credential.setPoll(poll);
				credential.setMember(member);
				credential.setSecret(RandomStringUtils.randomAlphabetic(32));
				credential.setUsed(false);
				
				credential = pollCredentialRepository.save(credential);
				
				poll.getCredentials().add(credential);
			}
		}	
		
		config = pollConfigRepository.save(config);
	
		for (AxisDto axisDto : pollDto.getAxes()) {
			
			Axis axis = new Axis();
			
			axis.setTitle(axisDto.getTitle());
			axis.setDescription(axisDto.getDescription());
			axis.setIncludeInPlot(axisDto.getIncludeInPlot());
			axis = axisRepository.save(axis);
			
			for (QuestionDto questionDto : axisDto.getQuestions()) {
				Question question = new Question();
				
				question.setText(questionDto.getText());
				question.setType(QuestionType.valueOf(questionDto.getType()));
				question.setWeight(questionDto.getWeight());
				question = questionRepository.save(question);
				
				axis.getQuestions().add(question);
			}
			
			poll.getAxes().add(axis);
		}

		return new PostResult("success", "poll created", poll.getId().toString());
	}
	
	@Transactional
	public PostResult editPoll(UUID pollId, PollDto pollDto) {
		
		Poll poll = pollRepository.findById(pollId);
		if (poll == null) {
			return new PostResult("error", "poll not found", null);
		}
		
		poll.setTitle(pollDto.getTitle());
		poll.setDescription(pollDto.getDescription());
		poll = pollRepository.save(poll);
		
		List<Axis> newAxes = new ArrayList<Axis>();
		
		for (AxisDto axisDto : pollDto.getAxes()) {
			
			/* check if axis is new or existing */
			UUID axisId = null;
			try { axisId = UUID.fromString(axisDto.getId()); } catch (Exception ex) {}
			
			Axis axis = null;
			/* check if this axis was already in the poll */
			if (axisId != null) { 
				for (Axis axisTemp : poll.getAxes()) {
					if (axisTemp.getId().equals(axisId)) {
						axis = axisTemp;
					}
				}
			}
			
			if (axis == null) {
				axis = new Axis();
			}
			
			axis.setTitle(axisDto.getTitle());
			axis.setDescription(axisDto.getDescription());
			axis.setIncludeInPlot(axisDto.getIncludeInPlot());
			axis = axisRepository.save(axis);
			
			List<Question> newQuestions = new ArrayList<Question>();
			
			for (QuestionDto questionDto : axisDto.getQuestions()) {
				
				/* check if question is new or existing */
				UUID questionId = null;
				try { questionId = UUID.fromString(questionDto.getId()); } catch (Exception ex) {}
				
				Question question = null;
				if (questionId != null) { 
					for (Question questionTemp : axis.getQuestions()) {
						if (questionTemp.getId().equals(questionId)) {
							question = questionTemp;
						}
					}
				}
				
				if (question == null) {
					question = new Question();
				}
				
				question.setText(questionDto.getText());
				question.setType(QuestionType.valueOf(questionDto.getType()));
				question.setWeight(questionDto.getWeight());
				question = questionRepository.save(question);
				
				newQuestions.add(question);	
			}
			
			axis.getQuestions().clear();
			for (Question question : newQuestions) {
				axis.getQuestions().add(question);
			}
			
			newAxes.add(axis);	
		}
		
		poll.getAxes().clear();
		for (Axis axis : newAxes) {
			poll.getAxes().add(axis);
		}

		return new PostResult("success", "poll created", poll.getId().toString());
	}
	
	@Transactional
	public GetResult<List<PollDto>> getPollsList(UUID orgId) {
		List<Poll> polls = pollRepository.findByOrganization_IdAndNotInStatus(orgId, PollStatus.DELETED);
		
		List<PollDto> pollDtos = new ArrayList<PollDto>();
		
		for (Poll poll : polls) {
			pollDtos.add(poll.toDtoLight());
		}
		
		return new GetResult<List<PollDto>>("success", "organization retrieved", pollDtos);
	}
	
	@Transactional
	public UUID getOrganizationIdFromPollId(UUID pollId) {
		return pollRepository.getOrganizationIdFromPollId(pollId);
	}
	
	@Transactional
	public UUID getOrganizationIdFromMemberId(UUID memberId) {
		return memberRepository.findById(memberId).getOrganization().getId();
	}
	
	@Transactional
	public PollDto getPollDto(UUID pollId) {
		Poll poll = pollRepository.findById(pollId);
		return poll.toDto();
	}
	
	@Transactional
	public GetResult<PollDto> getPoll(UUID pollId) {
		return new GetResult<PollDto>("success", "organization retrieved", getPollDto(pollId));
	}
	
	@Transactional
	public PostResult deletePoll(UUID pollId) {
		Poll poll = pollRepository.findById(pollId);
		poll.setStatus(PollStatus.DELETED);
		
		return new PostResult("success", "poll deleted", null);
	}
	
	@Transactional
	public PostResult makeTemplate(UUID pollId, Boolean isTemplate) {
		Poll poll = pollRepository.findById(pollId);
		poll.setIsTemplate(isTemplate);
		
		pollRepository.save(poll);
		return new PostResult("success", "poll template modified", poll.getId().toString());
	}
	
	@Transactional
	public PostResult makeTemplatePublic(UUID pollId, Boolean isPublic) {
		Poll poll = pollRepository.findById(pollId);
		poll.setIsPublicTemplate(isPublic);
		
		pollRepository.save(poll);
		return new PostResult("success", "poll template modified", poll.getId().toString());
	}
	
	@Transactional
	public GetResult<PollDetailsDto> getPollDetails(UUID pollId) {
		PollDetailsDto pollDetailsDto = new PollDetailsDto();
		
		pollDetailsDto.setNumberOfAnswers(answerBatchRepository.countNAnswers(pollId));
		pollDetailsDto.setAxesResults(getAxesResults(pollId));
		
		return new GetResult<PollDetailsDto>("success", "organization retrieved", pollDetailsDto);
	}
	

	@Transactional
	public List<AxisResultDto> getAxesResults(UUID pollId) {
		
		Poll poll = pollRepository.findById(pollId);
		List<AxisResultDto> axesResultsDto = new ArrayList<AxisResultDto>();
		
		for (Axis axis : poll.getAxes()) {
			AxisResultDto axisResults = new AxisResultDto();
			axisResults.setAxis(axis.toDto());
			
			for (Question question : axis.getQuestions()) {
				
				QuestionResultDto questionResult = new QuestionResultDto();
				questionResult.setQuestionId(question.getId().toString());
				questionResult.setQuestionText(question.getText());
				questionResult.setQuestionType(question.getType().toString());
				
				switch (question.getType()) {
				case TEXT:
					questionResult.setAnswersTexts(answerBatchRepository.getQuestionTextAnswers(poll.getId(), question.getId()));
					break;
					
				case RATE_1_5:
					questionResult.setWeight(question.getWeight());
					questionResult.setAnswersRates(answerBatchRepository.getQuestionRates(poll.getId(), question.getId()));
					break;
				default:
					break;
				}
				
				axisResults.getQuestionResults().add(questionResult);
			}
			
			axesResultsDto.add(axisResults);
		}
		
		return axesResultsDto;
	}
	
	@Transactional
	public PollAudience getPollAudience(UUID pollId) {
		return pollRepository.findById(pollId).getConfig().getAudience();
	}
	
	@Transactional
	public PostResult answerPoll(UUID pollId, List<AnswerDto> answersDto, String secret) {
		
		Poll poll = pollRepository.findById(pollId);
		
		if (poll == null) {
			return new PostResult("error", "poll not found", null);
		}
		
		AnswerBatch batch = new AnswerBatch();
		
		batch.setSecret(secret);
		batch.setPoll(poll);
		batch = answerBatchRepository.save(batch);
		
		for (AnswerDto answerDto : answersDto) {
			
			Question question = questionRepository.findById(UUID.fromString(answerDto.getQuestionId()));
			if (question == null) {
				return new PostResult("error", "question not found", null);
			}
			
			Answer answer = new Answer();
			
			answer.setBatch(batch);
			answer.setQuestion(question);
			answer.setText(answerDto.getText());
			answer.setRate(answerDto.getRate());
			
			answer = answerRepository.save(answer);
			batch.getAnswers().add(answer);
		}
		
		return new PostResult("success", "poll answered", batch.getId().toString());
	}
	
	@Transactional
	public GetResult<List<MemberDto>> getMembersList(UUID orgId) {
		Organization organization = organizationRepository.findById(orgId);
		List<Member> members = organization.getMembers();
		
		List<MemberDto> membersDto = new ArrayList<MemberDto>();
		for (Member member : members) {
			membersDto.add(member.toDto());
		}
		
		return new GetResult<List<MemberDto>>("success", "members retrieved", membersDto);
	}
	
	@Transactional
	public PostResult addMember(UUID orgId, MemberDto memberDto) {
		Organization organization = organizationRepository.findById(orgId);
		
		Member existingMember = memberRepository.findByOrganizationIdAndEmail(orgId, memberDto.getEmail());
		
		if (existingMember != null) {
			return new PostResult("error", "email already in organization", null);
		}
		
		Member member = new Member();
		
		member.setOrganization(organization);
		member.setEmail(memberDto.getEmail());
		member = memberRepository.save(member);
		
		organization.getMembers().add(member);
		organizationRepository.save(organization);
		
		return new PostResult("success", "member added", member.getId().toString());
	}
	
	@Transactional
	public PostResult deleteMember(UUID memberId) {
		Member member = memberRepository.findById(memberId);
		
		if (member == null) {
			return new PostResult("error", "member not found", null);
		}
		
		/* delete credentials too */
		List<PollCredential> credentials = pollCredentialRepository.findByMember_Id(member.getId());
		for (PollCredential credential : credentials) {
			pollCredentialRepository.delete(credential);
		}
		
		memberRepository.delete(member);
		return new PostResult("success", "member deleted", null);
		
	}
	
	@Transactional
	public PostResult addAdmin(UUID orgId, String email) {
		AppUser admin = appUserRepository.findByEmail(email);
		
		if (admin == null) {
			return new PostResult("error", "user not found with that email", null);
		}
		
		Organization organization = organizationRepository.findById(orgId);
		organization.getAdmins().add(admin);
		organizationRepository.save(organization);
		
		return new PostResult("success", "admin added", admin.getId().toString());
	}
	
	@Transactional
	public GetResult<List<AppUserDto>> getAdmins(UUID orgId) {
		Organization organization = organizationRepository.findById(orgId);
		List<AppUser> admins = organization.getAdmins();
		List<AppUserDto> adminsDto = new ArrayList<AppUserDto>();
		
		for (AppUser admin : admins) {
			adminsDto.add(admin.toDto());	
		}
		
		return new GetResult<List<AppUserDto>>("success", "admins retrieved", adminsDto);
	}
	
	@Transactional
	public PostResult deleteAdmin(UUID orgId, UUID adminId) {
		AppUser admin = appUserRepository.findById(adminId);
		
		Organization organization = organizationRepository.findById(orgId);
		if (organization.getAdmins().size() == 1) {
			return new PostResult("error", "cant delete all admins", null);
		}
		
		organization.getAdmins().remove(admin);
		organizationRepository.save(organization);
		
		return new PostResult("success", "admin removed", null);
	}
	
	@Transactional
	public Boolean checkSecret(UUID pollId, String secret) {
		PollCredential credential = pollCredentialRepository.findBySecret(secret);
		
		if (credential == null) {
			return false;	
		}
		
		Boolean pollValid = credential.getPoll().getId().equals(pollId);
		
		return pollValid;
	}
	
}
