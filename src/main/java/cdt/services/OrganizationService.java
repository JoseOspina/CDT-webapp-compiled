package cdt.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import cdt.dto.AnswerDto;
import cdt.dto.AxisDto;
import cdt.dto.AxisResultDto;
import cdt.dto.GetResult;
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
import cdt.entities.Organization;
import cdt.entities.Poll;
import cdt.entities.PollAudience;
import cdt.entities.PollConfig;
import cdt.entities.PollStatus;
import cdt.entities.Question;
import cdt.entities.QuestionAndWeight;
import cdt.entities.QuestionType;
import cdt.entities.ResponderType;

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
		
		organization = organizationRepository.save(organization);
		
		return new PostResult("success", "organization created", organization.getId().toString());
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
	public GetResult<List<PollDto>> getTemplates(UUID orgId) {
		List<Poll> templates = pollRepository.getTemplates(orgId);
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
		config.setPoll(poll);
		
		config = pollConfigRepository.save(config);
		
		
		for (AxisDto axisDto : pollDto.getAxes()) {
			
			Axis axis = null;
			if (axisDto.getCustom()) {
				axis = new Axis();
				
				axis.setTitle(axisDto.getTitle());
				axis.setDescription(axisDto.getDescription());
				axis = axisRepository.save(axis);
				
				for (QuestionDto questionDto : axisDto.getQuestions()) {
					Question question = null;
					
					if (questionDto.getCustom()) {
						question = new Question();
						
					} else {
						question = questionRepository.findById(UUID.fromString(questionDto.getId()));
						if (question == null) {
							return new PostResult("error", "non-custom question not found", null);
						}
					}
					
					question.setText(questionDto.getText());
					question.setType(QuestionType.valueOf(questionDto.getType()));
					question = questionRepository.save(question);
					
					QuestionAndWeight questionAndWeight = new QuestionAndWeight();
					
					questionAndWeight.setQuestion(question);
					questionAndWeight.setWeight(questionDto.getWeight());
					questionAndWeight = questionAndWeightRepository.save(questionAndWeight);
					
					axis.getQuestionsAndWeights().add(questionAndWeight);
				}
			} else {
				axis = axisRepository.findById(UUID.fromString(axisDto.getId()));
				if (axis == null) {
					return new PostResult("error", "non-custom axis not found", null);
				}
			}
			
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
			axisResults.setAxisId(axis.getId().toString());
			axisResults.setAxisTitle(axis.getTitle());
			
			for (QuestionAndWeight questionAndWeight : axis.getQuestionsAndWeights()) {
				
				QuestionResultDto questionResult = new QuestionResultDto();
				questionResult.setQuestionId(questionAndWeight.getQuestion().getId().toString());
				questionResult.setQuestionText(questionAndWeight.getQuestion().getText());
				questionResult.setQuestionType(questionAndWeight.getQuestion().getType().toString());
				
				switch (questionAndWeight.getQuestion().getType()) {
				case TEXT:
					questionResult.setAnswersTexts(answerBatchRepository.getQuestionTextAnswers(poll.getId(), questionAndWeight.getQuestion().getId()));
					break;
					
				case RATE_1_5:
					questionResult.setWeight(questionAndWeight.getWeight());
					questionResult.setAnswersRates(answerBatchRepository.getQuestionRates(poll.getId(), questionAndWeight.getQuestion().getId()));
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
	public PostResult answerPoll(UUID pollId, List<AnswerDto> answersDto, ResponderType responderType) {
		
		Poll poll = pollRepository.findById(pollId);
		
		if (poll == null) {
			return new PostResult("error", "poll not found", null);
		}
		
		AnswerBatch batch = new AnswerBatch();
		
		batch.setResponderType(responderType);
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
}
