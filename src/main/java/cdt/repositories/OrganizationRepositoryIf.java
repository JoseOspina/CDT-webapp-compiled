package cdt.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import cdt.entities.Organization;

public interface OrganizationRepositoryIf extends CrudRepository<Organization, UUID> {
	
	
	public Organization findById(UUID id);
	
	public List<Organization> findByAdmins_Id(UUID id);
	
	public Organization findByIdAndAdmins_Id(UUID orgId, UUID adminId);
	
}
