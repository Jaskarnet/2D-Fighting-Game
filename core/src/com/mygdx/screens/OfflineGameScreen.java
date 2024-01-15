package com.mygdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.mygdx.utils.ImageFlipper;
import com.mygdx.utils.json.MoveFileReader;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import java.util.ArrayList;
import java.util.Collection;

public class OfflineGameScreen implements Screen {
    FightingGame game;
    private Collection<Entity> entities;
    private Fighter player1, player2;
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
    private Sound soundThree, soundTwo, soundOne, punch1, punch2, punch3;
    private boolean oneSound = true, twoSound = true, threeSound = true, punch1Sound = false, punch2Sound = false, punch3Sound = false;
    private int frameIndex = 1;
    private float timeAccumulator = 0f;
    private static final float FRAME_DURATION = 1.0f;
    private static final int MAX_FRAMES = 71;
    private ArrayList<Texture> frameTextures = new ArrayList<Texture>();




    public OfflineGameScreen(FightingGame game) {
        this.game = game;
        gameState = GameState.OFFLINE_GAME;
        /*MoveFileReader moveFileReader = new MoveFileReader();
        moveFileReader.copyAndReverseMoveInfo("moves/Fighter1/moveinfo", "moves/Fighter2/moveinfo");
        ImageFlipper.flipImagesHorizontally("moves/Fighter1/spritesheets", "moves/Fighter2/spritesheets");*/

        player1 = new Fighter(250, 20, Player.PLAYER1, Input.Keys.A, Input.Keys.D, Input.Keys.S, Input.Keys.F, 600);
        player2 = new Fighter(650, 20, Player.PLAYER2, Input.Keys.RIGHT, Input.Keys.LEFT, Input.Keys.DOWN, Input.Keys.CONTROL_RIGHT, 600);

        entities = new ArrayList<>();
        entities.add(player1);
        entities.add(player2);
        collision = new Collision(player1, player2);
        backgroundTexture = new Texture(Gdx.files.internal("sunsetmid.jpg"));
        p1Movelist = new Texture(Gdx.files.internal("p1movelist.png"));
        p2Movelist = new Texture(Gdx.files.internal("p2movelist.png"));
        countdownFont = new BitmapFont();
        countdownFont.setColor(Color.WHITE);
        countdownFont.getData().setScale(10);
        layout = new GlyphLayout();
        this.shapeRenderer = new ShapeRenderer();
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("risk.mp3"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(0.2f);
        soundThree = Gdx.audio.newSound(Gdx.files.internal("three.mp3"));
        soundTwo = Gdx.audio.newSound(Gdx.files.internal("two.mp3"));
        soundOne = Gdx.audio.newSound(Gdx.files.internal("one.mp3"));
        for (int i = 1; i <= MAX_FRAMES; i++) {
            String framePath = "sunset/" + String.format("%04d.jpg", i);
            frameTextures.add(new Texture(Gdx.files.internal(framePath)));
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
        if (player1.getRoundsWon() >= 3 && player2.getRoundsWon() >= 3 && player1.getState() == State.HIT_STUNNED_HIGH && player2.getState() == State.HIT_STUNNED_HIGH) {
            winnerMessage = "Draw!";
            isWinnerMessageActive = true;
            if (winnerMessageTime == 2.0f) isRoundWonActive = true;
        } else if (player1.getRoundsWon() >= 3 && player2.getState() == State.HIT_STUNNED_HIGH) {
            winnerMessage = "Player 1 wins!";
            isWinnerMessageActive = true;
            if (winnerMessageTime == 2.0f) isRoundWonActive = true;
        } else if (player2.getRoundsWon() >= 3 && player1.getState() == State.HIT_STUNNED_HIGH) {
            winnerMessage = "Player 2 wins!";
            isWinnerMessageActive = true;
            if (winnerMessageTime == 2.0f) isRoundWonActive = true;
        } else if (player1.getState() == State.HIT_STUNNED_HIGH || player2.getState() == State.HIT_STUNNED_HIGH) {
            if (player1.getState() == State.HIT_STUNNED_HIGH && winnerMessageTime == 2.0f) player2.setRoundsWon(player2.getRoundsWon() + 1);
            if (player2.getState() == State.HIT_STUNNED_HIGH && winnerMessageTime == 2.0f) player1.setRoundsWon(player1.getRoundsWon() + 1);
            winnerMessage = String.format("%d - %d", player1.getRoundsWon(), player2.getRoundsWon());
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
                if (player1.getRoundsWon() >= 3 || player2.getRoundsWon() >= 3) {
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
        player1.resetToDefault();
        player2.resetToDefault();
        player1DepleteHealth = player1.getMaxHealth();
        player2DepleteHealth = player2.getMaxHealth();
        player1RoundsWon = player1.getRoundsWon();
        player2RoundsWon = player2.getRoundsWon();

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
            player1.updateIdle();
            player2.updateIdle();
        } else if (isWinnerMessageActive) {
            player1.updateRoundEnd();
            player2.updateRoundEnd();
        } else {
            for (Entity entity : entities) {
                entity.update();
            }
            collision.update();
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
        float player1DepleteWidth = (maxHealthWidth / player1.getMaxHealth()) * player1DepleteHealth;
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(healthBarSideMargin, Gdx.graphics.getHeight() - healthBarHeight - healthBarTopMargin, player1DepleteWidth, healthBarHeight);

        // Draw the current health with gradient
        float player1HealthWidth = (maxHealthWidth / 3) * player1.getHealth(); // Calculate the width based on current health
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
        float player2DepleteWidth = (maxHealthWidth / player2.getMaxHealth()) * player2DepleteHealth;
        shapeRenderer.rect(Gdx.graphics.getWidth() - player2DepleteWidth - healthBarSideMargin, Gdx.graphics.getHeight() - healthBarHeight - healthBarTopMargin, player2DepleteWidth, healthBarHeight);

        // Draw the current health with gradient
        float player2HealthWidth = (maxHealthWidth / 3) * player2.getHealth(); // Calculate the width based on current health
        // Ending point of the gradient because it's drawing from right to left
        shapeRenderer.rect(Gdx.graphics.getWidth() - player2HealthWidth - healthBarSideMargin, Gdx.graphics.getHeight() - healthBarHeight - healthBarTopMargin, player2HealthWidth, healthBarHeight, endHealthColor, endHealthColor, startHealthColor, startHealthColor);


        shapeRenderer.end();
        renderRoundCounters(maxHealthWidth, healthBarHeight, healthBarTopMargin, healthBarSideMargin);
    }

    private void animateHealthDepletion(float delta) {
        // Constants
        float depleteSpeed = 2; // The speed at which the health depletes

        // Animate Player 1's health depleting
        if (player1DepleteHealth > player1.getHealth()) {
            player1DepleteHealth -= depleteSpeed * delta;
            if (player1DepleteHealth < player1.getHealth()) {
                player1DepleteHealth = player1.getHealth();
            }
        }

        // Animate Player 2's health depleting
        if (player2DepleteHealth > player2.getHealth()) {
            player2DepleteHealth -= depleteSpeed * delta;
            if (player2DepleteHealth < player2.getHealth()) {
                player2DepleteHealth = player2.getHealth();
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
        drawCounters(borderColor, emptyCounterColor, outerCounterColor, centerCounterColor, counterXStartP1, counterDiameter, counterMargin, counterYPosition, borderThickness, 1, player1);
        drawCounters(borderColor, emptyCounterColor, outerCounterColor, centerCounterColor, counterXStartP2, counterDiameter, counterMargin, counterYPosition, borderThickness, -1, player2);

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
                    if (player1RoundsWon < player1.getRoundsWon() && (player.getPlayer() == Player.PLAYER1 || player.getPlayer() == Player.ONLINE_PLAYER1)) {
                        outerCounterColor = calculateColor(roundWonAnimationTime, totalRoundWonAnimationTime, Color.GRAY, Color.WHITE, Color.GOLD);
                        centerCounterColor = calculateColor(roundWonAnimationTime, totalRoundWonAnimationTime, Color.GRAY, Color.WHITE, new Color(1f, 1f, 0.8f, 1f));

                    } else if (player2RoundsWon < player2.getRoundsWon() && (player.getPlayer() == Player.PLAYER2 || player.getPlayer() == Player.ONLINE_PLAYER2)) {
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

        // Rysowanie hurtboxów gracza 1
        for (Rectangle box : player1.getHurtboxes()) {
            game.shapeRenderer.setColor(Color.BLACK);
            game.shapeRenderer.rect(box.x, box.y, box.width, box.height);
        }

        // Rysowanie hurtboxów gracza 2
        for (Rectangle box : player2.getHurtboxes()) {
            game.shapeRenderer.setColor(Color.BLACK);
            game.shapeRenderer.rect(box.x, box.y, box.width, box.height);
        }

        // Rysowanie hitboxów gracza 1
        for (Rectangle box : player1.getHitboxes()) {
            game.shapeRenderer.setColor(Color.RED);
            game.shapeRenderer.rect(box.x, box.y, box.width, box.height);
        }

        // Rysowanie hitboxów gracza 2
        for (Rectangle box : player2.getHitboxes()) {
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

    //TODO: dispose ShapeRenderer
    @Override
    public void dispose() {
        System.out.println("~dispose(OfflineGameScreen)");
        backgroundTexture.dispose();
        countdownFont.dispose();
        if (gameMusic != null) gameMusic.dispose();
        if (soundThree != null) soundThree.dispose();
        if (soundTwo != null) soundTwo.dispose();
        if (soundOne != null) soundOne.dispose();
        for (Texture frame : frameTextures) {
            frame.dispose();
        }
    }
}
