package cdt.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import cdt.entities.Organization;
import cdt.entities.OrganizationStatus;

public interface OrganizationRepositoryIf extends CrudRepository<Organization, UUID> {
	
	
	public Organization findById(UUID id);
	
	@Query("SELECT org FROM Organization org JOIN org.admins admin WHERE admin.id = ?1 AND (org.status != ?2 OR org.status IS NULL) ORDER BY org.creationDate DESC")
	public List<Organization> findByAdminNotWithStatus(UUID id, OrganizationStatus status);
	
	public Organization findByIdAndAdmins_Id(UUID orgId, UUID adminId);
	
}
