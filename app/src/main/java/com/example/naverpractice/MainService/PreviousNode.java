package com.example.naverpractice.MainService;

public class PreviousNode {
    int x, y , node;

    public PreviousNode(int x, int y, int node) {
        this.x = x;
        this.y = y;
        this.node = node;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getNode() {
        return node;
    }

    public void setNode(int node) {
        this.node = node;
    }
}