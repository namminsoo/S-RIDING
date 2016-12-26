package com.example.namsoo.s_riding_ui.nayoung;

/**
 * Created by 김나영 on 2015-11-12.
 */
public class ServerTest {

    public int id; //index
    public String email; //fb email
    public String ridingStartTime; //출발시간
    public String ridingStopTime; //종료시간
    public String ridingTime; //라이딩 시간
    public String ridingBreakTime; //라이딩휴식시간
    public String entireRidingTime; //전체라이딩시간
    public String entireRidingDistance; //라이딩주행거리
    public String startingLocationName; //출발지명
    public String startingLocationLatitude; //출발지 경도
    public String startingLocationLongitude; //출발지 위도
    public String destinationName; //도착지명
    public String destinationLatitude; //도착지 경도
    public String destinationLongitude; //도착지 위도
    public String consumedCal; //칼로리
    public String averageSpeed; //평균속도
    public String maxSpeed; //평균속도

    public String instantSpeed; // 1초마다 스피드
    public String instantHeight; //1초마다 고도


    public void setTest(int _id, String _email, String _ridingStartTime, String _ridingStopTime, String _ridingTime, String _ridingBreakTime, String _entireRidingTime, String _entireRidindDistance, String _startingLocationName, String _startingLocationLatitude, String _startLocationLongitude, String _destinationName, String _destinationLatitude, String _destinationLongitude, String _consumedCal, String _averageSpeed, String _maxSpeed, String _instantSpeed, String _instantHeight) {

        id = _id;
        email = _email;
        ridingStartTime = _ridingStartTime;
        ridingStopTime = _ridingStopTime;
        ridingTime = _ridingTime;
        ridingBreakTime= _ridingBreakTime;
        entireRidingTime = _entireRidingTime;
        entireRidingDistance = _entireRidindDistance;
        startingLocationName = _startingLocationName;
        startingLocationLatitude = _startingLocationLatitude;
        startingLocationLongitude = _startLocationLongitude;
        destinationName = _destinationName;
        destinationLatitude = _destinationLatitude;
        destinationLongitude = _destinationLongitude;
        consumedCal = _consumedCal;
        averageSpeed = _averageSpeed;
        maxSpeed = _maxSpeed;

        instantSpeed = _instantSpeed;
        instantHeight = _instantHeight;
    }


    public String insertDB(){

        String insertQuery = "insert into S_RidingTest values(null,"+getEmail()+","+getRidingStartTime()+" , "+getRidingStopTime()+","+getRidingTime()+","+getRidingBreakTime()+","+getRidingBreakTime()+","+getStartingLocationName()+","+getStartingLocationLatitude()+","+getStartingLocationLongitude()+","+getDestinationName()+","+getDestinationLatitude()+","+getDestinationLongitude()+","+getConsumedCal()+","+getAverageSpeed()+","+getMaxSpeed()+","+getInstantSpeed()+","+getInstantHeight()+");";



        return insertQuery;

    }
//
//
//    ServerTest(int _id, String _email, String _ridingStartTime, String _ridingStopTime, String _entireRidingTime, String _entireRidindDistance, String _startingLocationName, String _startingLocationLatitude, String _startLocationLongitude, String _destinationName, String _destinationLatitude, String _destinationLongitude, String _consumedCal, String _averageSpeed, String _maxSpeed, String _instantSpeed, String _instantHeight){
//
//        id = _id;
//        email = _email;
//        ridingStartTime = _ridingStartTime;
//        ridingStopTime = _ridingStopTime;
//        entireRidingTime = _entireRidingTime;
//        entireRidingDistance = _entireRidindDistance;
//        startingLocationName = _startingLocationName;
//        startingLocationLatitude = _startingLocationLatitude;
//        startingLocationLongitude = _startLocationLongitude;
//        destinationName = _destinationName;
//        destinationLatitude = _destinationLatitude;
//        destinationLongitude = _destinationLongitude;
//        consumedCal = _consumedCal;
//        averageSpeed = _averageSpeed;
//        maxSpeed = _maxSpeed;
//
//        instantSpeed = _instantSpeed;
//        instantHeight = _instantHeight;
//
//    }

    public void set_id(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRidingStartTime(String ridingStartTime) {
        this.ridingStartTime = ridingStartTime;
    }

    public void setRidingStopTime(String ridingStopTime) {
        this.ridingStopTime = ridingStopTime;
    }

    public void setEntireRidingTime(String entireRidingTime) {
        this.entireRidingTime = entireRidingTime;
    }

    public void setEntireRidingDistance(String entireRidingDistance) {
        this.entireRidingDistance = entireRidingDistance;
    }

    public void setStartingLocationName(String startingLocationName) {
        this.startingLocationName = startingLocationName;
    }

    public void setStartingLocationLatitude(String startingLocationLatitude) {
        this.startingLocationLatitude = startingLocationLatitude;
    }

    public void setStartingLocationLongitude(String startingLocationLongitude) {
        this.startingLocationLongitude = startingLocationLongitude;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public void setDestinationLatitude(String destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public void setDestinationLongitude(String destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public void setConsumedCal(String consumedCal) {
        this.consumedCal = consumedCal;
    }

    public void setAverageSpeed(String averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public void setMaxSpeed(String maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setInstantSpeed(String instantSpeed) {
        this.instantSpeed = instantSpeed;
    }

    public void setInstantHeight(String instantHeight) {
        this.instantHeight = instantHeight;
    }


    public void setRidingTime(String ridingTime) {
        this.ridingTime = ridingTime;
    }

    public void setRidingBreakTime(String ridingBreakTime) {
        this.ridingBreakTime = ridingBreakTime;
    }

    public String getRidingBreakTime() {
        return ridingBreakTime;
    }

    public String getRidingTime() {
        return ridingTime;
    }

    public int getid() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRidingStartTime() {
        return ridingStartTime;
    }

    public String getRidingStopTime() {
        return ridingStopTime;
    }

    public String getEntireRidingTime() {
        return entireRidingTime;
    }

    public String getEntireRidingDistance() {
        return entireRidingDistance;
    }

    public String getStartingLocationName() {
        return startingLocationName;
    }

    public String getStartingLocationLatitude() {
        return startingLocationLatitude;
    }

    public String getStartingLocationLongitude() {
        return startingLocationLongitude;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public String getDestinationLatitude() {
        return destinationLatitude;
    }

    public String getDestinationLongitude() {
        return destinationLongitude;
    }

    public String getConsumedCal() {
        return consumedCal;
    }

    public String getAverageSpeed() {
        return averageSpeed;
    }

    public String getMaxSpeed() {
        return maxSpeed;
    }

    public String getInstantSpeed() {
        return instantSpeed;
    }

    public String getInstantHeight() {
        return instantHeight;
    }
}
