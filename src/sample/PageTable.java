package sample;

import javafx.beans.binding.Bindings;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.*;

public class PageTable {

    private int size;
    private int capacity;
    private ArrayList<Page> pageEntry;

    public PageTable(int size){
        this.size = size;
        if(size == 1){
            pageEntry = new ArrayList<>(131);
            capacity = 131;
        }else if(size == 2){
            pageEntry = new ArrayList<>(262);
            capacity = 262;
        }else if(size == 4){
            pageEntry = new ArrayList<>(524);
            capacity = 524;
        }else{
            pageEntry = null;
            capacity = 0;
        }
    }

    public void setPageEntry(ArrayList<Page> pageEntry) {
        this.pageEntry = pageEntry;
    }

    public ArrayList<Page> getPageEntry() {
        return pageEntry;
    }

    public int getSize() {
        return size;
    }

    public void addInitialPagesFromMemory(Memory memory){
        int pagesToBeAdded = (int)(( (float)memory.getPercentageOccupied()/100) * memory.getCapacity());
        ArrayList<Integer> usedNumbers = new ArrayList<>();
        for(int i=0;i<pagesToBeAdded;i++){
            usedNumbers.add(i);
        }
        Collections.shuffle(usedNumbers);
        for(int i=0;i<pagesToBeAdded;i++){
            Page page = new Page(true);
            page.setFrameNumber(usedNumbers.get(i));
            page.setDirtyBit(false);
            page.setTimesUsed(1);
            page.setLru(usedNumbers.get(i)); // last time it was used
            pageEntry.add(page);
        }
    }

    public void addPagesFromDisk(Storage storage){
        int size = pageEntry.size();
        ArrayList<Integer> usedNumbers = new ArrayList<>();
        for(int i = size;i < capacity;i++){
            usedNumbers.add(i);
        }
        Collections.shuffle(usedNumbers);
        for(int i=0;i<capacity- size;i++){
            Page page = new Page(false);
            page.setDiskNumber(i);
            page.setDirtyBit(false);
            page.setTimesUsed(0);
            page.setLru(0);
            pageEntry.add(page);
        }
    }
    public void addPage(Page page, CPU cpu){
        this.pageEntry.add(cpu.getPageNumber(),page);
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public TableView<Page> importFrames(TableView<Page> table, boolean initializeTable, PageTable pageTable){
        TableView<Page> newTable;
        if(initializeTable){
            table = setTableHeaders(table);
        }
        for(Page pages:pageTable.getPageEntry()){
            table.getItems().add(pages);
        }
        table.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("present"));
        table.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("dirtyBit"));
        table.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("timesUsed"));
        table.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("lru"));
        table.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("frameNumber"));
        table.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("diskNumber"));
        newTable = table;
        return newTable;
    }
    public TableView<Page> setTableHeaders(TableView<Page> table){
        TableView<Page> newTable;
        TableColumn<Page, Void> indexCol = new TableColumn<>("Index");
        indexCol.setCellFactory(col -> {
            TableCell<Page, Void> cell = new TableCell<>();
            cell.textProperty().bind(Bindings.createStringBinding(() -> {
                if (cell.isEmpty()) {
                    return null ;
                } else {
                    return Integer.toString(cell.getIndex());
                }
            }, cell.emptyProperty(), cell.indexProperty()));
            return cell ;
        });
        table.getColumns().add(indexCol);
        List<String> headers = List.of("Present","Dirty bit","Times Used","LRU","FrameNumber","DiskNumber");
        for(String string:headers){
            TableColumn<Page,String> tableColumn = new TableColumn<>(string);
            table.getColumns().add(tableColumn); }
        newTable = table;
        return newTable;
    }
}
