package cdt.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import cdt.entities.Axis;
import cdt.entities.Question;

public interface AxisRepositoryIf extends CrudRepository<Axis, UUID> {
	public Axis findById(UUID id);
	
	@Query("SELECT q FROM Axis ax JOIN ax.questions q WHERE ax.id = ?1 and q.id = ?2")
	public Question findByAxisIdAndQuestionId(UUID axisId, UUID questionId);
	
}
