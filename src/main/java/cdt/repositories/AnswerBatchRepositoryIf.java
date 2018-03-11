package cdt.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import cdt.entities.AnswerBatch;

public interface AnswerBatchRepositoryIf extends CrudRepository<AnswerBatch, UUID> {
	
	public List<AnswerBatch> findByPollId(UUID pollId);
	
	@Query("SELECT COUNT(batch) FROM AnswerBatch batch WHERE batch.poll.id = ?1")
	public Integer countNAnswersInternal(UUID pollId);
	
	default Integer countNAnswers(UUID pollId) {
		Integer res = countNAnswersInternal(pollId);
		return res == null ? 0 : res.intValue();
	}
	
	@Query("SELECT ans.text FROM AnswerBatch batch JOIN batch.answers ans WHERE batch.poll.id = ?1 AND ans.question.id = ?2")
	public List<String> getQuestionTextAnswers(UUID pollId, UUID questionId);
	
	@Query("SELECT ans.rate FROM AnswerBatch batch JOIN batch.answers ans WHERE batch.poll.id = ?1 AND ans.question.id = ?2")
	public List<Integer> getQuestionRates(UUID pollId, UUID questionId);
}
