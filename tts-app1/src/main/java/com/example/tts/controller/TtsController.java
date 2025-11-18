package com.example.tts.controller;

import com.example.tts.model.TtsRecord;
import com.example.tts.repo.TtsRecordRepository;
import com.example.tts.service.TextToSpeechService;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class TtsController {

    private final TextToSpeechService ttsService;
    private final TtsRecordRepository repo;

    public TtsController(TextToSpeechService ttsService,
                         TtsRecordRepository repo) {
        this.ttsService = ttsService;
        this.repo = repo;
    }

    // ----------------------------------------
    // TTS GENERATION
    // ----------------------------------------
    @PostMapping("/tts")
    public ResponseEntity<Map<String, Object>> generateTts(
            @RequestBody Map<String, Object> body) {

        String text = (String) body.getOrDefault("text", "");

        if (text.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Text is required"
            );
        }

        String languageCode = (String) body.getOrDefault("languageCode", "en-US");
        String voiceName = (String) body.getOrDefault("voiceName", "");
        double rate = ((Number) body.getOrDefault("rate", 1.0)).doubleValue();
        double pitch = ((Number) body.getOrDefault("pitch", 0.0)).doubleValue();
        String encoding = (String) body.getOrDefault("encoding", "MP3");

        try {
            // Generate audio file
            String fileName = ttsService.synthesizeToFile(
                    text, languageCode, voiceName, rate, pitch, encoding
            );

            // Save record to DB
            TtsRecord record = new TtsRecord(
                    text, languageCode, voiceName, rate, pitch,
                    fileName, LocalDateTime.now()
            );

            repo.save(record);

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "file", "/api/audio/" + fileName,
                            "recordId", record.getId()
                    )
            );

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "TTS failed: " + ex.getMessage()
            );
        }
    }

    // ----------------------------------------
    // GET AUDIO FILE
    // ----------------------------------------
    @GetMapping("/audio/{fileName:.+}")
    public ResponseEntity<Resource> getAudio(@PathVariable String fileName) {

        File file = ttsService.getAudioFile(fileName);

        if (file == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Audio file not found"
            );
        }

        FileSystemResource resource = new FileSystemResource(file);

        MediaType mediaType = fileName.endsWith(".mp3")
                ? MediaType.valueOf("audio/mpeg")
                : MediaType.valueOf("audio/wav");

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getName() + "\""
                )
                .body(resource);
    }

    // ----------------------------------------
    // GET HISTORY
    // ----------------------------------------
    @GetMapping("/history")
    public List<TtsRecord> getHistory() {
        return repo.findAll();
    }

    // ----------------------------------------
    // DELETE HISTORY RECORD
    // ----------------------------------------
    @DeleteMapping("/history/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Record not found"
            );
        }

        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
