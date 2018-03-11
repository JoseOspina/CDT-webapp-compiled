package cdt.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import cdt.entities.QuestionAndWeight;

public interface QuestionAndWeightRepositoryIf extends CrudRepository<QuestionAndWeight, UUID> {
	
}
