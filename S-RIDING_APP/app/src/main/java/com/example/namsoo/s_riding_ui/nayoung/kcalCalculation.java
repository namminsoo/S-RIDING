package com.example.namsoo.s_riding_ui.nayoung;

/**
 * Created by 김나영 on 2015-10-05.
 */
public class kcalCalculation {


    int weight=0;

    int min=0;



    public double Calculation(int weight, double speed, int second){
        double kcal;
        kcal = (weight * getCoefficient(speed) * 1) / 60;
        return kcal;
    }

    double getCoefficient(double speed){
        double coefficient=0;

        if(speed<13.0){
            speed=13;
        }else if(13.0<speed && speed<16.0 ){
            speed=16;
        }else if(16.0<speed && speed<19.0){
            speed=19;
        }else if(19.0<speed && speed<22.0){
            speed=22;
        }else if(22.0<speed && speed<24.0){
            speed=24;
        }else if(24.0<speed && speed<26.0){
            speed=26;
        }else if(26.0<speed && speed<27.0){
            speed=27;
        }else if(27.0<speed && speed<29.0){
            speed=29;
        }else if(29.0<speed && speed<31.0){
            speed=31;
        }else if(31.0<speed && speed<32.0){
            speed=32;
        }else if(32.0<speed && speed<34.0){
            speed=34;
        }else if(34.0<speed && speed<37.0){
            speed=37;
        }else if(37.0<speed && speed<40.0){
            speed=40;
        }



        switch ((int) speed){

            case 13:
                coefficient=0.0650;
                break;
            case 16:
                coefficient=0.0783;
                break;
            case 19:
                coefficient=0.0939;
                break;
            case 22:
                coefficient=0.113;
                break;
            case 24:
                coefficient=0.124;
                break;
            case 26:
                coefficient=0.136;
                break;
            case 27:
                coefficient=0.149;
                break;
            case 29:
                coefficient=0.163;
                break;
            case 31:
                coefficient=0.179;
                break;
            case 32:
                coefficient=0.196;
                break;
            case 34:
                coefficient=0.215;
                break;
            case 37:
                coefficient=0.259;
                break;
            case 40:
                coefficient=0.311;
                break;
            default:
                break;
        }



        return coefficient;
    }

}
