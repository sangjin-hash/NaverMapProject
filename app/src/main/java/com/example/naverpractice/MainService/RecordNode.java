package com.example.naverpractice.MainService;

/*
        - : Blocked
        Node : The section of movable

        0    1   2   3   4   5   6   -

        7    -   -   -   -   8   -   9

        10  11  12  13  14  15  16  17

         -   -  18   -  19   -  20   -

         -   -  21  22  23  24  25   -
 */


public class RecordNode {
    private int record_node = -1;

    public boolean record(int node){
        boolean result = false;

        if(record_node != -1){
            switch(node){
                case 0 :
                    if(record_node == 0 || record_node == 1 || record_node == 7){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 1 :
                    if(record_node == 0 || record_node == 1 || record_node == 2 || record_node == 7){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 2 :
                    if(record_node == 1 || record_node == 2 || record_node == 3){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 3 :
                    if(record_node == 2 || record_node == 3 || record_node == 4 || record_node == 8){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 4 :
                    if(record_node == 3 || record_node == 4 || record_node == 5 || record_node == 8){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 5 :
                    if(record_node == 4 || record_node == 5 || record_node == 6 || record_node == 8 || record_node == 9){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 6 :
                    if(record_node == 5 || record_node == 6 || record_node == 9){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 7 :
                    if(record_node == 0 || record_node == 1 || record_node == 7 || record_node == 10 || record_node == 11){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 8 :
                    if(record_node == 3 || record_node == 4 || record_node == 5 || record_node == 8 || record_node ==13 || record_node == 14 || record_node == 15){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 9 :
                    if(record_node == 6 || record_node == 9 || record_node == 15 ||record_node == 16 || record_node == 17){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 10 :
                    if(record_node == 7 || record_node == 10 || record_node == 11){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 11 :
                    if(record_node == 7 || record_node == 10 || record_node == 11 || record_node == 12){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 12 :
                    if(record_node == 11 || record_node == 12 || record_node == 13 || record_node == 18){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 13 :
                    if(record_node == 12 || record_node == 13 || record_node == 14){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 14 :
                    if(record_node == 8 || record_node == 13 || record_node == 14 || record_node == 15 || record_node == 19){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 15 :
                    if(record_node == 8 || record_node == 9 || record_node == 14 || record_node == 15 || record_node == 16 || record_node == 19 || record_node == 20){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 16 :
                    if(record_node == 9 || record_node == 15 || record_node == 16 || record_node == 17 || record_node == 20){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 17 :
                    if( record_node == 9 || record_node == 16 || record_node == 17 || record_node == 20){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 18 :
                    if(record_node == 11 || record_node == 12 || record_node == 13 || record_node == 18 || record_node == 21 || record_node == 22){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 19 :
                    if(record_node == 13 || record_node == 14 || record_node == 15 || record_node == 19 || record_node == 22 || record_node == 23 || record_node == 24){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 20 :
                    if(record_node == 15|| record_node == 16 || record_node == 17 || record_node == 20 || record_node == 24 || record_node == 25){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 21 :
                    if(record_node == 18 || record_node == 21 || record_node == 22){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 22 :
                    if(record_node == 18 || record_node == 21 || record_node == 22 || record_node == 23 || record_node == 19){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 23 :
                    if(record_node == 19 || record_node == 22 || record_node == 23 || record_node == 24){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 24 :
                    if(record_node == 19 || record_node == 23 || record_node == 20 || record_node == 24 || record_node == 25){
                        record_node = node;
                        result = true;
                    }
                    break;
                case 25 :
                    if(record_node == 20 || record_node == 24 || record_node == 25){
                        record_node = node;
                        result = true;
                    }
                    break;
            }
        }else{
            record_node = node;
            result = true;
        }
        return result;
    }
}