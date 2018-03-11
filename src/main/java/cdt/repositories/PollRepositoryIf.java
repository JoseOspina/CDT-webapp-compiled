package cdt.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import cdt.entities.Poll;
import cdt.entities.PollStatus;

public interface PollRepositoryIf extends CrudRepository<Poll, UUID> {
	
	
	public Poll findById(UUID id);
	
	@Query("SELECT COUNT(po) FROM Poll po WHERE (po.isTemplate = TRUE AND po.organization.id = ?1) OR po.isPublicTemplate = TRUE")
	public Integer countNTemplatesInternal(UUID orgId);
	
	@Query("SELECT po FROM Poll po WHERE po.organization.id = ?1 AND po.status != ?2")
	public List<Poll> findByOrganization_IdAndNotInStatus(UUID orgId, PollStatus status);
	
	public List<Poll> findByOrganization_Id(UUID orgId);
	
	default Boolean hasTemplates(UUID orgId) {
		Integer res = countNTemplatesInternal(orgId);
		return res == null ? false : res.intValue() > 0;
	}
	
	@Query("SELECT org.id FROM Poll po JOIN po.organization org WHERE po.id = ?1")
	public UUID getOrganizationIdFromPollId(UUID pollId);
	
	@Query("SELECT po FROM Poll po WHERE (po.isTemplate = TRUE AND po.organization.id = ?1) OR po.isPublicTemplate = TRUE")
	public List<Poll> getTemplates(UUID orgId);
	
}
