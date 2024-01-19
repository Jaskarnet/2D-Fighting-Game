package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.commands.Player;
import com.mygdx.engine.Collision;
import com.mygdx.entities.Entity;
import com.mygdx.entities.Fighter;
import com.mygdx.entities.State;
import com.mygdx.game.FightingGame;
import com.mygdx.game.GameState;
import com.mygdx.utils.GameAssetManager;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import java.util.ArrayList;
import java.util.Collection;

public class OfflineGameScreen implements Screen {
    FightingGame game;
    private Collection<Entity> entities;
    private Collision collision;
    private GameState gameState;
    private Texture backgroundTexture, p1Movelist, p2Movelist;
    private float countdownTime = 5f;
    private boolean isCountdownActive = true;
    private float fightMessageTime = 0.5f;
    private boolean isFightMessageActive = false;
    private BitmapFont countdownFont;
    private GlyphLayout layout;
    private boolean isWinnerMessageActive = false;
    private float winnerMessageTime = 2.0f;
    private String winnerMessage = "";
    private ShapeRenderer shapeRenderer;
    private float player1DepleteHealth = 3;
    private float player2DepleteHealth = 3;
    private boolean isRoundWonActive = false;
    private float roundWonAnimationTime = 0.5f;
    private float totalRoundWonAnimationTime = 0.5f;
    private int player1RoundsWon, player2RoundsWon;
    private Music gameMusic;
    private Sound soundThree, soundTwo, soundOne;
    private boolean oneSound = true, twoSound = true, threeSound = true;
    private int frameIndex = 1;
    private float timeAccumulator = 0f;
    private static final float FRAME_DURATION = 1.0f;
    private static final int MAX_FRAMES = 71;
    private ArrayList<Texture> frameTextures = new ArrayList<Texture>();
    private Sound walkSound1;
    private boolean isWalkSoundPlaying1;
    private Sound walkSound2;
    private boolean isWalkSoundPlaying2;
    private Sound punch1, punch2, death;
    private boolean isPunch1Playing, isPunch2Playing, isDeathPlaying;



    public OfflineGameScreen(FightingGame game) {
        this.game = game;
        gameState = GameState.OFFLINE_GAME;

        // Przydzielanie graczy
        game.player1.setPlayer(Player.PLAYER1);
        game.player2.setPlayer(Player.PLAYER2);

        // Inicjalizacja kolekcji bytów i kolizji
        entities = new ArrayList<>();
        entities.add(game.player1);
        entities.add(game.player2);
        collision = new Collision(game.player1, game.player2);

        // Pobieranie załadowanych zasobów za pomocą AssetManagera
        backgroundTexture = game.assetManager.manager.get(GameAssetManager.backgroundImagePath, Texture.class);
        p1Movelist = game.assetManager.manager.get(GameAssetManager.p1MovelistPath, Texture.class);
        p2Movelist = game.assetManager.manager.get(GameAssetManager.p2MovelistPath, Texture.class);
        gameMusic = game.assetManager.manager.get(GameAssetManager.gameMusicPath, Music.class);
        soundThree = game.assetManager.manager.get(GameAssetManager.threeSoundPath, Sound.class);
        soundTwo = game.assetManager.manager.get(GameAssetManager.twoSoundPath, Sound.class);
        soundOne = game.assetManager.manager.get(GameAssetManager.oneSoundPath, Sound.class);
        walkSound1 = game.assetManager.manager.get(GameAssetManager.walk1SoundPath, Sound.class);
        walkSound2 = game.assetManager.manager.get(GameAssetManager.walk2SoundPath, Sound.class);
        isWalkSoundPlaying1 = false;
        punch1 = game.assetManager.manager.get(GameAssetManager.punch1SoundPath, Sound.class);
        punch2 = game.assetManager.manager.get(GameAssetManager.punch2SoundPath, Sound.class);
        death = game.assetManager.manager.get(GameAssetManager.deathSoundPath, Sound.class);
        isPunch1Playing = isPunch2Playing = isDeathPlaying = false;
        // Ustawienia czcionki
        countdownFont = game.assetManager.manager.get(GameAssetManager.fontPath, BitmapFont.class);
        countdownFont.setColor(Color.WHITE);
        countdownFont.getData().setScale(10);

        // Inicjalizacja layoutu i ShapeRenderera
        layout = new GlyphLayout();
        shapeRenderer = new ShapeRenderer();

        // Inicjalizacja muzyki
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.2f);
        gameMusic.play();

        // Ustawienia animacji tła
        frameTextures = new ArrayList<>();
        for (int i = 1; i <= MAX_FRAMES; i++) {
            String framePath = "sunset/" + String.format("%04d.jpg", i);
            frameTextures.add(game.assetManager.manager.get(framePath, Texture.class));
        }
        backgroundTexture = frameTextures.get(0);
    }

    private void updateBackground(float delta) {
        timeAccumulator += delta;

        if (timeAccumulator >= FRAME_DURATION) {
            timeAccumulator -= FRAME_DURATION; // Reset the accumulator

            // Set the current background to the next frame
            frameIndex++;
            if (frameIndex >= MAX_FRAMES) {
                frameIndex = MAX_FRAMES; // Reset to the last frame if it goes over
            }

            // Assuming frameTextures contains all your sunset images
            backgroundTexture = frameTextures.get(frameIndex - 1);
        }
    }

    @Override
    public void show() {
        gameMusic.play();
    }

    @Override
    public void render(float delta) {
        updateBackground(delta);
        checkWinCondition();

        updateRoundWonAnimation(delta);
        updateCountdown(delta);
        updateFightMessage(delta);
        updateWinnerMessage(delta);
        animateHealthDepletion(delta);
        clearScreen();
        updateEntities();
        renderGame();
        drawCountdown();
        drawFightMessage();
        drawWinnerMessage();
        //drawHitboxesAndHurtboxes();
    }


    private void checkWinCondition() {
        if (game.player1.getRoundsWon() >= 3 && game.player2.getRoundsWon() >= 3 && game.player1.getState() == State.HIT_STUNNED_HIGH && game.player2.getState() == State.HIT_STUNNED_HIGH) {
            winnerMessage = "Draw!";
            isWinnerMessageActive = true;
            if (winnerMessageTime == 2.0f) isRoundWonActive = true;
        } else if (game.player1.getRoundsWon() >= 3 && game.player2.getState() == State.HIT_STUNNED_HIGH) {
            winnerMessage = "Player 1 wins!";
            isWinnerMessageActive = true;
            if (winnerMessageTime == 2.0f) isRoundWonActive = true;
        } else if (game.player2.getRoundsWon() >= 3 && game.player1.getState() == State.HIT_STUNNED_HIGH) {
            winnerMessage = "Player 2 wins!";
            isWinnerMessageActive = true;
            if (winnerMessageTime == 2.0f) isRoundWonActive = true;
        } else if (game.player1.getState() == State.HIT_STUNNED_HIGH || game.player2.getState() == State.HIT_STUNNED_HIGH) {
            if (game.player1.getState() == State.HIT_STUNNED_HIGH && winnerMessageTime == 2.0f) game.player2.setRoundsWon(game.player2.getRoundsWon() + 1);
            if (game.player2.getState() == State.HIT_STUNNED_HIGH && winnerMessageTime == 2.0f) game.player1.setRoundsWon(game.player1.getRoundsWon() + 1);
            winnerMessage = String.format("%d - %d", game.player1.getRoundsWon(), game.player2.getRoundsWon());
            isWinnerMessageActive = true;
            if (winnerMessageTime == 2.0f) isRoundWonActive = true;
        }
    }

    private void updateRoundWonAnimation(float delta) {
        if (isRoundWonActive) {
            roundWonAnimationTime -= delta;
            if (roundWonAnimationTime <= 0) {
                isRoundWonActive = false;
                roundWonAnimationTime = totalRoundWonAnimationTime;
            }
        }
    }

    private void updateWinnerMessage(float delta) {
        if (isWinnerMessageActive) {
            winnerMessageTime -= delta;
            if (winnerMessageTime <= 0) {
                isWinnerMessageActive = false;
                winnerMessageTime = 2.0f;
                if (game.player1.getRoundsWon() >= 3 || game.player2.getRoundsWon() >= 3) {
                    dispose();
                    game.setScreen(new MainMenuScreen(game));
                }
                else startNewRound();
            }
        }
    }

    private void drawWinnerMessage() {
        if (isWinnerMessageActive) {
            drawTextCentered(winnerMessage);
        }
    }

    private void startNewRound() {
        game.player1.resetToDefault();
        game.player2.resetToDefault();
        player1DepleteHealth = game.player1.getMaxHealth();
        player2DepleteHealth = game.player2.getMaxHealth();
        player1RoundsWon = game.player1.getRoundsWon();
        player2RoundsWon = game.player2.getRoundsWon();

        isCountdownActive = true;
    }

    private void updateCountdown(float delta) {
        if (isCountdownActive) {
            countdownTime -= delta;
            if (countdownTime <= 3 && threeSound) {
                soundThree.play(0.2f);
                threeSound = false;
            }
            if (countdownTime <= 2 && twoSound) {
                soundTwo.play(0.2f);
                twoSound = false;
            }
            if (countdownTime <= 1 && oneSound) {
                soundOne.play(0.2f);
                oneSound = false;
            }
            if (countdownTime <= 0) {
                isCountdownActive = false;
                isFightMessageActive = true;
                oneSound = twoSound = threeSound = true;
                countdownTime = 3;
            }
        }
    }

    private void updateFightMessage(float delta) {
        if (isFightMessageActive) {
            fightMessageTime -= delta;
            if (fightMessageTime <= 0) {
                isFightMessageActive = false;
                fightMessageTime = 0.5f;
            }
        }
    }

    private void clearScreen() {
        ScreenUtils.clear(1, 1, 1, 1);
    }

    private void updateEntities() {
        if (isCountdownActive || isFightMessageActive) {
            game.player1.updateIdle();
            game.player2.updateIdle();
        } else if (isWinnerMessageActive) {
            game.player1.updateRoundEnd();
            game.player2.updateRoundEnd();
        } else {
            for (Entity entity : entities) {
                entity.update();
            }
            collision.update();
        }
        if (!isWalkSoundPlaying1 && (game.player1.getState() == State.GOING_BACK || game.player1.getState() == State.GOING_FORWARD)) {
            System.out.println(game.player1.getPlayer() + "play");
            walkSound1.loop();
            isWalkSoundPlaying1 = true;
        } else if (isWalkSoundPlaying1 && !(game.player1.getState() == State.GOING_BACK || game.player1.getState() == State.GOING_FORWARD)) {
            System.out.println(game.player1.getPlayer() + "stop");
            walkSound1.stop();
            isWalkSoundPlaying1 = false;
        }
        if (!isWalkSoundPlaying2 && (game.player2.getState() == State.GOING_BACK || game.player2.getState() == State.GOING_FORWARD)) {
            System.out.println(game.player2.getPlayer() + "play");
            walkSound2.loop();
            isWalkSoundPlaying2 = true;
        } else if (isWalkSoundPlaying2 && !(game.player2.getState() == State.GOING_BACK || game.player2.getState() == State.GOING_FORWARD)){
            System.out.println(game.player2.getPlayer() + "stop");
            walkSound2.stop();
            isWalkSoundPlaying2 = false;
        }

        if (!isPunch1Playing && (game.player1.getState() == State.BLOCK_STUNNED_HIGH || game.player1.getState() == State.BLOCK_STUNNED_MID || game.player1.getState() == State.BLOCK_STUNNED_LOW || game.player2.getState() == State.BLOCK_STUNNED_HIGH || game.player2.getState() == State.BLOCK_STUNNED_MID || game.player2.getState() == State.BLOCK_STUNNED_LOW)) {
            punch1.play();
            System.out.println("Punch1 play");
            isPunch1Playing = true;
        } else if (isPunch1Playing && !(game.player1.getState() == State.BLOCK_STUNNED_HIGH || game.player1.getState() == State.BLOCK_STUNNED_MID || game.player1.getState() == State.BLOCK_STUNNED_LOW || game.player2.getState() == State.BLOCK_STUNNED_HIGH || game.player2.getState() == State.BLOCK_STUNNED_MID || game.player2.getState() == State.BLOCK_STUNNED_LOW)) {
            isPunch1Playing = false;
        }

        if (!isPunch2Playing && (game.player1.getState() == State.HIT_STUNNED_HIGH || game.player1.getState() == State.HIT_STUNNED_MID || game.player1.getState() == State.HIT_STUNNED_LOW || game.player2.getState() == State.HIT_STUNNED_HIGH || game.player2.getState() == State.HIT_STUNNED_MID || game.player2.getState() == State.HIT_STUNNED_LOW)) {
            punch2.play(0.8f);
            System.out.println("Punch2 play");
            isPunch2Playing = true;
        } else if (isPunch2Playing && !(game.player1.getState() == State.HIT_STUNNED_HIGH || game.player1.getState() == State.HIT_STUNNED_MID || game.player1.getState() == State.HIT_STUNNED_LOW || game.player2.getState() == State.HIT_STUNNED_HIGH || game.player2.getState() == State.HIT_STUNNED_MID || game.player2.getState() == State.HIT_STUNNED_LOW)) {
            isPunch2Playing = isDeathPlaying = false;
        }

        if (!isDeathPlaying && (game.player1.getState() == State.HIT_STUNNED_HIGH || game.player2.getState() == State.HIT_STUNNED_HIGH)) {
            death.play();
            System.out.println("Death play");
            isDeathPlaying = true;
        } else if (isDeathPlaying && !(game.player1.getState() == State.HIT_STUNNED_HIGH || game.player2.getState() == State.HIT_STUNNED_HIGH)) {
            isDeathPlaying = false;
        }
    }

    private void renderGame() {
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.draw(p1Movelist, 40, Gdx.graphics.getHeight() - 60 - 300, 200, 300);
        game.batch.draw(p2Movelist, Gdx.graphics.getWidth() - 40 - 200, Gdx.graphics.getHeight() - 60 - 300, 200, 300);
        for (Entity entity : entities) {
            game.batch.draw(entity.getTextureRegion(), entity.getX(), entity.getY());
        }
        game.batch.end();
        renderHealthBars();
    }

    private void renderHealthBars() {
        // Start the ShapeRenderer to draw the health bars
        shapeRenderer.setProjectionMatrix(game.batch.getProjectionMatrix());

        // Constants for the health bars
        float maxHealthWidth = 450; // The total width of the health bar
        float healthBarHeight = 20; // The height of the health bar
        float healthBarTopMargin = 20; // Margin from the top of the screen
        float healthBarSideMargin = 40; // Margin from the sides of the screen
        float borderThickness = 3; // Thickness of the health bar border

        // Gradient Colors
        Color borderColor = Color.BLACK; // Color for the border
        Color backgroundHealthColor = Color.DARK_GRAY; // Color for the background
        Color startHealthColor = Color.GREEN; // Starting color of the gradient
        Color endHealthColor = Color.FOREST; // Ending color of the gradient

        // Draw Player 1 health bar with border
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw the border
        shapeRenderer.setColor(borderColor);
        shapeRenderer.rect(healthBarSideMargin - borderThickness, Gdx.graphics.getHeight() - healthBarHeight - healthBarTopMargin - borderThickness, maxHealthWidth + borderThickness * 2, healthBarHeight + borderThickness * 2);

        // Draw the background
        shapeRenderer.setColor(backgroundHealthColor);
        shapeRenderer.rect(healthBarSideMargin, Gdx.graphics.getHeight() - healthBarHeight - healthBarTopMargin, maxHealthWidth, healthBarHeight);

        // Draw Player 1's depleting health
        float player1DepleteWidth = (maxHealthWidth / game.player1.getMaxHealth()) * player1DepleteHealth;
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(healthBarSideMargin, Gdx.graphics.getHeight() - healthBarHeight - healthBarTopMargin, player1DepleteWidth, healthBarHeight);

        // Draw the current health with gradient
        float player1HealthWidth = (maxHealthWidth / 3) * game.player1.getHealth(); // Calculate the width based on current health
        // Starting point of the gradient
        shapeRenderer.rect(healthBarSideMargin, Gdx.graphics.getHeight() - healthBarHeight - healthBarTopMargin, player1HealthWidth, healthBarHeight, endHealthColor, endHealthColor, startHealthColor, startHealthColor);

        // Draw the border
        shapeRenderer.setColor(borderColor);
        shapeRenderer.rect(Gdx.graphics.getWidth() - maxHealthWidth - healthBarSideMargin - borderThickness, Gdx.graphics.getHeight() - healthBarHeight - healthBarTopMargin - borderThickness, maxHealthWidth + borderThickness * 2, healthBarHeight + borderThickness * 2);

        // Draw the background
        shapeRenderer.setColor(backgroundHealthColor);
        shapeRenderer.rect(Gdx.graphics.getWidth() - maxHealthWidth - healthBarSideMargin, Gdx.graphics.getHeight() - healthBarHeight - healthBarTopMargin, maxHealthWidth, healthBarHeight);

        // Draw Player 2's depleting health
        shapeRenderer.setColor(Color.RED);
        float player2DepleteWidth = (maxHealthWidth / game.player2.getMaxHealth()) * player2DepleteHealth;
        shapeRenderer.rect(Gdx.graphics.getWidth() - player2DepleteWidth - healthBarSideMargin, Gdx.graphics.getHeight() - healthBarHeight - healthBarTopMargin, player2DepleteWidth, healthBarHeight);

        // Draw the current health with gradient
        float player2HealthWidth = (maxHealthWidth / 3) * game.player2.getHealth(); // Calculate the width based on current health
        // Ending point of the gradient because it's drawing from right to left
        shapeRenderer.rect(Gdx.graphics.getWidth() - player2HealthWidth - healthBarSideMargin, Gdx.graphics.getHeight() - healthBarHeight - healthBarTopMargin, player2HealthWidth, healthBarHeight, endHealthColor, endHealthColor, startHealthColor, startHealthColor);


        shapeRenderer.end();
        renderRoundCounters(maxHealthWidth, healthBarHeight, healthBarTopMargin, healthBarSideMargin);
    }

    private void animateHealthDepletion(float delta) {
        // Constants
        float depleteSpeed = 2; // The speed at which the health depletes

        // Animate Player 1's health depleting
        if (player1DepleteHealth > game.player1.getHealth()) {
            player1DepleteHealth -= depleteSpeed * delta;
            if (player1DepleteHealth < game.player1.getHealth()) {
                player1DepleteHealth = game.player1.getHealth();
            }
        }

        // Animate Player 2's health depleting
        if (player2DepleteHealth > game.player2.getHealth()) {
            player2DepleteHealth -= depleteSpeed * delta;
            if (player2DepleteHealth < game.player2.getHealth()) {
                player2DepleteHealth = game.player2.getHealth();
            }
        }
    }

    private void renderRoundCounters(float healthBarWidth, float healthBarHeight, float healthBarTopMargin, float healthBarSideMargin) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Constants for the round counters
        float counterDiameter = 20; // The diameter of the round counter circles
        float counterMargin = 8; // The margin between the circles
        float counterYPosition = Gdx.graphics.getHeight() - healthBarHeight - healthBarTopMargin - counterDiameter;
        float borderThickness = 2; // Thickness of the border around the counters
        Color borderColor = Color.BLACK; // Color for the border
        Color emptyCounterColor = Color.GRAY;
        Color outerCounterColor = Color.GOLD;
        Color centerCounterColor = new Color(1f, 1f, 0.8f, 1f);

        // Calculate the starting x position for players round counters
        float counterXStartP1 = healthBarWidth + healthBarSideMargin - counterMargin;
        float counterXStartP2 = Gdx.graphics.getWidth() - (healthBarWidth + healthBarSideMargin - counterMargin);

        // Draw Player 1 round counters
        drawCounters(borderColor, emptyCounterColor, outerCounterColor, centerCounterColor, counterXStartP1, counterDiameter, counterMargin, counterYPosition, borderThickness, 1, game.player1);
        drawCounters(borderColor, emptyCounterColor, outerCounterColor, centerCounterColor, counterXStartP2, counterDiameter, counterMargin, counterYPosition, borderThickness, -1, game.player2);

        shapeRenderer.end();
    }

    private Color calculateColor(float remainingTime, float totalAnimationDuration, Color startColor, Color midColor, Color endColor) {
        // Oblicz postęp na podstawie pozostałego czasu i całkowitego czasu trwania
        float progress = 1.0f - (remainingTime / totalAnimationDuration);
        float midPoint = 0.5f; // Możesz dostosować ten punkt środkowy

        if (progress < midPoint) {
            // Skalowanie postępu dla pierwszej połowy animacji (od startColor do midColor)
            float scaledProgress = progress / midPoint;
            return interpolateColor(startColor, midColor, scaledProgress);
        } else {
            // Skalowanie postępu dla drugiej połowy animacji (od midColor do endColor)
            float scaledProgress = (progress - midPoint) / (1 - midPoint);
            return interpolateColor(midColor, endColor, scaledProgress);
        }
    }

    private Color interpolateColor(Color color1, Color color2, float progress) {
        // Oblicz interpolowany kolor
        float r = color1.r + (color2.r - color1.r) * progress;
        float g = color1.g + (color2.g - color1.g) * progress;
        float b = color1.b + (color2.b - color1.b) * progress;
        return new Color(r, g, b, 1);
    }


    private void drawCounters(Color borderColor, Color emptyCounterColor, Color outerCounterColor, Color centerCounterColor, float counterXStart, float counterDiameter, float counterMargin, float counterYPosition, float borderThickness, int direction, Fighter player) {
        for (int i = 0; i < 3; i++) {
            float x = counterXStart - (counterDiameter + counterMargin) * direction * i;
            // Draw border
            shapeRenderer.setColor(borderColor);
            shapeRenderer.circle(x, counterYPosition, counterDiameter / 2 + borderThickness);

            // Simulate a gradient by drawing concentric circles
            if (player.getRoundsWon() > i) {
                if (isRoundWonActive && i == player.getRoundsWon() - 1) {
                    if (player1RoundsWon < game.player1.getRoundsWon() && (player.getPlayer() == Player.PLAYER1 || player.getPlayer() == Player.ONLINE_PLAYER1)) {
                        outerCounterColor = calculateColor(roundWonAnimationTime, totalRoundWonAnimationTime, Color.GRAY, Color.WHITE, Color.GOLD);
                        centerCounterColor = calculateColor(roundWonAnimationTime, totalRoundWonAnimationTime, Color.GRAY, Color.WHITE, new Color(1f, 1f, 0.8f, 1f));

                    } else if (player2RoundsWon < game.player2.getRoundsWon() && (player.getPlayer() == Player.PLAYER2 || player.getPlayer() == Player.ONLINE_PLAYER2)) {
                        outerCounterColor = calculateColor(roundWonAnimationTime, totalRoundWonAnimationTime, Color.GRAY, Color.WHITE, Color.GOLD);
                        centerCounterColor = calculateColor(roundWonAnimationTime, totalRoundWonAnimationTime, Color.GRAY, Color.WHITE, new Color(1f, 1f, 0.8f, 1f));

                    }
                }
                int gradientSteps = 10;
                for (int j = gradientSteps; j > 0; j--) {
                    float stepRadius = counterDiameter / 2 * (j / (float) gradientSteps);
                    Color stepColor = new Color(
                            outerCounterColor.r + (centerCounterColor.r - outerCounterColor.r) * (1 - j / (float) gradientSteps),
                            outerCounterColor.g + (centerCounterColor.g - outerCounterColor.g) * (1 - j / (float) gradientSteps),
                            outerCounterColor.b + (centerCounterColor.b - outerCounterColor.b) * (1 - j / (float) gradientSteps),
                            1
                    );
                    shapeRenderer.setColor(stepColor);
                    shapeRenderer.circle(x, counterYPosition, stepRadius);
                }
            } else {
                // Draw empty counter
                shapeRenderer.setColor(emptyCounterColor);
                shapeRenderer.circle(x, counterYPosition, counterDiameter / 2);
            }
        }
    }


    private void drawCountdown() {
        if (isCountdownActive) {
            drawTextCentered(String.format("%.0f", Math.ceil(countdownTime)));
        }
    }

    private void drawFightMessage() {
        if (isFightMessageActive) {
            drawTextCentered("FIGHT!");
        }
    }

    private void drawTextCentered(String text) {
        layout.setText(countdownFont, text);
        float x = Gdx.graphics.getWidth() / 2f - layout.width / 2;
        float y = Gdx.graphics.getHeight() / 2f + layout.height / 2;
        game.batch.begin();
        countdownFont.draw(game.batch, text, x, y);
        game.batch.end();
    }

    private void drawHitboxesAndHurtboxes() {
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (Rectangle box : game.player1.getHurtboxes()) {
            game.shapeRenderer.setColor(Color.BLACK);
            game.shapeRenderer.rect(box.x, box.y, box.width, box.height);
        }

        for (Rectangle box : game.player2.getHurtboxes()) {
            game.shapeRenderer.setColor(Color.BLACK);
            game.shapeRenderer.rect(box.x, box.y, box.width, box.height);
        }

        for (Rectangle box : game.player1.getHitboxes()) {
            game.shapeRenderer.setColor(Color.RED);
            game.shapeRenderer.rect(box.x, box.y, box.width, box.height);
        }

        for (Rectangle box : game.player2.getHitboxes()) {
            game.shapeRenderer.setColor(Color.RED);
            game.shapeRenderer.rect(box.x, box.y, box.width, box.height);
        }

        game.shapeRenderer.end();
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        System.out.println("~dispose(OfflineGameScreen)");
        game.player1.resetToInitial();
        game.player2.resetToInitial();
        countdownFont.getData().setScale(1);
        gameMusic.stop();
        walkSound1.stop();
        walkSound2.stop();
    }
}
