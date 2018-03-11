package cdt.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import cdt.entities.PollConfig;

public interface PollConfigRepositoryIf extends CrudRepository<PollConfig, UUID> {
	
}
