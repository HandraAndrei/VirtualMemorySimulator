package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private Button start;
    @FXML
    private Label enterRAMLabel;
    @FXML
    private Label enterVRAMLabel;
    @FXML
    private Label enterPercentage;
    @FXML
    private Button proceed;
    @FXML
    private ComboBox<Integer> percentageCombo;
    @FXML
    private ComboBox<Integer> vramCombo;
    @FXML
    private ComboBox<Float> ramCombo;
    @FXML
    private Button clear;
    @FXML
    private Label error;

    Memory memory;
    Storage storage;
    PageTable pageTable;
    TLB tlb;

    public void onButtonClicked(ActionEvent e){
        if(e.getSource().equals(start)){
            start.setDisable(true);
            enterRAMLabel.setVisible(true);
            enterVRAMLabel.setVisible(true);
            ramCombo.setVisible(true);
            vramCombo.setVisible(true);
            enterPercentage.setVisible(true);
            percentageCombo.setVisible(true);
            proceed.setVisible(true);
            proceed.disableProperty().bind(ramCombo.valueProperty().isNull() .or(vramCombo.valueProperty().isNull()) .or(percentageCombo.valueProperty().isNull()));
        }else if(e.getSource().equals(proceed)){
            float ram = ramCombo.getValue();
            int v  = vramCombo.getValue();
            int p = percentageCombo.getValue();
            if(ram >= v){
                error.setText("Please enter a virtual memory size that is greater than the actual RAM");
                error.setVisible(true);
                clear.setDisable(false);
                clear.setVisible(true);
            }else {
                try {
                    memory = new Memory(ram);
                    memory.setPercentageOccupied(p);
                    memory.addForOccupied();
                    pageTable = new PageTable(v);
                    tlb = new TLB();
                    pageTable.addInitialPagesFromMemory(memory);
                    storage = new Storage(v);
                    int pageOnDisk = pageTable.getCapacity() - pageTable.getPageEntry().size();
                    storage.setBeginPages(pageOnDisk);
                    storage.addRemainingPages();
                    tlb.addInitialPagesFromPageTable(pageTable);
                    pageTable.addPagesFromDisk(storage);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("simulation.fxml"));
                    Parent root = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root, 1000, 800));
                    stage.setTitle("Simulator");
                    stage.show();
                    SimulationController simulationController = fxmlLoader.getController();
                    simulationController.setMemory(memory);
                    simulationController.setStorage(storage);
                    simulationController.setPageTable(pageTable);
                    simulationController.setTLB(tlb);
                    simulationController.setLruCounter(memory.getFrames().size());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }else if(e.getSource().equals(clear)){
            error.setText("");
            error.setVisible(false);
            start.setDisable(false);
            enterRAMLabel.setVisible(false);
            enterVRAMLabel.setVisible(false);
            ramCombo.setVisible(false);
            vramCombo.setVisible(false);
            enterPercentage.setVisible(false);
            percentageCombo.setVisible(false);
            proceed.setVisible(false);
            percentageCombo.getSelectionModel().clearSelection();
            ramCombo.getSelectionModel().clearSelection();
            vramCombo.getSelectionModel().clearSelection();
        }
    }
}
