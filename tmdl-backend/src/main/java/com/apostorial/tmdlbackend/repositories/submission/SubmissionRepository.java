package com.apostorial.tmdlbackend.repositories.submission;

import com.apostorial.tmdlbackend.entities.submission.Submission;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubmissionRepository extends MongoRepository<Submission, String> {
}
