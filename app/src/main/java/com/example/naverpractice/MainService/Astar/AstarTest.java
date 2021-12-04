package com.example.naverpractice.MainService.Astar;

//Search Area
//      0   1   2   3   4   5   6   7
// 0    -   -   -   -   -   -   -   B
// 1    -   B   B   B   -   B   -   B
// 2    -   -   -   -   -   -   -   -
// 3    B   B   -   B   -   B   -   B
// 4    B   B   -   -   -   -   -   B

import android.os.Message;
import android.util.Log;

import com.example.naverpractice.MainService.LocationService;
import com.example.naverpractice.network.ApiClient;
import com.example.naverpractice.network.ApiInterface;
import com.example.naverpractice.network.RecommendSeat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AstarTest extends Thread {

    private static final String TAG = "[AstarTest]";
    private int recommend_node;
    private int node;
    private int x, y;

    public static ArrayList<Integer[]> path = new ArrayList<>();

    public AstarTest(int x, int y, int node) {
        this.x = x;
        this.y = y;
        this.node = node;
    }

    @Override
    public void run() {
        if(AstarNetwork.recommend_seat_num != -1){
            Node initialNode = coordinateToNode(node);
            Node finalNode = recommendToNode(AstarNetwork.recommend_seat_num);

            int rows = 5;
            int cols = 8;
            Astar aStar = new Astar(rows, cols, initialNode, finalNode);

            //차량의 방향벡터에 따라서 차량 진행 방향의 반대 방향을 동적으로 block하면 된다.
            ArrayList<Integer[]> blocksArray = new ArrayList<>();
            blocksArray.add(new Integer[]{0, 7});
            blocksArray.add(new Integer[]{1, 1});
            blocksArray.add(new Integer[]{1, 2});
            blocksArray.add(new Integer[]{1, 3});
            blocksArray.add(new Integer[]{1, 5});
            blocksArray.add(new Integer[]{1, 7});
            blocksArray.add(new Integer[]{3, 0});
            blocksArray.add(new Integer[]{3, 1});
            blocksArray.add(new Integer[]{3, 3});
            blocksArray.add(new Integer[]{3, 5});
            blocksArray.add(new Integer[]{3, 7});
            blocksArray.add(new Integer[]{4, 0});
            blocksArray.add(new Integer[]{4, 1});
            blocksArray.add(new Integer[]{4, 7});

            aStar.setBlocks(blocksArray);
            List<Node> node_list = aStar.findPath();

            if (path.size() == 0) {
                path.add(new Integer[]{x, y});

                if (node_list.size() > 1) {
                    for (int i = 0; i < node_list.size(); i++) {
                        if (i == node_list.size() - 1) break;

                        int row = node_list.get(i + 1).getRow() - node_list.get(i).getRow();
                        int col = node_list.get(i + 1).getCol() - node_list.get(i).getCol();

                        if (row == 0 && col > 0) {
                            // Motion Vector : ->
                            int next_node_Row = node_list.get(i + 1).getRow();
                            int next_node_Col = node_list.get(i + 1).getCol();

                            int[][][] coordinate = node_list.get(i + 1).getCoordinate();
                            int x = coordinate[next_node_Row][next_node_Col][0];
                            int y = coordinate[next_node_Row][next_node_Col][1];
                            path.add(new Integer[]{x, y});
                        } else if (row == 0 && col < 0) {
                            // Motion Vector : <-
                            int next_node_Row = node_list.get(i + 1).getRow();
                            int next_node_Col = node_list.get(i + 1).getCol();

                            int[][][] coordinate = node_list.get(i + 1).getCoordinate();
                            int x = coordinate[next_node_Row][next_node_Col][0];
                            int y = coordinate[next_node_Row][next_node_Col][1];
                            path.add(new Integer[]{x, y});
                        } else if (row < 0 && col == 0) {
                            // Motion Vector : up
                            int next_node_Row = node_list.get(i + 1).getRow();
                            int next_node_Col = node_list.get(i + 1).getCol();

                            int[][][] coordinate = node_list.get(i + 1).getCoordinate();
                            int x = coordinate[next_node_Row][next_node_Col][0];
                            int y = coordinate[next_node_Row][next_node_Col][1];
                            path.add(new Integer[]{x, y});
                        } else if (row > 0 && col == 0) {
                            // Motion Vector : down
                            int next_node_Row = node_list.get(i + 1).getRow();
                            int next_node_Col = node_list.get(i + 1).getCol();

                            int[][][] coordinate = node_list.get(i + 1).getCoordinate();
                            int x = coordinate[next_node_Row][next_node_Col][0];
                            int y = coordinate[next_node_Row][next_node_Col][1];
                            path.add(new Integer[]{x, y});
                        } else break;
                    }
                }
                else{
                    int row = node_list.get(0).getRow();
                    int col = node_list.get(0).getCol();

                    int [][][] coordinate = node_list.get(0).getCoordinate();
                    int x = coordinate[row][col][0];
                    int y = coordinate[row][col][1];
                    path.add(new Integer[]{x,y});
                }
                if(AstarNetwork.recommend_seat_num != -1){
                    Integer[] final_coordinate =  DestCoordindate(AstarNetwork.recommend_seat_num);
                    Integer[] temp_final = path.get(path.size()-1);
                    int temp_final_Y = temp_final[1];
                    Integer[] coordinate = {final_coordinate[0], temp_final_Y};
                    path.set(path.size()-1, coordinate);
                    path.add(final_coordinate);
                }
            }
        }
    }

    public Integer[] DestCoordindate(int num){
        Integer[] array = null;
        int x = 0;
        int y = 0;
        if (0 <= num && num <= 14) {
            x = 29 + (num * 23);
            y = 240;
        } else if (15 <= num && num <= 38) {
            x = 420 + (num - 15) * 23;
            y = 240;
        } else if (39 <= num && num <= 50) {
            x = 75 + (num - 39) * 23;
            y = 315;
        } else if (51 <= num && num <= 69) {
            x = 444 + (num - 51) * 23;
            y = 315;
        } else if (70 <= num && num <= 81) {
            x = 75 + (num - 70) * 23;
            y = 585;
        } else if (82 <= num && num <= 100) {
            x = 444 + (num - 82) * 23;
            y = 585;
        } else if (101 <= num && num <= 105) {
            x = 954 + (num - 101) * 23;
            y = 585;
        } else if (106 <= num && num <= 110) {
            x = 52 + (num - 106) * 23;
            y = 660;
        } else if (111 <= num && num <= 115) {
            x = 236 + (num - 111) * 23;
            y = 660;
        } else if (116 <= num && num <= 134) {
            x = 444 + (num - 116) * 23;
            y = 660;
        } else if (135 <= num && num <= 137) {
            x = 954 + (num - 135) * 23;
            y = 660;
        } else if (138 <= num && num <= 142) {
            x = 236 + (num - 138) * 23;
            y = 930;
        } else if (143 <= num && num <= 161) {
            x = 444 + (num - 143) * 23;
            y = 930;
        } else if (162 <= num && num <= 179) {
            x = 444 + (num - 162) * 23;
            y = 1010;
        }
        array = new Integer[]{x,y};
        return array;
    }


    public Node coordinateToNode(int node) {
        int coordinate_row = -1;
        int coordinate_col = -1;

        if (0 <= node && node <= 6) {
            coordinate_row = 0;
            coordinate_col = node;
        } else if (7 <= node && node <= 9) {
            coordinate_row = 1;
            if (node == 7) coordinate_col = 0;
            else if (node == 8) coordinate_col = 4;
            else if (node == 9) coordinate_col = 6;
        } else if (10 <= node && node <= 17) {
            coordinate_row = 2;
            coordinate_col = node % 10;
        } else if (18 <= node && node <= 20) {
            coordinate_row = 3;
            if (node == 18) coordinate_col = 2;
            else if (node == 19) coordinate_col = 4;
            else if (node == 20) coordinate_col = 6;
        } else if (21 <= node && node <= 25) {
            coordinate_row = 4;
            coordinate_col = node % 19;
        }
        Node startNode = new Node(coordinate_row, coordinate_col);
        return startNode;
    }

    public Node recommendToNode(int seat_num) {
        int recommend_row = -1;
        int recommend_col = -1;

        if (0 == seat_num || seat_num == 1) {
            recommend_node = 0;
            recommend_row = 0;
            recommend_col = 0;
        } else if ((2 <= seat_num && seat_num <= 5) || (39 <= seat_num && seat_num <= 42)) {
            recommend_node = 1;
            recommend_row = 0;
            recommend_col = 1;
        } else if ((6 <= seat_num && seat_num <= 8) || (43 <= seat_num && seat_num <= 45)) {
            recommend_node = 2;
            recommend_row = 0;
            recommend_col = 2;
        } else if ((9 <= seat_num && seat_num <= 13) || (46 <= seat_num && seat_num <= 50)) {
            recommend_node = 3;
            recommend_row = 0;
            recommend_col = 3;
        } else if (seat_num == 14 || seat_num == 15) {
            recommend_node = 4;
            recommend_row = 0;
            recommend_col = 4;
        } else if ((16 <= seat_num && seat_num <= 34) || (51 <= seat_num && seat_num <= 69)) {
            recommend_node = 5;
            recommend_row = 0;
            recommend_col = 5;
        } else if ((35 <= seat_num && seat_num <= 38)) {
            recommend_node = 6;
            recommend_row = 0;
            recommend_col = 6;
        } else if ((seat_num == 106)) {
            recommend_node = 10;
            recommend_row = 2;
            recommend_col = 0;
        } else if ((70 <= seat_num && seat_num <= 73) || (107 <= seat_num && seat_num <= 110)) {
            recommend_node = 11;
            recommend_row = 2;
            recommend_col = 1;
        } else if (74 <= seat_num && seat_num <= 76) {
            recommend_node = 12;
            recommend_row = 2;
            recommend_col = 2;
        } else if ((77 <= seat_num && seat_num <= 81) || (111 <= seat_num && seat_num <= 115)) {
            recommend_node = 13;
            recommend_row = 2;
            recommend_col = 3;
        } else if ((82 <= seat_num && seat_num <= 100) || (116 <= seat_num && seat_num <= 134)) {
            recommend_node = 15;
            recommend_row = 2;
            recommend_col = 5;
        } else if ((101 <= seat_num && seat_num <= 105) || (135 <= seat_num && seat_num <= 137)) {
            recommend_node = 17;
            recommend_row = 2;
            recommend_col = 7;
        } else if (138 <= seat_num && seat_num <= 142) {
            recommend_node = 22;
            recommend_row = 4;
            recommend_col = 3;
        } else if ((143 <= seat_num && seat_num <= 161) || (162 <= seat_num && seat_num <= 179)) {
            recommend_node = 24;
            recommend_row = 4;
            recommend_col = 5;
        }
        Node finalNode = new Node(recommend_row, recommend_col);
        return finalNode;
    }

}
