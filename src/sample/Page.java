package sample;

import java.util.ArrayList;

public class Page {

    static final String TYPE = "Byte";
    static final int PAGE_SIZE = 4096;
    static final int PAGES = 128;
    private boolean present; //to see if the page is in the memory
    private boolean dirtyBit; // to see if the page from the disk was modified
    private int timesUsed; // for LFU and MFU
    private int lru; // for lru algorithm
    private int frameNumber;
    private int diskNumber; //if the page is not present in the main memory


    public Page(boolean isPresent){
        this.present = isPresent;
        if(isPresent){
            this.diskNumber = -1;
        }else{
            this.frameNumber = -1;
        }
    }
    public Page(boolean isPresent,int timesUsed, int number,int min, int max){
        this.present = isPresent;
        this.timesUsed = timesUsed;
        this.dirtyBit = false;
        if(isPresent){
            this.frameNumber = number;
            this.diskNumber = -1;
            this.lru = max;

        }else{
            this.frameNumber = -1;
            this.diskNumber = number;
            this.lru = 0;
        }
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public int getTimesUsed() {
        return timesUsed;
    }

    public void setTimesUsed(int timesUsed) {
        this.timesUsed = timesUsed;
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(int frameNumber) {
        this.frameNumber = frameNumber;
    }

    public int getDiskNumber() {
        return diskNumber;
    }

    public void setDiskNumber(int diskNumber) {
        this.diskNumber = diskNumber;
    }

    public boolean isDirtyBit() {
        return dirtyBit;
    }

    public void setDirtyBit(boolean dirtyBit) {
        this.dirtyBit = dirtyBit;
    }

    public int getLru() {
        return lru;
    }

    public void setLru(int lru) {
        this.lru = lru;
    }

}
