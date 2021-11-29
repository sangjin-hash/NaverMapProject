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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AstarTest extends Thread{

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
        recommend(node);
    }

    private void recommend(int node) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<RecommendSeat>> call = apiInterface.getNum();
        call.enqueue(new Callback<List<RecommendSeat>>() {
            @Override
            public void onResponse(Call<List<RecommendSeat>> call, Response<List<RecommendSeat>> response) {
                int recommend_num = response.body().get(0).getNum();
                Log.e("[RECOMMEND]", "Server로부터 받아온 추천 좌석 = " + recommend_num);

                Node initialNode = coordinateToNode(node);
                Node finalNode = recommendToNode(recommend_num);

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

                if(path.size() == 0){
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
                                Log.e("NEXT NODE 테스트", "X = " + x + " Y = " + y);

                                path.add(new Integer[]{x, y});
                            } else if (row == 0 && col < 0) {
                                // Motion Vector : <-
                                int next_node_Row = node_list.get(i + 1).getRow();
                                int next_node_Col = node_list.get(i + 1).getCol();

                                int[][][] coordinate = node_list.get(i + 1).getCoordinate();
                                int x = coordinate[next_node_Row][next_node_Col][0];
                                int y = coordinate[next_node_Row][next_node_Col][1];
                                Log.e("NEXT NODE 테스트", "X = " + x + " Y = " + y);

                                path.add(new Integer[]{x, y});
                            } else if (row < 0 && col == 0) {
                                // Motion Vector : up
                                int next_node_Row = node_list.get(i + 1).getRow();
                                int next_node_Col = node_list.get(i + 1).getCol();

                                int[][][] coordinate = node_list.get(i + 1).getCoordinate();
                                int x = coordinate[next_node_Row][next_node_Col][0];
                                int y = coordinate[next_node_Row][next_node_Col][1];
                                Log.e("NEXT NODE 테스트", "X = " + x + " Y = " + y);

                                path.add(new Integer[]{x, y});
                            } else if (row > 0 && col == 0) {
                                // Motion Vector : down
                                int next_node_Row = node_list.get(i + 1).getRow();
                                int next_node_Col = node_list.get(i + 1).getCol();

                                int[][][] coordinate = node_list.get(i + 1).getCoordinate();
                                int x = coordinate[next_node_Row][next_node_Col][0];
                                int y = coordinate[next_node_Row][next_node_Col][1];
                                Log.e("NEXT NODE 테스트", "X = " + x + " Y = " + y);

                                path.add(new Integer[]{x, y});
                            } else break;
                        }
                    }
                }
                /*Log
                for (Node node : node_list) {
                    Log.d("TEST", "" + node.toString());
                }*/
            }

            @Override
            public void onFailure(Call<List<RecommendSeat>> call, Throwable t) {
                Log.e("[RECOMMEND]", t.getMessage());
            }
        });
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
