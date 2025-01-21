package com.example.test1;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundManager {
    private MediaPlayer correctSound;
    private MediaPlayer incorrectSound;
    private Context context;

    public SoundManager(Context context) {
        this.context = context;
        correctSound = MediaPlayer.create(context, R.raw.correct);
        incorrectSound = MediaPlayer.create(context, R.raw.incorrect);
    }

    public void playCorrectSound() {
        try {
            if (correctSound != null && !correctSound.isPlaying()) {
                correctSound.start();
            }
        } catch (IllegalStateException e) {
            // Handle or log error
            recreateMediaPlayer(true);
        }
    }

    public void playIncorrectSound() {
        try {
            if (incorrectSound != null && !incorrectSound.isPlaying()) {
                incorrectSound.start();
            }
        } catch (IllegalStateException e) {
            recreateMediaPlayer(false);
        }
    }

    public void release() {
        if (correctSound != null) {
            correctSound.release();
            correctSound = null;
        }
        if (incorrectSound != null) {
            incorrectSound.release();
            incorrectSound = null;
        }
    }

    private void recreateMediaPlayer(boolean isCorrect) {
        try {
            if (isCorrect) {
                if (correctSound != null) {
                    correctSound.release();
                }
                correctSound = MediaPlayer.create(context, R.raw.correct);
            } else {
                if (incorrectSound != null) {
                    incorrectSound.release();
                }
                incorrectSound = MediaPlayer.create(context, R.raw.incorrect);
            }
        } catch (Exception e) {
            // Log error
        }
    }
} 