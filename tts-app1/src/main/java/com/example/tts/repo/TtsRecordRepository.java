package com.example.tts.repo;

import com.example.tts.model.TtsRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TtsRecordRepository extends JpaRepository<TtsRecord, Long> {
}
