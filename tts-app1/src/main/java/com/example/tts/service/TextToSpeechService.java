package com.example.tts.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TextToSpeechService {

    private static final String CREDENTIAL_PATH = "D:/jar/tts-app.json";

    public String synthesizeToFile(
            String text,
            String languageCode,
            String voiceName,
            double rate,
            double pitch,
            String encoding
    ) throws Exception {

        // Load credentials
        GoogleCredentials credentials =
                GoogleCredentials.fromStream(new FileInputStream(CREDENTIAL_PATH));

        TextToSpeechSettings settings = TextToSpeechSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        try (TextToSpeechClient client = TextToSpeechClient.create(settings)) {

            // Input
            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(text)
                    .build();

            // Voice
            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode(languageCode)
                    .setName(voiceName)
                    .build();

            // Encoding type
            AudioEncoding audioEncoding =
                    encoding.equalsIgnoreCase("wav") ? AudioEncoding.LINEAR16 : AudioEncoding.MP3;

            // Audio config
            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(audioEncoding)
                    .setSpeakingRate(rate)
                    .setPitch(pitch)
                    .build();

            // Send request
            SynthesizeSpeechResponse response =
                    client.synthesizeSpeech(input, voice, audioConfig);

            // Output file name
            String fileName = "tts_" + System.currentTimeMillis() +
                    (audioEncoding == AudioEncoding.MP3 ? ".mp3" : ".wav");

            Path outputPath = Path.of("audio", fileName);
            Files.createDirectories(outputPath.getParent());
            Files.write(outputPath, response.getAudioContent().toByteArray());

            return fileName;
        }
    }

	public File getAudioFile(String fileName) {
		// TODO Auto-generated method stub
		return null;
	}
}
