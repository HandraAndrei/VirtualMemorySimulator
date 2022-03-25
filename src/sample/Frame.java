package sample;

import java.util.ArrayList;

public class Frame {

    static final String TYPE = "Byte";
    static final int FRAME_SIZE = 4096;
    static final int FRAMES = 128;
    private ArrayList<Integer> data;

    public Frame(){
        data = new ArrayList<>(FRAMES);
    }

    public ArrayList<Integer> getData() {
        return data;
    }

    public void setData(ArrayList<Integer> data) {
        this.data = data;
    }

}
