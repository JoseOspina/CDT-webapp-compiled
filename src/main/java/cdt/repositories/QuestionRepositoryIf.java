package cdt.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import cdt.entities.Question;

public interface QuestionRepositoryIf extends CrudRepository<Question, UUID> {
	public Question findById(UUID questionId);
}
