package cdt.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import cdt.entities.PollCredential;

public interface PollCredentialRepositoryIf extends CrudRepository<PollCredential, UUID> {
	
	public List<PollCredential> findByMember_Id(UUID memberId);
	
	public PollCredential findBySecret(String secret);
}
