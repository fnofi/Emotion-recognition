package com.example.face_test.ui.notifications;

public class EmotionData {
    String emotion, time;
    public EmotionData(){
    }
    public EmotionData(String emotion, String time){
        this.emotion = emotion;
        this.time = time;
    }
    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
