package sample;

import javafx.beans.binding.Bindings;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Storage {

    final int MAX_MEMORY;
    private int beginPages;
    private int capacity;
    private ArrayList<Frame> frames;

    public Storage(int size){
        MAX_MEMORY = size;
        if(size == 1){
            capacity = 131;
            frames = new ArrayList<>();
        }else if(size == 2){
            frames = new ArrayList<>();
            capacity = 262;
        }else if(size == 4){
            frames = new ArrayList<>();
            capacity = 524;
        }else{
            frames = null;
            capacity = 0;
        }
    }

    public int getBeginPages() {

        return beginPages;
    }

    public void setBeginPages(int beginPages) {
        this.beginPages = beginPages;
    }

    public ArrayList<Frame> getFrames() {
        return frames;
    }

    public void setFrames(ArrayList<Frame> frames) {
        this.frames = frames;
    }

    public void addRemainingPages(){
        for (int i = 0; i < beginPages; i++){
            Frame frame = new Frame();
            for (int j = 0; j< Frame.FRAMES; j++){
                Random random = new Random();
                int number = random.nextInt(10000 - 1) + 1;
                frame.getData().add(number);
            }
            frames.add(frame);
        }
    }

    public TableView<Frame> importFrames(TableView<Frame> table, boolean initializeTable, Storage storage){
        TableView<Frame> newTable;
        if(initializeTable){
            table = setTableHeaders(table);
        }
        for(Frame frames: storage.getFrames()){
            table.getItems().add(frames);
        }
        table.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("data"));
        newTable = table;
        return newTable;

    }
    public TableView<Frame> setTableHeaders(TableView<Frame> table){
        TableView<Frame> newTable;
        TableColumn<Frame, Void> indexCol = new TableColumn<>("Index");
        indexCol.setCellFactory(col -> {
            TableCell<Frame, Void> cell = new TableCell<>();
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
        List<String> headers = List.of("Frames");
        for(String string:headers){
            TableColumn<Frame,String> tableColumn = new TableColumn<>(string);
            table.getColumns().add(tableColumn); }
        newTable = table;
        return newTable;
    }
}
