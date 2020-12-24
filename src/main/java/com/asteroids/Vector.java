package com.asteroids;

public class Vector {

    public double x;
    public double y;

    public Vector() {
        this.set(0, 0);
    }

    public Vector(double x, double y) {
        this.set(x, y);
    }

    public Vector(Vector vector) {
        this.set(vector);
    }

    public Vector getVector() {
        return this;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vector vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    public void add(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public void add(Vector vector) {
        this.x += vector.x;
        this.y += vector.y;
    }

    public static Vector add(Vector vector1, Vector vector2) {
        Vector newVector = new Vector(vector1.x,vector1.y);
        newVector.x += vector2.x;
        newVector.y += vector2.y;
        return newVector;
    }

    public void multiply(double constant) {
        this.x *= constant;
        this.y *= constant;
    }

    public double getLength() {
        return Math.sqrt((Math.pow(x, 2) + Math.pow(y, 2)));
    }

    public void setLength(double length) {
        double currentLength = this.getLength();
        if (currentLength == 0) {
            this.set(length,0);
        }
        else {
            this.multiply(1/ currentLength); // set the length to 1.
            this.multiply(length);
        }
    }

    public double getAngle() {
        return Math.toDegrees(Math.atan2(this.y, this.x));
    }

    public void setAngle(double degrees) {
        this.y = getLength() * Math.sin(Math.toRadians(degrees));
        this.x = getLength() * Math.cos(Math.toRadians(degrees));
    }





}
