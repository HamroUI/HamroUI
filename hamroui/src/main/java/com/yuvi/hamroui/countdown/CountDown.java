package com.yuvi.hamroui.countdown;

public class CountDown {
    String caption;
    long endsIn;
    String endsAt;

    private CountDown(){

    }

    private CountDown(String caption, long endsIn){
        this.endsIn = endsIn;
        this.caption = caption;
    }

    private CountDown(String caption, String endsAt){
        this.caption = caption;
        this.endsAt = endsAt;
    }

    public static CountDown instance(){
        return new CountDown();
    }


    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public long getEndsIn() {
        return endsIn;
    }

    public void setEndsIn(long endsIn) {
        this.endsIn = endsIn;
    }

    public String getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(String endsAt) {
        this.endsAt = endsAt;
    }
}
