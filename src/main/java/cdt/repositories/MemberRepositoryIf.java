package cdt.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import cdt.entities.Member;

public interface MemberRepositoryIf extends CrudRepository<Member, UUID> {
	public Member findById(UUID id);
	
	public Member findByOrganizationIdAndEmail(UUID orgId, String email);
}
