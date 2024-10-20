package net.haxjakt.core;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgramRepository extends MongoRepository<ProgramRecord, String> {
    Optional<ProgramRecord> findByScriptName(String id);
}
