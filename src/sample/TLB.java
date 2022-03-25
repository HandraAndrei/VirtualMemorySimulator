package sample;

import javafx.beans.binding.Bindings;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TLB {

    final int CAPACITY = 10;
    private ArrayList<Page> recentPages;
    private ArrayList<Integer> pageNumbers;

    public TLB(){
        recentPages = new ArrayList<>(CAPACITY);
        pageNumbers = new ArrayList<>(CAPACITY);
    }

    public ArrayList<Page> getRecentPages() {
        return recentPages;
    }

    public void setRecentPages(ArrayList<Page> recentPages) {
        this.recentPages = recentPages;
    }

    public ArrayList<Integer> getPageNumbers() {
        return pageNumbers;
    }

    public void setPageNumbers(ArrayList<Integer> pageNumbers) {
        this.pageNumbers = pageNumbers;
    }

    public void addInitialPagesFromPageTable(PageTable pageTable){
        for(int i = pageTable.getPageEntry().size() - CAPACITY;i < pageTable.getPageEntry().size();i++){
            Page page = new Page(true);
            page = pageTable.getPageEntry().get(i);
            recentPages.add(page);
            pageNumbers.add(i);
        }
    }

    public TableView<Page> importFrames(TableView<Page> table, boolean initializeTable, TLB tlb){
        TableView<Page> newTable;
        if(initializeTable){
            table = setTableHeaders(table);
        }
        for(Page pages:tlb.getRecentPages()){
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
