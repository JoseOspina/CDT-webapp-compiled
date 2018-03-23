package cdt.services;

import org.springframework.beans.factory.annotation.Autowired;

import cdt.repositories.AnswerBatchRepositoryIf;
import cdt.repositories.AnswerRepositoryIf;
import cdt.repositories.AppUserRepositoryIf;
import cdt.repositories.AxisRepositoryIf;
import cdt.repositories.MemberRepositoryIf;
import cdt.repositories.OrganizationRepositoryIf;
import cdt.repositories.PollConfigRepositoryIf;
import cdt.repositories.PollCredentialRepositoryIf;
import cdt.repositories.PollRepositoryIf;
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
	protected AnswerRepositoryIf answerRepository;
	
	@Autowired
	protected AnswerBatchRepositoryIf answerBatchRepository;
	
	@Autowired
	protected MemberRepositoryIf memberRepository;
	
	@Autowired
	protected PollCredentialRepositoryIf pollCredentialRepository;
	
	
	

}
