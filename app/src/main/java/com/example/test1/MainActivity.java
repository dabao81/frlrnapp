package com.example.test1;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView wordHintText;
    private ImageView wordImage;
    private EditText wordInput;
    private Button submitButton;
    private TextView resultText;
    private TextView scoreText;

    private List<Word> words;
    private Word currentWord;
    private int score = 0;
    private Random random = new Random();
    private SoundManager soundManager;
    private int currentLevel = 1;
    private static final int WORDS_PER_LEVEL = 5;
    private int wordsCompleted = 0;

    private static final String KEY_SCORE = "score";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_WORDS_COMPLETED = "words_completed";

    private List<Word> masterWordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(KEY_SCORE, 0);
            currentLevel = savedInstanceState.getInt(KEY_LEVEL, 1);
            wordsCompleted = savedInstanceState.getInt(KEY_WORDS_COMPLETED, 0);
        }

        // Initialize views
        wordHintText = findViewById(R.id.wordHintText);
        wordImage = findViewById(R.id.wordImage);
        wordInput = findViewById(R.id.wordInput);
        submitButton = findViewById(R.id.submitButton);
        resultText = findViewById(R.id.resultText);
        scoreText = findViewById(R.id.scoreText);

        // Initialize word list
        initializeWords();

        // Set up the first word
        showNextWord();

        // Set up button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });

        updateScore();

        soundManager = new SoundManager(this);

        wordInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkAnswer();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_LEVEL, currentLevel);
        outState.putInt(KEY_WORDS_COMPLETED, wordsCompleted);
    }

    private void initializeWords() {
        masterWordList = new ArrayList<>();
        
        // DELF A1 Level Words
        // Personal Information
        masterWordList.add(new Word("nom", "Comment tu t'appelles?", R.drawable.name));
        masterWordList.add(new Word("prénom", "Ton premier nom", R.drawable.firstname));
        masterWordList.add(new Word("âge", "Tu as combien d'ans?", R.drawable.age));
        masterWordList.add(new Word("adresse", "Où habites-tu?", R.drawable.address));
        masterWordList.add(new Word("téléphone", "Pour appeler quelqu'un", R.drawable.phone));
        
        // Family
        masterWordList.add(new Word("famille", "Les personnes qui vivent avec toi", R.drawable.family));
        masterWordList.add(new Word("père", "Le papa", R.drawable.father));
        masterWordList.add(new Word("mère", "La maman", R.drawable.mother));
        masterWordList.add(new Word("frère", "Garçon de mes parents", R.drawable.brother));
        masterWordList.add(new Word("sœur", "Fille de mes parents", R.drawable.sister));
        
        // Daily Objects
        masterWordList.add(new Word("livre", "On le lit pour apprendre", R.drawable.book));
        masterWordList.add(new Word("stylo", "Pour écrire", R.drawable.pen));
        masterWordList.add(new Word("crayon", "Pour dessiner", R.drawable.pencil));
        masterWordList.add(new Word("cahier", "Pour prendre des notes", R.drawable.notebook));
        masterWordList.add(new Word("sac", "Pour porter des choses", R.drawable.bag));
        
        // Basic Food
        masterWordList.add(new Word("pain", "Pour le petit déjeuner", R.drawable.bread));
        masterWordList.add(new Word("lait", "Boisson blanche", R.drawable.milk));
        masterWordList.add(new Word("eau", "Pour boire", R.drawable.water));
        masterWordList.add(new Word("fromage", "Spécialité française", R.drawable.cheese));
        masterWordList.add(new Word("œuf", "La poule le pond", R.drawable.egg));
        
        // Numbers (1-10)
        masterWordList.add(new Word("un", "Premier nombre", R.drawable.one));
        masterWordList.add(new Word("deux", "Après un", R.drawable.two));
        masterWordList.add(new Word("trois", "Après deux", R.drawable.three));
        masterWordList.add(new Word("quatre", "Après trois", R.drawable.four));
        masterWordList.add(new Word("cinq", "Après quatre", R.drawable.five));
        
        // Colors
        masterWordList.add(new Word("rouge", "Couleur du feu", R.drawable.red));
        masterWordList.add(new Word("bleu", "Couleur du ciel", R.drawable.blue));
        masterWordList.add(new Word("vert", "Couleur de l'herbe", R.drawable.green));
        masterWordList.add(new Word("jaune", "Couleur du soleil", R.drawable.yellow));
        masterWordList.add(new Word("noir", "Couleur de la nuit", R.drawable.black));
        
        // DELF A2 Level Words
        // Weather
        masterWordList.add(new Word("soleil", "Il brille dans le ciel", R.drawable.sun));
        masterWordList.add(new Word("pluie", "Elle tombe du ciel", R.drawable.rain));
        masterWordList.add(new Word("neige", "Blanche et froide", R.drawable.snow));
        masterWordList.add(new Word("vent", "Il fait bouger les arbres", R.drawable.wind));
        masterWordList.add(new Word("nuage", "Dans le ciel, parfois gris", R.drawable.cloud));
        
        // Emotions
        masterWordList.add(new Word("content", "Quand on est heureux", R.drawable.happy));
        masterWordList.add(new Word("triste", "Quand on pleure", R.drawable.sad));
        masterWordList.add(new Word("fatigué", "Besoin de dormir", R.drawable.tired));
        masterWordList.add(new Word("fâché", "Très en colère", R.drawable.angry));
        masterWordList.add(new Word("surpris", "Pas attendu", R.drawable.surprised));
        
        // Places
        masterWordList.add(new Word("école", "Pour apprendre", R.drawable.school));
        masterWordList.add(new Word("maison", "Où on habite", R.drawable.house));
        masterWordList.add(new Word("parc", "Pour jouer dehors", R.drawable.park));
        masterWordList.add(new Word("magasin", "Pour acheter", R.drawable.store));
        masterWordList.add(new Word("restaurant", "Pour manger dehors", R.drawable.restaurant));
        
        // Transportation
        masterWordList.add(new Word("voiture", "Sur la route", R.drawable.car));
        masterWordList.add(new Word("vélo", "Deux roues", R.drawable.bicycle));
        masterWordList.add(new Word("bus", "Transport public", R.drawable.bus));
        masterWordList.add(new Word("train", "Sur des rails", R.drawable.train));
        masterWordList.add(new Word("avion", "Dans le ciel", R.drawable.airplane));
        
        // Time
        masterWordList.add(new Word("matin", "Début de journée", R.drawable.morning));
        masterWordList.add(new Word("midi", "Milieu de journée", R.drawable.noon));
        masterWordList.add(new Word("soir", "Fin de journée", R.drawable.evening));
        masterWordList.add(new Word("heure", "Pour dire quand", R.drawable.hour));
        masterWordList.add(new Word("minute", "Plus petit que l'heure", R.drawable.minute));
        
        // Body Parts
        masterWordList.add(new Word("tête", "Partie haute du corps", R.drawable.head));
        masterWordList.add(new Word("main", "Pour tenir des choses", R.drawable.hand));
        masterWordList.add(new Word("pied", "Pour marcher", R.drawable.foot));
        masterWordList.add(new Word("nez", "Pour sentir", R.drawable.nose));
        masterWordList.add(new Word("bouche", "Pour manger et parler", R.drawable.mouth));
        
        // Clothes
        masterWordList.add(new Word("pantalon", "Pour les jambes", R.drawable.pants));
        masterWordList.add(new Word("chemise", "Pour le haut", R.drawable.shirt));
        masterWordList.add(new Word("chaussure", "Pour les pieds", R.drawable.shoe));
        masterWordList.add(new Word("manteau", "Quand il fait froid", R.drawable.coat));
        masterWordList.add(new Word("chapeau", "Sur la tête", R.drawable.hat));
        
        words = new ArrayList<>();
    }

    // Update the difficulty based on level
    private void adjustDifficultyForLevel() {
        words = new ArrayList<>();
        if (currentLevel <= 3) {
            // A1 level words (first 30 words)
            for (int i = 0; i < Math.min(30, masterWordList.size()); i++) {
                words.add(masterWordList.get(i));
            }
        } else if (currentLevel <= 6) {
            // Advanced A1 words (next 30 words)
            for (int i = 30; i < Math.min(60, masterWordList.size()); i++) {
                words.add(masterWordList.get(i));
            }
        } else {
            // A2 level words (remaining words)
            for (int i = 60; i < masterWordList.size(); i++) {
                words.add(masterWordList.get(i));
            }
        }
    }

    private void showNextWord() {
        adjustDifficultyForLevel();
        currentWord = words.get(random.nextInt(words.size()));
        wordHintText.setText(currentWord.getHint());
        wordImage.setImageResource(currentWord.getImageResourceId());
        wordInput.setText("");
        resultText.setText("");
    }

    private void checkAnswer() {
        String userAnswer = wordInput.getText().toString().trim().toLowerCase();
        if (normalizeString(userAnswer).equals(normalizeString(currentWord.getWord()))) {
            soundManager.playCorrectSound();
            resultText.setText(R.string.correct);
            score++;
            wordsCompleted++;
            updateScore();
            
            // Animate score increase
            scoreText.animate()
                    .scaleX(1.5f)
                    .scaleY(1.5f)
                    .setDuration(200)
                    .withEndAction(() -> {
                        scoreText.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(200)
                                .start();
                    })
                    .start();

            // Check for level completion
            if (wordsCompleted >= WORDS_PER_LEVEL) {
                showLevelCompleteDialog();
            } else {
                // Wait a moment before showing next word
                wordInput.postDelayed(this::showNextWord, 1500);
            }
        } else {
            soundManager.playIncorrectSound();
            resultText.setText(R.string.incorrect);
            
            // Shake animation for wrong answer
            wordInput.animate()
                    .translationX(20f)
                    .setDuration(100)
                    .withEndAction(() -> {
                        wordInput.animate()
                                .translationX(-20f)
                                .setDuration(100)
                                .withEndAction(() -> {
                                    wordInput.animate()
                                            .translationX(0f)
                                            .setDuration(100)
                                            .start();
                                })
                                .start();
                    })
                    .start();
        }
    }

    private String normalizeString(String input) {
        return input.replaceAll("[éèêë]", "e")
                    .replaceAll("[àâä]", "a")
                    .replaceAll("[ïî]", "i")
                    .replaceAll("[ôö]", "o")
                    .replaceAll("[ûüù]", "u")
                    .replaceAll("[ç]", "c");
    }

    private void updateScore() {
        scoreText.setText(getString(R.string.score, score));
    }

    private void showLevelCompleteDialog() {
        String levelType = currentLevel <= 3 ? "A1" : "A2";
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.level_complete))
                .setMessage(getString(R.string.congratulations) + "\n" +
                        "Niveau DELF " + levelType + "\n" +
                        getString(R.string.final_score, score))
                .setPositiveButton(getString(R.string.next_level), (dialog, which) -> {
                    currentLevel++;
                    wordsCompleted = 0;
                    showNextWord();
                })
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (soundManager != null) {
            soundManager.release();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (soundManager == null) {
            soundManager = new SoundManager(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundManager != null) {
            soundManager.release();
            soundManager = null;
        }
    }
} 