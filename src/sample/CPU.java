package sample;

public class CPU {

    private int logicalAddress;
    private int pageNumber;
    private int pageOffset;

    public CPU(int logicalAddress) {
        this.logicalAddress = logicalAddress;
        this.pageNumber = getPageNumber(logicalAddress);
        this.pageOffset = getOffset(logicalAddress);
    }

    static int getOffset(int address){
        return ((1<<12) - 1) & address;
    }

    static int getPageNumber(int address){
        return ((1<<20) - 1) & (address >> 12);
    }

    public int getLogicalAddress() {
        return logicalAddress;
    }

    public void setLogicalAddress(int logicalAddress) {
        this.logicalAddress = logicalAddress;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(int pageOffset) {
        this.pageOffset = pageOffset;
    }
}


