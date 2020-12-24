package com.asteroids;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Sprite {

    public Vector position;
    public Vector velocity;
    public double rotation; // degrees
    public HitBox bounds;
    public double timeOnScreen; // seconds
    public int spin;
    public boolean isWarping;

    public Image sprite;


    public Sprite() {
        this.position = new Vector();
        this.velocity = new Vector();
        this.rotation = 0;
        this.bounds = new HitBox();
        timeOnScreen = 0;
        spin = 1;
        isWarping = true;
    }

    public Sprite(String imageFilename) {
        this(); // runs default constructor
        this.setImage(imageFilename);

    }

    public Sprite(String imageFilename, double requestedWidth, double requestedHeight, boolean preserveRatio, boolean smooth) {
        this(); // runs default constructor
        this.sprite = new Image(imageFilename, requestedWidth, requestedHeight, preserveRatio, smooth);

    }

    public void setSpin(int spin) {
            this.spin = spin;
    }


    public void setImage(String imageFilename) {
        this.sprite = new Image(imageFilename);
        this.bounds.setSize(this.sprite.getWidth(), this.sprite.getHeight());
    }

    public void setWarping(boolean warping) {
        this.isWarping = warping;
    }

    public HitBox getBounds() {
        this.bounds.setPosition(this.position.x, this.position.y); // updates position
        return this.bounds;
    }

    public boolean overlaps(Sprite other_sprite) {
        return this.getBounds().overlaps(other_sprite.getBounds());
    }

    public boolean overlaps(Rectangle other_sprite) {
        return this.getBounds().overlaps(other_sprite);
    }

    public void update(double deltaTime) {
        // update time on screen
        this.timeOnScreen += deltaTime;
        // updates position according to displacement over time (aka velocity)
        this.position.add(this.velocity.x * deltaTime, this.velocity.y * deltaTime);

        // enable wrapping
        if (isWarping)
            this.wrap(1280, 696);
    }

    public void wrap(double screenWidth, double screenHeight) {
        if (this.position.x + this.sprite.getWidth() / 2 < 0)
            this.position.x = screenWidth + this.sprite.getWidth() / 2;

        if (this.position.x > screenWidth + this.sprite.getWidth() / 2)
            this.position.x = -this.sprite.getWidth() / 2;

        if (this.position.y + this.sprite.getHeight() / 2 < 0)
            this.position.y = screenHeight + this.sprite.getHeight() / 2;

        if (this.position.y > screenHeight + this.sprite.getHeight() / 2)
            this.position.y = -this.sprite.getHeight() / 2;
    }

    public void render(GraphicsContext context) {
        context.save();

        context.translate(this.position.x, this.position.y);
        context.rotate(this.rotation);
        context.translate(-this.sprite.getWidth() / 2, -this.sprite.getHeight() / 2); //34:30s
        context.drawImage(this.sprite, 0, 0);

        context.restore();
    }

}
