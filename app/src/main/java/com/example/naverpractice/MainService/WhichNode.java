package com.example.naverpractice.MainService;

public class WhichNode {

    public int setNode(int transformX, int transformY){
        int node = -1;

        if(0<= transformX && transformX <= 63 && 130 <= transformY && transformY <= 315)           node = 0;
        else if(63 <transformX && transformX <= 155 && 130 <= transformY && transformY <= 450)     node = 1;
        else if(155 < transformX && transformX <= 224 && 130 <= transformY && transformY <= 450)   node = 2;
        else if(224 < transformX && transformX <= 339 && 130 <= transformY && transformY <= 450)   node = 3;
        else if(339 < transformX && transformX <= 431 && 130 <= transformY && transformY <= 315)   node = 4;
        else if(431 < transformX && transformX <= 873 && 130 <= transformY && transformY <= 450)   node = 5;
        else if(873 < transformX && transformX <= 1057 && 130 <= transformY && transformY <= 315)  node = 6;
        else if(0<= transformX && transformX <= 63 && 315 < transformY && transformY <= 585)     node = 7;
        else if(339 < transformX && transformX <= 431 && 315 < transformY && transformY <= 585)  node = 8;
        else if((873 < transformX && transformX <= 988 && 315 < transformY && transformY <= 495)
                || (873 < transformX && transformX <= 942 && 495 < transformY && transformY <= 585))  node = 9;
        else if(0<= transformX && transformX <= 63 && 585 < transformY && transformY <= 750) node = 10;
        else if(63< transformX && transformX <= 155 && 450 < transformY && transformY <= 800) node = 11;
        else if(155 < transformX && transformX <= 224 && 450 < transformY && transformY <= 660) node = 12;
        else if(224 < transformX && transformX <= 339 && 450 < transformY && transformY <= 800) node = 13;
        else if(339 < transformX && transformX <= 431 && 585 < transformY && transformY <= 660) node = 14;
        else if(431 < transformX && transformX <= 873 && 450 < transformY && transformY <= 800) node = 15;
        else if(873 < transformX && transformX <= 942 && 585 < transformY && transformY <= 660) node = 16;
        else if(942 < transformX && transformX <= 1057 && 565 < transformY && transformY <= 680) node = 17;
        else if(155 < transformX && transformX <= 224 && 660 < transformY && transformY <= 930) node = 18;
        else if(339 < transformX && transformX <= 431 && 660 < transformY && transformY <= 930) node = 19;
        else if(873 < transformX && transformX <= 942 && 660 < transformY && transformY <= 930) node = 20;
        else if(155 < transformX && transformX <= 224 && 930 < transformY && transformY <= 1010) node = 21;
        else if(224 < transformX && transformX <= 339 && 800 < transformY && transformY <= 1010) node = 22;
        else if(339 < transformX && transformX <= 431 && 930 < transformY && transformY <= 1010) node = 23;
        else if(431 < transformX && transformX <= 873 && 800 < transformY && transformY <= 1010) node = 24;
        else if(873 < transformX && transformX <= 942 && 930 < transformY && transformY <= 1010) node = 25;
        return node;
    }
}
