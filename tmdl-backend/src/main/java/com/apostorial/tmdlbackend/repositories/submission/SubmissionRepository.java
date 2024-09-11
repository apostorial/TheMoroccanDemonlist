package com.apostorial.tmdlbackend.repositories.submission;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface SubmissionRepository<T> extends MongoRepository<T, String> {
}
