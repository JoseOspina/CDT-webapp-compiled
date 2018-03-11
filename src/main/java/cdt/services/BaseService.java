package cdt.services;

import org.springframework.beans.factory.annotation.Autowired;

import cdt.repositories.AnswerBatchRepositoryIf;
import cdt.repositories.AnswerRepositoryIf;
import cdt.repositories.AppUserRepositoryIf;
import cdt.repositories.AxisRepositoryIf;
import cdt.repositories.OrganizationRepositoryIf;
import cdt.repositories.PollConfigRepositoryIf;
import cdt.repositories.PollRepositoryIf;
import cdt.repositories.QuestionAndWeightRepositoryIf;
import cdt.repositories.QuestionRepositoryIf;

public class BaseService {
	
	@Autowired
	protected AppUserRepositoryIf appUserRepository;
	
	@Autowired
	protected OrganizationRepositoryIf organizationRepository;
	
	@Autowired
	protected PollRepositoryIf pollRepository;
	
	@Autowired
	protected PollConfigRepositoryIf pollConfigRepository;
	
	@Autowired
	protected AxisRepositoryIf axisRepository;
	
	@Autowired
	protected QuestionRepositoryIf questionRepository;
	
	@Autowired
	protected QuestionAndWeightRepositoryIf questionAndWeightRepository;
	
	@Autowired
	protected AnswerRepositoryIf answerRepository;
	
	@Autowired
	protected AnswerBatchRepositoryIf answerBatchRepository;

}
