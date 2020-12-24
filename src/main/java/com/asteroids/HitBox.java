package com.asteroids;

import javafx.scene.shape.Rectangle;

public class HitBox { // aka rectangle

    double x;
    double y;

    double width;
    double height;

    public HitBox() {
        setPosition(0,0);
        setSize(1,1);
    }

    public HitBox(double x, double y, double width, double height) {
        setPosition(x, y);
        setSize(width,height);
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(double width, double height) {
        this.height = height;
        this.width = width;
    }

    public boolean overlaps(HitBox other_object ) {
        boolean isOverlapping = this.x + this.width < other_object.x ||
                        other_object.x + other_object.width < this.x ||
                        this.y + this.height < other_object.y        ||
                        other_object.y + other_object.height < this.y;
        return !isOverlapping;
    }

    public boolean overlaps(Rectangle other_object ) {
        boolean isOverlapping = false;

        Rectangle rectangle = toRectangle();
        if (rectangle.intersects(other_object.getBoundsInLocal()))
            isOverlapping = true;

        return isOverlapping;
    }

    public Rectangle toRectangle() {
        return new Rectangle(this.x, this.y, this.width, this.height);
    }

    public double getArea() {
        return width * height;
    }

}
