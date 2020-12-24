package com.asteroids.controllers;

import com.asteroids.Main;
import com.asteroids.Sprite;
import com.asteroids.Vector;
import javafx.animation.AnimationTimer;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Controller implements Initializable {


    public BorderPane root;
    public Canvas canvas;
    public Label scoreLabel;

    Sprite spaceShip;
    Sprite background;
    Sprite gameOverScreen;
    AnimationTimer gameLoop = null;

    boolean isGameOver = false;

    // TODO: Add some more sprites there
    String[] asteroidPath = {"/Asteroids_Sprites/asteroid1.png", "/Asteroids_Sprites/asteroid2.png", "/Asteroids_Sprites/asteroid3.png"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        canvas = new Canvas(1280, 696);
        GraphicsContext context = canvas.getGraphicsContext2D();
        root.setCenter(canvas);

        try {
            background = new Sprite(getClass().getResource("/Other_Sprites/background2.png").toURI().toString()); // background
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        background.position.set(canvas.getWidth() / 2, 1100);

        try {
            gameOverScreen = new Sprite(getClass().getResource("/Other_Sprites/gameOverScreen.png").toURI().toString()); // gameOverScreen
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        gameOverScreen.position.set(canvas.getWidth() / 2, canvas.getHeight() / 2);

        try {
            spaceShip = new Sprite(getClass().getResource("/SpaceShip_Sprites/spaceShip2.png").toURI().toString(), 40, 25, true, false); // ship
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        spaceShip.getBounds().setSize(spaceShip.sprite.getWidth(), spaceShip.sprite.getHeight());
        spaceShip.position.set(100, 300);

        ArrayList<Sprite> projectiles = new ArrayList<>();
        ArrayList<Sprite> asteroids = new ArrayList<>();
        ArrayList<Sprite> explosions = new ArrayList<>();

        final int[] asteroidCount = {(int) (30 * Math.random() + 5)};
        final int[] score = {0};
        scoreLabel.setText("Score: " + score[0]);

        for (int i = 0; i < asteroidCount[0]; i++) { // setting up asteroids
            Sprite asteroid = makeAsteroid(80 * Math.random() + 30, 80 * Math.random() + 30, new Vector(600 * Math.random() + 300, 300 * Math.random() + 100));
            asteroids.add(asteroid);
        }

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long l) {

                // ============== Input handling =================

                Vector direction = new Vector(Main.mousePosition.x - spaceShip.position.x, Main.mousePosition.y - spaceShip.position.y); // follow the mouse rotation
                spaceShip.rotation = direction.getAngle();

                if (Main.keysPressed.contains("A")) {
                    spaceShip.rotation -= 5;
                    Main.keysPressed.remove("A");
                }

                if (Main.keysPressed.contains("D")) {
                    spaceShip.rotation += 5;
                    Main.keysPressed.remove("D");
                }

                // TODO: movement sucks ass! fix it
                if (Main.keysPressed.contains("W")) {
                    Vector a = new Vector(spaceShip.velocity);
                    Vector b = new Vector(0, 12);
                    b.setAngle(spaceShip.rotation);
                    b.setLength(100);

                    Vector c = Vector.add(a, b);

                    spaceShip.velocity.setLength(c.getLength()); //  spaceShip.velocity.setLength(spaceShip.velocity.getLength() + 10);
                    spaceShip.velocity.setAngle(c.getAngle());
                    Main.keysPressed.remove("W");
                }

                if (Main.keysPressedDiscrete.contains("SPACE") || Main.keysPressedDiscrete.contains("MOUSE_1")) {
                    Sprite shot = null;
                    try {
                        shot = new Sprite(getClass().getResource("/Other_Sprites/projectile2.png").toURI().toString());
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    assert shot != null;
                    shot.setWarping(false);
                    shot.position.set(spaceShip.position);
                    shot.velocity.setLength(800);
                    shot.velocity.setAngle(spaceShip.rotation + 1);
                    shot.rotation = spaceShip.rotation;
                    projectiles.add(shot);
                }



                if (isGameOver) {
                    if (Main.keysPressedDiscrete.contains("MOUSE_1")) {
                        System.out.println("X: " + Main.mousePosition.x + " Y: " + Main.mousePosition.y);
                    }

                }

                Main.keysPressedDiscrete.clear();

                // ============== Updating =================

                spaceShip.update(1 / 60.0);

                for (Sprite asteroid : asteroids) { // updating asteroids
                    asteroid.update(1 / 60.0);
                }

                for (int i = 0; i < projectiles.size(); i++) { // updating projectiles
                    Sprite shot = projectiles.get(i);
                    shot.update(1 / 60.0);

                    if (shot.timeOnScreen > 2.4) { // remove projectile after 2.4 seconds
                        projectiles.remove(i);
                    }
                }

                for (int i = 0; i < explosions.size(); i++) { // removing explosion animation after a time
                    Sprite ex = explosions.get(i);
                    ex.update(1 / 60.0);

                    if (ex.timeOnScreen > 0.9) {
                        explosions.remove(ex);
                    }
                }

                // collision detection: projectile with asteroid
                for (int i = 0; i < projectiles.size(); i++) { // hitbox check
                    Sprite shot = projectiles.get(i);

                    for (int j = 0; j < asteroids.size(); j++) {
                        Sprite asteroid = asteroids.get(j);

                        if (asteroid.overlaps(shot.getBounds().toRectangle())) {

                            // explosion ===
                            Sprite explosion = null;
                            try {
                                explosion = new Sprite(getClass().getResource("/Other_Sprites/Boom.gif").toURI().toString());
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                            assert explosion != null;
                            explosion.position.set(asteroid.position.x, asteroid.position.y);
                            explosions.add(explosion);
                            // ==============

                            if (asteroid.getBounds().getArea() > 5000) { // spawning smaller asteroid
                                Sprite smallAsteroid = makeAsteroid(30 * Math.random() + 10, 30 * Math.random() + 10, asteroid.position);
                                asteroids.add(smallAsteroid);
                                asteroidCount[0]++;
                            }

                            projectiles.remove(shot);
                            asteroids.remove(asteroid);
                            score[0] += 5;
                        }
                    }
                }

                // collision detection: spaceship with asteroid
                // TODO: Game over
                for (int i = 0; i < asteroids.size(); i++) {
                    Sprite asteroid = asteroids.get(i);
                    if (spaceShip.overlaps(asteroid.getBounds().toRectangle())) {
                        asteroids.remove(asteroid);
                        scoreLabel.setText("You lost! Game Over!");
                        //System.out.println("Game Over!");
                        gameOver();
                    }
                }

                // ============== Rendering =================

                background.render(context);
                spaceShip.render(context);


                for (Sprite ex : explosions) {
                    ex.render(context);
                }

                for (Sprite shot : projectiles) {
                    shot.render(context);
                }

                for (Sprite asteroid : asteroids) {
                    asteroid.rotation += asteroid.spin * 0.7 * Math.random() + 0.1;
                    asteroid.render(context);
                }

                // TODO: make it better, add some kind of "play again" menu
                if (score[0] == 5 * asteroidCount[0]) {
                    scoreLabel.setText("You Won!");
                } else {
                    scoreLabel.setText("Score: " + score[0]);
                }

            }
        };

       // gameLoop.start();
        newGame();

    }

    // ============ Utility methods ==============

    void gameOver() {
        gameLoop.stop();
    }

    void newGame() {
        gameLoop.start();
    }


    Sprite makeAsteroid(double maxSize, double minSize, Vector parentPosition) {
        double newWidth = maxSize * Math.random() + minSize;
        double newHeight = maxSize * Math.random() + minSize;

        Sprite asteroid = null;
        int selectRandom = (int) (3 * Math.random()); // select random asteroid path from array of 3 asteroid paths
        try {
            asteroid = new Sprite(getClass().getResource(asteroidPath[selectRandom]).toURI().toString(), newWidth, newHeight, true, false);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        assert asteroid != null;
        asteroid.position.set(parentPosition.x, parentPosition.y);
        asteroid.getBounds().setSize(asteroid.sprite.getWidth(), asteroid.sprite.getHeight());

        asteroid.velocity.setLength(550000 / asteroid.getBounds().getArea() * Math.random() + 100);
        asteroid.velocity.setAngle(360 * Math.random());
        asteroid.setSpin((30 * Math.random()) < 15 ? -1 : 1);

        return asteroid;
    }
}




