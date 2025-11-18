package com.example.tts.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tts_history")
public class TtsRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4000)
    private String text;

    private String languageCode;
    private String voiceName;
    private double rate;
    private double pitch;
    private String audioFileName;
    private LocalDateTime createdAt;

    // Default constructor
    public TtsRecord() {}

    // Parameterized constructor
    public TtsRecord(String text, String languageCode, String voiceName,
                     double rate, double pitch, String audioFileName,
                     LocalDateTime createdAt) {

        this.text = text;
        this.languageCode = languageCode;
        this.voiceName = voiceName;
        this.rate = rate;
        this.pitch = pitch;
        this.audioFileName = audioFileName;
        this.createdAt = createdAt;
    }

    // Getters & setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String t) {
        this.text = t;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String l) {
        this.languageCode = l;
    }

    public String getVoiceName() {
        return voiceName;
    }

    public void setVoiceName(String v) {
        this.voiceName = v;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double r) {
        this.rate = r;
    }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double p) {
        this.pitch = p;
    }

    public String getAudioFileName() {
        return audioFileName;
    }

    public void setAudioFileName(String a) {
        this.audioFileName = a;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime c) {
        this.createdAt = c;
    }
}
