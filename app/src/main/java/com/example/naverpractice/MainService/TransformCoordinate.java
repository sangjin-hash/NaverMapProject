package com.example.naverpractice.MainService;

public class TransformCoordinate {
    private int transformX, transformY;

    public int getTransformX() {
        return transformX;
    }

    public void setTransformX(int transformX) {
        this.transformX = transformX;
    }

    public int getTransformY() {
        return transformY;
    }

    public void setTransformY(int transformY) {
        this.transformY = transformY;
    }

    public void transForm(int x, int y, int node){
        if(node == 0 || node == 4 || node == 6){
            if(y < 275) y = 275;
            setTransformX(x);
            setTransformY(y);
        }
        else if((1<= node && node <= 3) || node == 5){
            y = 275;
            setTransformX(x);
            setTransformY(y);
        }
        else if( 7 <= node && node <= 9){
            setTransformY(y);
            switch(node){
                case 7 :
                    x = 40;
                    setTransformX(x);
                    break;
                case 8:
                    x = 385;
                    setTransformX(x);
                    break;
                case 9:
                    x = 908;
                    setTransformX(x);
                    break;
            }
        }
        else if(node == 10){
            if(623< y) y = 623;
            setTransformX(x);
            setTransformY(y);
        }
        else if(node == 12){
            if(y < 623) y = 623;
            setTransformX(x);
            setTransformY(y);
        }
        else if(node == 14 || node == 16){
            setTransformX(x);
            setTransformY(y);
        }
        else if(node == 11 || node == 13 || node == 15 || node == 17){
            y = 623;
            setTransformX(x);
            setTransformY(y);
        }
        else if(18 <= node && node <= 20){
            switch(node){
                case 18 :
                    x = 190;
                    setTransformX(x);
                    setTransformY(y);
                    break;
                case 19 :
                    x = 385;
                    setTransformX(x);
                    setTransformY(y);
                    break;
                case 20 :
                    x = 908;
                    setTransformX(x);
                    setTransformY(y);
                    break;
            }
        }
        else{
            if(node == 22 || node == 24) y = 970;
            else{
                if(y > 970) y = 970;
            }
            setTransformX(x);
            setTransformY(y);
        }
    }
}