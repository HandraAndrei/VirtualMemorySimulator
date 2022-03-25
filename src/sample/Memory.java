package sample;

import javafx.beans.binding.Bindings;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Memory {

    final float RAM_SIZE;
    private int percentageOccupied;
    private int capacity;
    private ArrayList<Frame> frames;

    public Memory(float size){
        RAM_SIZE = size;
        if(size == 0.5){
            frames = new ArrayList<>(66);
            capacity = 66;
        }else if(size == 1){
            frames = new ArrayList<>(131);
            capacity = 131;
        }else if(size == 2){
            frames = new ArrayList<>(262);
            capacity = 262;
        }else{
            frames = null;
            capacity = 0;
        }
    }

    public int getCapacity() {
        return capacity;
    }

    public int getPercentageOccupied() {
        return percentageOccupied;
    }

    public void setPercentageOccupied(int percentageOccupied) {
        this.percentageOccupied = percentageOccupied;
    }

    public ArrayList<Frame> getFrames() {
        return frames;
    }

    public void setFrames(ArrayList<Frame> frames) {
        this.frames = frames;
    }

    public void addForOccupied(){
        int framesCapacity = 0;
        if(RAM_SIZE == 0.5){
            framesCapacity = 66;
        }else if(RAM_SIZE == 1){
            framesCapacity = 131;
        }else if(RAM_SIZE == 2){
            framesCapacity = 262;
        }
        int positionsOccupied = (int)(( (float)percentageOccupied/100) * framesCapacity);
        for (int i = 0; i < positionsOccupied; i++){
            Frame frame = new Frame();
            for (int j = 0; j< Frame.FRAMES; j++){
                Random random = new Random();
                int number = random.nextInt(10000 - 1) + 1;
                frame.getData().add(number);
            }
            frames.add(frame);
        }
    }

    public TableView<Frame> importFrames(TableView<Frame> table, boolean initializeTable,Memory memory){
        TableView<Frame> newTable;
        if(initializeTable){
            table = setTableHeaders(table);
        }
        for(Frame frames:memory.getFrames()){
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
