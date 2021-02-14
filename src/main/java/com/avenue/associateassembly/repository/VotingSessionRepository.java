package com.avenue.associateassembly.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.avenue.associateassembly.entity.VotingSession;

@Repository
public interface VotingSessionRepository extends MongoRepository<VotingSession, ObjectId> {
}

