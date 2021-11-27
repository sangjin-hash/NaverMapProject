package com.example.naverpractice.MainService.Astar;

public class Node {

    private int g;
    private int f;
    private int h;
    private int row;
    private int col;
    private boolean isBlock;
    private Node parent;

    private int[][][] coordinate;

    public Node(int row, int col) {
        super();
        this.row = row;
        this.col = col;
        coordinate = new int[][][]{
                {{40, 275}, {109, 275}, {190, 275}, {282,275},  {385, 275}, {652, 275}, {908, 275}, {-1,-1}},
                {{40, 450},  {-1, -1},    {-1,-1},    {-1,-1},  {385, 450}, {-1, -1},   {908, 450}, {-1, -1}},
                {{40,623},  {109, 623}, {190, 623}, {282,623},  {385,623}, {652,623},  {908,623},  {1000,623}},
                {{-1, -1},  {-1, -1},  {190, 795},  {-1, -1},   {385,795}, {-1, -1},   {908, 795},  {-1, -1}},
                {{-1, -1},  {-1, -1},  {190, 970},  {282, 970}, {385,970}, {652, 970},   {908, 970},  {-1, -1}}
        };
    }

    public void calculateHeuristic(Node finalNode) {
        this.h = Math.abs(finalNode.getRow() - getRow()) + Math.abs(finalNode.getCol() - getCol());
    }

    public void setNodeData(Node currentNode, int cost) {
        int gCost = currentNode.getG() + cost;
        setParent(currentNode);
        setG(gCost);
        calculateFinalCost();
    }

    public boolean checkBetterPath(Node currentNode, int cost) {
        int gCost = currentNode.getG() + cost;
        if (gCost < getG()) {
            setNodeData(currentNode, cost);
            return true;
        }
        return false;
    }

    private void calculateFinalCost() {
        int finalCost = getG() + getH();
        setF(finalCost);
    }

    @Override
    public boolean equals(Object arg0) {
        Node other = (Node) arg0;
        return this.getRow() == other.getRow() && this.getCol() == other.getCol();
    }

    @Override
    public String toString() {
        return "Node [row=" + row + ", col=" + col + "]";
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public void setBlock(boolean isBlock) {
        this.isBlock = isBlock;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int[][][] getCoordinate() {
        return coordinate;
    }
}
