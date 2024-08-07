package com.apostorial.tmdlbackend.repositories;

import com.apostorial.tmdlbackend.entities.Submission;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubmissionRepository extends MongoRepository<Submission, String> {
}
