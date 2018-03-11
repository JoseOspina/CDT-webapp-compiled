package cdt.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import cdt.entities.Answer;

public interface AnswerRepositoryIf extends CrudRepository<Answer, UUID> {
}
