package com.example.naverpractice;

public class GPSdistance {

    public GPSdistance(){ }

    public double ConvertDecimalDegreesToRadiands(double deg) {
        return (deg * Math.PI / 180);
    }

    public double ConvertRadiansToDecimalDegrees(double rad) {
        return (rad * 180 / Math.PI);
    }

    public double GetDistanceBetweenPoints(double lat1, double lon1, double lat2, double lon2) {
        double theta, dist;

        if((lat1 == lat2) && (lon1 == lon2)){
            return 0;
        }
        else{
            theta = lon1 - lon2;
            dist = Math.sin(ConvertDecimalDegreesToRadiands(lat1)) * Math.sin(ConvertDecimalDegreesToRadiands(lat2))+
                    Math.cos(ConvertDecimalDegreesToRadiands(lat1))* Math.cos(ConvertDecimalDegreesToRadiands(lat2))*
                            Math.cos(ConvertDecimalDegreesToRadiands(theta));
            dist = Math.acos(dist);
            dist = ConvertRadiansToDecimalDegrees(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1.609344 * 1000;
            return dist;
        }
    }
}

