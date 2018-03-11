package cdt.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import cdt.entities.Axis;
import cdt.entities.QuestionAndWeight;

public interface AxisRepositoryIf extends CrudRepository<Axis, UUID> {
	public Axis findById(UUID id);
	
	@Query("SELECT qw FROM Axis ax JOIN ax.questionsAndWeights qw WHERE ax.id = ?1 and qw.question.id = ?2")
	public QuestionAndWeight findByAxisIdAndQuestionId(UUID axisId, UUID questionId);
	
}
