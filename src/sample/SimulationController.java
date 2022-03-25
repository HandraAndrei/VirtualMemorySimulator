package sample;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationController {

    Memory memory;
    Storage storage;
    PageTable pageTable;
    int lruCounter;
    TLB tlb;

    int max_pages;
    boolean fromFind = false;
    boolean fromRead = false;
    boolean fromWrite = false;


    @FXML
    private Label ramSize;
    @FXML
    private Button findButton;
    @FXML
    private Label enterPageNumber;
    @FXML
    private Label enterOffset;
    @FXML
    private TextField pageNumber;
    @FXML
    private TextField offset;
    @FXML
    private Button proceed;
    @FXML
    private Label error;
    @FXML
    private Button clear;
    @FXML
    private Label find;
    @FXML
    private Button readButton;
    @FXML
    private Label read;
    @FXML
    private Button writeButton;
    @FXML
    private Label enterValue;
    @FXML
    private TextField value;
    @FXML
    private Button replaceButton;
    @FXML
    private HBox hBox;
    @FXML
    private Button randomButton;
    @FXML
    private Button lfuButton;
    @FXML
    private Button mfuButton;
    @FXML
    private Button fifoButton;
    @FXML
    private Button lruButton;
    @FXML
    private Label writeStep1;
    @FXML
    private Label writeStep2;
    @FXML
    private Label writeStep3;
    @FXML
    private Label writeStep4;
    @FXML
    private TableView<Frame> memoryTable;
    @FXML
    private TableView<Page> pageTableView;
    @FXML
    private TableView<Frame> storageTable;
    @FXML
    private TableView<Page> tlbView;

    boolean initializeTable = true;
    boolean initializePageTable = true;
    boolean initializeStorageTable = true;
    boolean initializeTLB = true;

    public void setMemory(Memory memory){
        this.memory = memory;
        memoryTable = memory.importFrames(memoryTable,initializeTable,memory);
        initializeTable = false;
    }

    public void setStorage(Storage storage){
        this.storage = storage;
        storageTable = storage.importFrames(storageTable,initializeStorageTable,storage);
        initializeStorageTable = false;
    }

    public void setPageTable(PageTable pageTable){
        this.pageTable = pageTable;
        pageTableView = pageTable.importFrames(pageTableView,initializePageTable,pageTable);
        initializePageTable = false;
    }

    public void setLruCounter(int count){
        this.lruCounter = count;
        ramSize.setVisible(true);
        if(memory.getCapacity() == memory.getFrames().size()) {
            ramSize.setText("full");
        }
        else{
            ramSize.setText("not full");
        }
    }

    public void setTLB(TLB tlb){
        this.tlb = tlb;
        tlbView = tlb.importFrames(tlbView,initializeTLB,tlb);
        initializeTLB = false;
        System.out.println("size" + tlb.getRecentPages().size());
    }

    public void onButtonClicked(ActionEvent e){
        if(e.getSource().equals(findButton)){ //finds data at a specific logical address in main memory
            max_pages = 0;
            if(pageTable.getSize() == 4){
                max_pages = 523;
            }else if(pageTable.getSize() == 2){
                max_pages = 261;
            }else if(pageTable.getSize() == 1){
                max_pages = 130;
            }
            fromFind = true;
            enterPageNumber.setText("Page number between 0 and " + max_pages);
            enterPageNumber.setVisible(true);
            enterOffset.setVisible(true);
            pageNumber.setVisible(true);
            offset.setVisible(true);
            proceed.setVisible(true);
            proceed.disableProperty().bind(Bindings.isEmpty(pageNumber.textProperty()) .or(Bindings.isEmpty(offset.textProperty())));
        }else if(e.getSource().equals(readButton)) {
            max_pages = 0;
            if (pageTable.getSize() == 4) {
                max_pages = 523;
            } else if (pageTable.getSize() == 2) {
                max_pages = 261;
            } else if (pageTable.getSize() == 1) {
                max_pages = 130;
            }
            fromRead = true;
            enterPageNumber.setText("Page number between 0 and " + max_pages);
            enterPageNumber.setVisible(true);
            enterOffset.setVisible(true);
            pageNumber.setVisible(true);
            offset.setVisible(true);
            proceed.setVisible(true);
            proceed.disableProperty().bind(Bindings.isEmpty(pageNumber.textProperty()).or(Bindings.isEmpty(offset.textProperty())));
        }else if(e.getSource().equals(writeButton)) {
            max_pages = 0;
            if (pageTable.getSize() == 4) {
                max_pages = 523;
            } else if (pageTable.getSize() == 2) {
                max_pages = 261;
            } else if (pageTable.getSize() == 1) {
                max_pages = 130;
            }
            fromWrite = true;
            enterPageNumber.setText("Page number between 0 and " + max_pages);
            enterPageNumber.setVisible(true);
            enterOffset.setVisible(true);
            pageNumber.setVisible(true);
            offset.setVisible(true);
            enterValue.setVisible(true);
            value.setVisible(true);
            proceed.setVisible(true);
            proceed.disableProperty().bind(Bindings.isEmpty(pageNumber.textProperty()).or(Bindings.isEmpty(offset.textProperty())).or(Bindings.isEmpty(value.textProperty())));
        }else if(e.getSource().equals(replaceButton)){
            if (memory.getFrames().size() != memory.getCapacity()){
                error.setText("Can't replace a page until the memory is full");
                error.setVisible(true);
                clear.setVisible(true);
            }else{
                hBox.setVisible(true);
                max_pages = 0;
                if (pageTable.getSize() == 4) {
                    max_pages = 523;
                } else if (pageTable.getSize() == 2) {
                    max_pages = 261;
                } else if (pageTable.getSize() == 1) {
                    max_pages = 130;
                }
                enterPageNumber.setText("Page number between " + 0 + " and " + max_pages);
                enterPageNumber.setVisible(true);
                pageNumber.setVisible(true);
                randomButton.disableProperty().bind(Bindings.isEmpty(pageNumber.textProperty()));
                lfuButton.disableProperty().bind(Bindings.isEmpty(pageNumber.textProperty()));
                mfuButton.disableProperty().bind(Bindings.isEmpty(pageNumber.textProperty()));
                lruButton.disableProperty().bind(Bindings.isEmpty(pageNumber.textProperty()));
                fifoButton.disableProperty().bind(Bindings.isEmpty(pageNumber.textProperty()));
            }
        }else if(e.getSource().equals(proceed)) {
            if (fromFind) {
                try {
                    int pageNr = Integer.parseInt(pageNumber.getText());
                    int offsetNr = Integer.parseInt(offset.getText());
                    if (pageNr < 0 || pageNr > max_pages || offsetNr < 0 || offsetNr > 4096) {
                        throw new NumberFormatException();
                    }
                    String page = Integer.toBinaryString(pageNr);
                    String offset = Integer.toBinaryString(offsetNr);
                    for (int i = 20 - page.length(); i > 0; i--) {
                        page = "0" + page;
                    }
                    for (int i = 12 - offset.length(); i > 0; i--) {
                        offset = "0" + offset;
                    }
                    String address = page + offset;
                    CPU cpu = new CPU(Integer.parseInt(address, 2));
                    if (pageTable.getPageEntry().get(cpu.getPageNumber()).isPresent()) {
                        find.setText("The page is in RAM");
                        find.setVisible(true);
                        clear.setVisible(true);
                    } else {
                        find.setText("The page is on the disk");
                        find.setVisible(true);
                        clear.setVisible(true);
                    }
                } catch (NumberFormatException ex) {
                    error.setText("Please enter a valid number");
                    error.setVisible(true);
                    clear.setVisible(true);
                }
            } else if (fromRead) {
                try {
                    int pageNr = Integer.parseInt(pageNumber.getText());
                    int offsetNr = Integer.parseInt(offset.getText());
                    if (pageNr < 0 || pageNr > max_pages || offsetNr < 0 || offsetNr > 4096) {
                        throw new NumberFormatException();
                    }
                    String page = Integer.toBinaryString(pageNr);
                    String offset = Integer.toBinaryString(offsetNr);
                    for (int i = 20 - page.length(); i > 0; i--) {
                        page = "0" + page;
                    }
                    for (int i = 12 - offset.length(); i > 0; i--) {
                        offset = "0" + offset;
                    }
                    String address = page + offset;
                    CPU cpu = new CPU(Integer.parseInt(address, 2));
                    if (pageTable.getPageEntry().get(cpu.getPageNumber()).isPresent()) { // 1. if the address is in the main memory
                        int frameNumber = pageTable.getPageEntry().get(cpu.getPageNumber()).getFrameNumber();
                        System.out.println(memory.getFrames().get(frameNumber).getData().get(cpu.getPageOffset() / 32)); // because a number has 32 bits, and 128 * 32 = 4096 which is a frame size
                        read.setText(String.valueOf("The value is: " + memory.getFrames().get(frameNumber).getData().get(cpu.getPageOffset() / 32) + " Frame: " + frameNumber));
                        pageTable.getPageEntry().get(cpu.getPageNumber()).setTimesUsed(pageTable.getPageEntry().get(cpu.getPageNumber()).getTimesUsed() + 1);
                        pageTable.getPageEntry().get(cpu.getPageNumber()).setLru(lruCounter);
                        lruCounter++;
                        read.setVisible(true);
                        clear.setVisible(true);
                    } else if (!pageTable.getPageEntry().get(cpu.getPageNumber()).isPresent() && (memory.getFrames().size() < memory.getCapacity())) {
                        // put the page in main memory
                        memory.getFrames().add(storage.getFrames().get(pageTable.getPageEntry().get((cpu.getPageNumber())).getDiskNumber()));
                        pageTable.getPageEntry().get(cpu.getPageNumber()).setFrameNumber(memory.getFrames().size() - 1); //sets the new frame number
                        pageTable.getPageEntry().get(cpu.getPageNumber()).setPresent(true);
                        writeStep1.setText("The page was added in the main memory");
                        writeStep1.setVisible(true);
                        // then read the data
                        int frameNumber = pageTable.getPageEntry().get(cpu.getPageNumber()).getFrameNumber();
                        System.out.println(memory.getFrames().get(frameNumber).getData().get(cpu.getPageOffset() / 32));
                        read.setText(String.valueOf("The value is: " + memory.getFrames().get(frameNumber).getData().get(cpu.getPageOffset() / 32) + " Frame: " + frameNumber));
                        pageTable.getPageEntry().get(cpu.getPageNumber()).setTimesUsed(pageTable.getPageEntry().get(cpu.getPageNumber()).getTimesUsed() + 1);
                        pageTable.getPageEntry().get(cpu.getPageNumber()).setLru(lruCounter);
                        lruCounter++;
                        read.setVisible(true);
                        clear.setVisible(true);
                    } else if (!pageTable.getPageEntry().get(cpu.getPageNumber()).isPresent() && (memory.getFrames().size() == memory.getCapacity())) {
                        randomReplaceAlgorithm(cpu);
                        // then read the data
                        int frameNumber = pageTable.getPageEntry().get(cpu.getPageNumber()).getFrameNumber();
                        System.out.println(memory.getFrames().get(frameNumber).getData().get(cpu.getPageOffset() / 32));
                        read.setText(String.valueOf("The value is: " + memory.getFrames().get(frameNumber).getData().get(cpu.getPageOffset() / 32) + " Frame: " + frameNumber));
                        pageTable.getPageEntry().get(cpu.getPageNumber()).setTimesUsed(pageTable.getPageEntry().get(cpu.getPageNumber()).getTimesUsed() + 1);
                        pageTable.getPageEntry().get(cpu.getPageNumber()).setLru(lruCounter);
                        lruCounter++;
                        read.setVisible(true);
                        clear.setVisible(true);
                    }
                    memoryTable.getItems().clear();
                    memoryTable = memory.importFrames(memoryTable,initializeTable,memory);
                    storageTable.getItems().clear();
                    storageTable = storage.importFrames(storageTable,initializeStorageTable,storage);
                    pageTableView.getItems().clear();
                    pageTableView = pageTable.importFrames(pageTableView,initializePageTable,pageTable);
                    tlb.getRecentPages().remove(0);
                    tlb.getPageNumbers().remove(0);
                    tlb.getRecentPages().add(pageTable.getPageEntry().get(cpu.getPageNumber()));
                    tlb.getPageNumbers().add(pageTable.getPageEntry().indexOf(pageTable.getPageEntry().get(cpu.getPageNumber())));
                    for(int i=0;i<tlb.getPageNumbers().size();i++){
                        int nr = tlb.getPageNumbers().get(i);
                        Page updatePage = pageTable.getPageEntry().get(nr);
                        tlb.getRecentPages().set(i,updatePage);
                    }
                    tlbView.getItems().clear();
                    tlbView = tlb.importFrames(tlbView,initializeTLB,tlb);
                } catch (NumberFormatException ex) {
                    error.setText("Please enter a valid number");
                    error.setVisible(true);
                    clear.setVisible(true);
                }
            } else if (fromWrite) {
                try {
                    int pageNr = Integer.parseInt(pageNumber.getText());
                    int offsetNr = Integer.parseInt(offset.getText());
                    int valueNr = Integer.parseInt(value.getText());
                    if (pageNr < 0 || pageNr > max_pages || offsetNr < 0 || offsetNr > 4096) {
                        throw new NumberFormatException();
                    }
                    String page = Integer.toBinaryString(pageNr);
                    String offset = Integer.toBinaryString(offsetNr);
                    for (int i = 20 - page.length(); i > 0; i--) {
                        page = "0" + page;
                    }
                    for (int i = 12 - offset.length(); i > 0; i--) {
                        offset = "0" + offset;
                    }
                    String address = page + offset;
                    CPU cpu = new CPU(Integer.parseInt(address, 2));
                    if (pageTable.getPageEntry().get(cpu.getPageNumber()).isPresent()) { //1. it is in the main memory
                        int frameNumber = pageTable.getPageEntry().get(cpu.getPageNumber()).getFrameNumber();
                        writeStep1.setText("Before writing, the value was: " + memory.getFrames().get(frameNumber).getData().get(cpu.getPageOffset() / 32));
                        writeStep1.setVisible(true);
                        memory.getFrames().get(frameNumber).getData().set(cpu.getPageOffset() / 32, valueNr);
                        writeStep2.setText("After writing, the value is: " + memory.getFrames().get(frameNumber).getData().get(cpu.getPageOffset() / 32) + " Frame: " + frameNumber);
                        writeStep2.setVisible(true);
                        pageTable.getPageEntry().get(cpu.getPageNumber()).setDirtyBit(true);
                        pageTable.getPageEntry().get(cpu.getPageNumber()).setTimesUsed(pageTable.getPageEntry().get(cpu.getPageNumber()).getTimesUsed() + 1);
                        pageTable.getPageEntry().get(cpu.getPageNumber()).setLru(lruCounter);
                        lruCounter++;
                        clear.setVisible(true);
                    } else if (!pageTable.getPageEntry().get(cpu.getPageNumber()).isPresent() && (memory.getFrames().size() < memory.getCapacity())) {
                        // put the page in main memory
                        memory.getFrames().add(storage.getFrames().get(pageTable.getPageEntry().get((cpu.getPageNumber())).getDiskNumber()));
                        pageTable.getPageEntry().get(cpu.getPageNumber()).setFrameNumber(memory.getFrames().size() - 1); //sets the new frame number
                        pageTable.getPageEntry().get(cpu.getPageNumber()).setPresent(true);
                        writeStep1.setText("The page was added in the main memory");
                        writeStep1.setVisible(true);
                        // write
                        int frameNumber = pageTable.getPageEntry().get(cpu.getPageNumber()).getFrameNumber();
                        writeStep2.setText("Before writing, the value was: " + memory.getFrames().get(frameNumber).getData().get(cpu.getPageOffset() / 32));
                        writeStep2.setVisible(true);
                        memory.getFrames().get(frameNumber).getData().set(cpu.getPageOffset() / 32, valueNr);
                        writeStep3.setText("After writing, the value is: " + memory.getFrames().get(frameNumber).getData().get(cpu.getPageOffset() / 32) + " Frame: " + frameNumber);
                        writeStep3.setVisible(true);
                        pageTable.getPageEntry().get(cpu.getPageNumber()).setDirtyBit(true);
                        pageTable.getPageEntry().get(cpu.getPageNumber()).setTimesUsed(pageTable.getPageEntry().get(cpu.getPageNumber()).getTimesUsed() + 1);
                        pageTable.getPageEntry().get(cpu.getPageNumber()).setLru(lruCounter);
                        lruCounter++;
                        clear.setVisible(true);
                    } else if (!pageTable.getPageEntry().get(cpu.getPageNumber()).isPresent() && (memory.getFrames().size() == memory.getCapacity())) {
                        randomReplaceAlgorithm(cpu);
                        // write
                        int frameNumber = pageTable.getPageEntry().get(cpu.getPageNumber()).getFrameNumber();
                        System.out.println("before write " + memory.getFrames().get(frameNumber).getData().get(cpu.getPageOffset() / 32));
                        writeStep2.setText("Before writing, the value was: " + memory.getFrames().get(frameNumber).getData().get(cpu.getPageOffset() / 32));
                        writeStep2.setVisible(true);
                        memory.getFrames().get(frameNumber).getData().set(cpu.getPageOffset() / 32, valueNr);
                        System.out.println("after write " + memory.getFrames().get(frameNumber).getData().get(cpu.getPageOffset() / 32));
                        writeStep3.setText("After writing, the value is: " + memory.getFrames().get(frameNumber).getData().get(cpu.getPageOffset() / 32) + " Frame: " + frameNumber);
                        writeStep3.setVisible(true);
                        pageTable.getPageEntry().get(cpu.getPageNumber()).setDirtyBit(true);
                        pageTable.getPageEntry().get(cpu.getPageNumber()).setTimesUsed(pageTable.getPageEntry().get(cpu.getPageNumber()).getTimesUsed() + 1);
                        pageTable.getPageEntry().get(cpu.getPageNumber()).setLru(lruCounter);
                        lruCounter++;
                        clear.setVisible(true);
                    }
                    memoryTable.getItems().clear();
                    memoryTable = memory.importFrames(memoryTable,initializeTable,memory);
                    storageTable.getItems().clear();
                    storageTable = storage.importFrames(storageTable,initializeStorageTable,storage);
                    pageTableView.getItems().clear();
                    pageTableView = pageTable.importFrames(pageTableView,initializePageTable,pageTable);
                    tlb.getRecentPages().remove(0);
                    tlb.getPageNumbers().remove(0);
                    tlb.getRecentPages().add(pageTable.getPageEntry().get(cpu.getPageNumber()));
                    tlb.getPageNumbers().add(pageTable.getPageEntry().indexOf(pageTable.getPageEntry().get(cpu.getPageNumber())));
                    for(int i=0;i<tlb.getPageNumbers().size();i++){
                        int nr = tlb.getPageNumbers().get(i);
                        Page updatePage = pageTable.getPageEntry().get(nr);
                        tlb.getRecentPages().set(i,updatePage);
                    }
                    tlbView.getItems().clear();
                    tlbView = tlb.importFrames(tlbView,initializeTLB,tlb);
                } catch (NumberFormatException ex) {
                    error.setText("Please enter a valid number");
                    error.setVisible(true);
                    clear.setVisible(true);
                }
            }
        }else if(e.getSource().equals(randomButton)) {
            try {
                int pageNr = Integer.parseInt(pageNumber.getText());
                int offsetNr = 1;
                if (pageNr < 0 || pageNr > max_pages) {
                    throw new NumberFormatException();
                }
                if(pageTable.getPageEntry().get(pageNr).isPresent()){
                    throw new IllegalArgumentException();
                }
                String page = Integer.toBinaryString(pageNr);
                String offset = Integer.toBinaryString(offsetNr);
                for (int i = 20 - page.length(); i > 0; i--) {
                    page = "0" + page;
                }
                for (int i = 12 - offset.length(); i > 0; i--) {
                    offset = "0" + offset;
                }
                String address = page + offset;
                CPU cpu = new CPU(Integer.parseInt(address, 2));
                randomReplaceAlgorithm(cpu);
                clear.setVisible(true);
                memoryTable.getItems().clear();
                memoryTable = memory.importFrames(memoryTable,initializeTable,memory);
                storageTable.getItems().clear();
                storageTable = storage.importFrames(storageTable,initializeStorageTable,storage);
                pageTableView.getItems().clear();
                pageTableView = pageTable.importFrames(pageTableView,initializePageTable,pageTable);
                tlb.getRecentPages().remove(0);
                tlb.getPageNumbers().remove(0);
                tlb.getRecentPages().add(pageTable.getPageEntry().get(cpu.getPageNumber()));
                tlb.getPageNumbers().add(pageTable.getPageEntry().indexOf(pageTable.getPageEntry().get(cpu.getPageNumber())));
                for(int i=0;i<tlb.getPageNumbers().size();i++){
                    int nr = tlb.getPageNumbers().get(i);
                    Page updatePage = pageTable.getPageEntry().get(nr);
                    tlb.getRecentPages().set(i,updatePage);
                }
                tlbView.getItems().clear();
                tlbView = tlb.importFrames(tlbView,initializeTLB,tlb);
            } catch (NumberFormatException ex) {
                error.setText("Please enter a valid number");
                error.setVisible(true);
                clear.setVisible(true);
            } catch (IllegalArgumentException ex){
                error.setText("Please enter a page that is on the disk");
                error.setVisible(true);
                clear.setVisible(true);
            }
        }else if(e.getSource().equals(lfuButton)) {
            try {
                int pageNr = Integer.parseInt(pageNumber.getText());
                int offsetNr = 1;
                if (pageNr < 0 || pageNr > max_pages) {
                    throw new NumberFormatException();
                }
                if (pageTable.getPageEntry().get(pageNr).isPresent()) {
                    throw new IllegalArgumentException();
                }
                String page = Integer.toBinaryString(pageNr);
                String offset = Integer.toBinaryString(offsetNr);
                for (int i = 20 - page.length(); i > 0; i--) {
                    page = "0" + page;
                }
                for (int i = 12 - offset.length(); i > 0; i--) {
                    offset = "0" + offset;
                }
                String address = page + offset;
                CPU cpu = new CPU(Integer.parseInt(address, 2));
                lfuReplaceAlgorithm(cpu);
                clear.setVisible(true);
                memoryTable.getItems().clear();
                memoryTable = memory.importFrames(memoryTable,initializeTable,memory);
                storageTable.getItems().clear();
                storageTable = storage.importFrames(storageTable,initializeStorageTable,storage);
                pageTableView.getItems().clear();
                pageTableView = pageTable.importFrames(pageTableView,initializePageTable,pageTable);
                tlb.getRecentPages().remove(0);
                tlb.getPageNumbers().remove(0);
                tlb.getRecentPages().add(pageTable.getPageEntry().get(cpu.getPageNumber()));
                tlb.getPageNumbers().add(pageTable.getPageEntry().indexOf(pageTable.getPageEntry().get(cpu.getPageNumber())));
                for(int i=0;i<tlb.getPageNumbers().size();i++){
                    int nr = tlb.getPageNumbers().get(i);
                    Page updatePage = pageTable.getPageEntry().get(nr);
                    tlb.getRecentPages().set(i,updatePage);
                }
                tlbView.getItems().clear();
                tlbView = tlb.importFrames(tlbView,initializeTLB,tlb);
            } catch (NumberFormatException ex) {
                error.setText("Please enter a valid number");
                error.setVisible(true);
                clear.setVisible(true);
            } catch (IllegalArgumentException ex) {
                error.setText("Please enter a page that is on the disk");
                error.setVisible(true);
                clear.setVisible(true);
            }
        }else if(e.getSource().equals(mfuButton)) {
            try {
                int pageNr = Integer.parseInt(pageNumber.getText());
                int offsetNr = 1;
                if (pageNr < 0 || pageNr > max_pages) {
                    throw new NumberFormatException();
                }
                if (pageTable.getPageEntry().get(pageNr).isPresent()) {
                    throw new IllegalArgumentException();
                }
                String page = Integer.toBinaryString(pageNr);
                String offset = Integer.toBinaryString(offsetNr);
                for (int i = 20 - page.length(); i > 0; i--) {
                    page = "0" + page;
                }
                for (int i = 12 - offset.length(); i > 0; i--) {
                    offset = "0" + offset;
                }
                String address = page + offset;
                CPU cpu = new CPU(Integer.parseInt(address, 2));
                mfuReplaceAlgorithm(cpu);
                clear.setVisible(true);
                memoryTable.getItems().clear();
                memoryTable = memory.importFrames(memoryTable,initializeTable,memory);
                storageTable.getItems().clear();
                storageTable = storage.importFrames(storageTable,initializeStorageTable,storage);
                pageTableView.getItems().clear();
                pageTableView = pageTable.importFrames(pageTableView,initializePageTable,pageTable);
                tlb.getRecentPages().remove(0);
                tlb.getPageNumbers().remove(0);
                tlb.getRecentPages().add(pageTable.getPageEntry().get(cpu.getPageNumber()));
                tlb.getPageNumbers().add(pageTable.getPageEntry().indexOf(pageTable.getPageEntry().get(cpu.getPageNumber())));
                for(int i=0;i<tlb.getPageNumbers().size();i++){
                    int nr = tlb.getPageNumbers().get(i);
                    Page updatePage = pageTable.getPageEntry().get(nr);
                    tlb.getRecentPages().set(i,updatePage);
                }
                tlbView.getItems().clear();
                tlbView = tlb.importFrames(tlbView,initializeTLB,tlb);
            } catch (NumberFormatException ex) {
                error.setText("Please enter a valid number");
                error.setVisible(true);
                clear.setVisible(true);
            } catch (IllegalArgumentException ex) {
                error.setText("Please enter a page that is on the disk");
                error.setVisible(true);
                clear.setVisible(true);
            }
        }else if(e.getSource().equals(fifoButton)) {
            try {
                int pageNr = Integer.parseInt(pageNumber.getText());
                int offsetNr = 1;
                if (pageNr < 0 || pageNr > max_pages) {
                    throw new NumberFormatException();
                }
                if (pageTable.getPageEntry().get(pageNr).isPresent()) {
                    throw new IllegalArgumentException();
                }
                String page = Integer.toBinaryString(pageNr);
                String offset = Integer.toBinaryString(offsetNr);
                for (int i = 20 - page.length(); i > 0; i--) {
                    page = "0" + page;
                }
                for (int i = 12 - offset.length(); i > 0; i--) {
                    offset = "0" + offset;
                }
                String address = page + offset;
                CPU cpu = new CPU(Integer.parseInt(address, 2));
                fifoReplaceAlgorithm(cpu);
                clear.setVisible(true);
                memoryTable.getItems().clear();
                memoryTable = memory.importFrames(memoryTable,initializeTable,memory);
                storageTable.getItems().clear();
                storageTable = storage.importFrames(storageTable,initializeStorageTable,storage);
                pageTableView.getItems().clear();
                pageTableView = pageTable.importFrames(pageTableView,initializePageTable,pageTable);
                tlb.getRecentPages().remove(0);
                tlb.getPageNumbers().remove(0);
                tlb.getRecentPages().add(pageTable.getPageEntry().get(cpu.getPageNumber()));
                tlb.getPageNumbers().add(pageTable.getPageEntry().indexOf(pageTable.getPageEntry().get(cpu.getPageNumber())));
                for(int i=0;i<tlb.getPageNumbers().size();i++){
                    int nr = tlb.getPageNumbers().get(i);
                    Page updatePage = pageTable.getPageEntry().get(nr);
                    tlb.getRecentPages().set(i,updatePage);
                }
                tlbView.getItems().clear();
                tlbView = tlb.importFrames(tlbView,initializeTLB,tlb);
            } catch (NumberFormatException ex) {
                error.setText("Please enter a valid number");
                error.setVisible(true);
                clear.setVisible(true);
            } catch (IllegalArgumentException ex) {
                error.setText("Please enter a page that is on the disk");
                error.setVisible(true);
                clear.setVisible(true);
            }
        }else if(e.getSource().equals(lruButton)){
            try {
                int pageNr = Integer.parseInt(pageNumber.getText());
                int offsetNr = 1;
                if (pageNr < 0 || pageNr > max_pages) {
                    throw new NumberFormatException();
                }
                if(pageTable.getPageEntry().get(pageNr).isPresent()){
                    throw new IllegalArgumentException();
                }
                String page = Integer.toBinaryString(pageNr);
                String offset = Integer.toBinaryString(offsetNr);
                for (int i = 20 - page.length(); i > 0; i--) {
                    page = "0" + page;
                }
                for (int i = 12 - offset.length(); i > 0; i--) {
                    offset = "0" + offset;
                }
                String address = page + offset;
                CPU cpu = new CPU(Integer.parseInt(address, 2));
                lruReplaceAlgorithm(cpu);
                clear.setVisible(true);
                memoryTable.getItems().clear();
                memoryTable = memory.importFrames(memoryTable,initializeTable,memory);
                storageTable.getItems().clear();
                storageTable = storage.importFrames(storageTable,initializeStorageTable,storage);
                pageTableView.getItems().clear();
                pageTableView = pageTable.importFrames(pageTableView,initializePageTable,pageTable);
                tlb.getRecentPages().remove(0);
                tlb.getPageNumbers().remove(0);
                tlb.getRecentPages().add(pageTable.getPageEntry().get(cpu.getPageNumber()));
                tlb.getPageNumbers().add(pageTable.getPageEntry().indexOf(pageTable.getPageEntry().get(cpu.getPageNumber())));
                for(int i=0;i<tlb.getPageNumbers().size();i++){
                    int nr = tlb.getPageNumbers().get(i);
                    Page updatePage = pageTable.getPageEntry().get(nr);
                    tlb.getRecentPages().set(i,updatePage);
                }
                tlbView.getItems().clear();
                tlbView = tlb.importFrames(tlbView,initializeTLB,tlb);
            } catch (NumberFormatException ex) {
                error.setText("Please enter a valid number");
                error.setVisible(true);
                clear.setVisible(true);
            } catch (IllegalArgumentException ex){
                error.setText("Please enter a page that is on the disk");
                error.setVisible(true);
                clear.setVisible(true);
            }
        }else if(e.getSource().equals(clear)){
            pageNumber.clear();
            offset.clear();
            pageNumber.setVisible(false);
            offset.setVisible(false);
            enterPageNumber.setVisible(false);
            enterOffset.setVisible(false);
            enterValue.setVisible(false);
            value.setVisible(false);
            value.clear();
            proceed.setVisible(false);
            clear.setVisible(false);
            error.setVisible(false);
            find.setText("");
            find.setVisible(false);
            read.setText("");
            read.setVisible(false);
            fromFind = false;
            fromRead = false;
            fromWrite = false;
            hBox.setVisible(false);
            writeStep2.setText("");
            writeStep3.setText("");
            writeStep2.setVisible(false);
            writeStep3.setVisible(false);
            writeStep1.setText("");
            writeStep1.setVisible(false);
            writeStep4.setText("");
            writeStep4.setVisible(false);
            ramSize.setVisible(true);
            if(memory.getCapacity() == memory.getFrames().size()) {
                ramSize.setText("full");
            }
            else{
                ramSize.setText("not full");
            }
        }
    }

    public void randomReplaceAlgorithm(CPU cpu){
        //step 1
        Random random = new Random();
        int r = random.nextInt(memory.getFrames().size());
        Frame removeFrame = memory.getFrames().get(r);
        Page pageTableEntry = null;
        int iR = -1;
        int diskNumber = -1;
        for (int i = 0;i < pageTable.getPageEntry().size();i++){
            if(pageTable.getPageEntry().get(i).getFrameNumber() == r){
                pageTableEntry = pageTable.getPageEntry().get(i);
                iR = i;
            }
        }
        //step 2
        assert pageTableEntry != null;
        if (pageTableEntry.getDiskNumber() == -1) {
            diskNumber = storage.getFrames().size();
            storage.getFrames().add(diskNumber,removeFrame);
            pageTable.getPageEntry().get(iR).setDiskNumber(diskNumber);
        }else{
            if(pageTableEntry.isDirtyBit()){
                storage.getFrames().get(pageTableEntry.getDiskNumber()).setData(removeFrame.getData());
            }
        }
        //step 3
        memory.getFrames().remove(removeFrame);
        //step 4
        writeStep1.setText("Frame that is removed is: " + r + " corresponding to the page: " + iR);
        writeStep1.setVisible(true);
        pageTable.getPageEntry().get(iR).setPresent(false);
        pageTable.getPageEntry().get(iR).setFrameNumber(-1);
        pageTable.getPageEntry().get(iR).setDirtyBit(false);
        pageTable.getPageEntry().get(iR).setTimesUsed(0);
        //step 5
        // put the page in main memory
        memory.getFrames().add(storage.getFrames().get(pageTable.getPageEntry().get((cpu.getPageNumber())).getDiskNumber()));
        pageTable.getPageEntry().get(cpu.getPageNumber()).setFrameNumber(memory.getFrames().size() - 1); //sets the new frame number
        pageTable.getPageEntry().get(cpu.getPageNumber()).setPresent(true);

    }

    public void lfuReplaceAlgorithm(CPU cpu){
        //step 1
        int removeFrameNumber = 0;
        int lfu = Integer.MAX_VALUE;
        Page pageTableEntry = null;
        int iR = -1;
        for(int i = 0; i<pageTable.getPageEntry().size();i++){
            if(pageTable.getPageEntry().get(i).isPresent()){
                if(pageTable.getPageEntry().get(i).getTimesUsed() < lfu){
                    lfu = pageTable.getPageEntry().get(i).getTimesUsed();
                    removeFrameNumber = pageTable.getPageEntry().get(i).getFrameNumber();
                    pageTableEntry = pageTable.getPageEntry().get(i);
                    iR = i;
                }
            }
        }
        Frame removeFrame = memory.getFrames().get(removeFrameNumber);
        System.out.println("Frame that is removed is: " + removeFrameNumber + " used " + lfu + " times " + " corresponding to the page: " + iR);
        writeStep1.setText("Frame that is removed is: " + removeFrameNumber + " used " + lfu + " times " + " corresponding to the page: " + iR);
        writeStep1.setVisible(true);
        int diskNumber = -1;
        //step 2
        assert pageTableEntry != null;
        if (pageTableEntry.getDiskNumber() == -1) {
            diskNumber = storage.getFrames().size();
            storage.getFrames().add(diskNumber,removeFrame);
            pageTable.getPageEntry().get(iR).setDiskNumber(diskNumber);
        }else{
            if(pageTableEntry.isDirtyBit()){
                storage.getFrames().get(pageTableEntry.getDiskNumber()).setData(removeFrame.getData());
            }
        }
        //step 3
        memory.getFrames().remove(removeFrame);
        //step 4
        pageTable.getPageEntry().get(iR).setPresent(false);
        pageTable.getPageEntry().get(iR).setFrameNumber(-1);
        pageTable.getPageEntry().get(iR).setDirtyBit(false);
        pageTable.getPageEntry().get(iR).setTimesUsed(0);
        //step 5
        // put the page in main memory
        memory.getFrames().add(storage.getFrames().get(pageTable.getPageEntry().get((cpu.getPageNumber())).getDiskNumber()));
        pageTable.getPageEntry().get(cpu.getPageNumber()).setFrameNumber(memory.getFrames().size() - 1); //sets the new frame number
        pageTable.getPageEntry().get(cpu.getPageNumber()).setPresent(true);
    }
    public void mfuReplaceAlgorithm(CPU cpu){
        //step 1
        int removeFrameNumber = 0;
        int mfu = 0;
        Page pageTableEntry = null;
        int iR = -1;
        for(int i = 0; i<pageTable.getPageEntry().size();i++){
            if(pageTable.getPageEntry().get(i).isPresent()){
                if(pageTable.getPageEntry().get(i).getTimesUsed() > mfu){
                    mfu = pageTable.getPageEntry().get(i).getTimesUsed();
                    removeFrameNumber = pageTable.getPageEntry().get(i).getFrameNumber();
                    pageTableEntry = pageTable.getPageEntry().get(i);
                    iR = i;
                }
            }
        }
        Frame removeFrame = memory.getFrames().get(removeFrameNumber);
        System.out.println("Frame that is removed is: " + removeFrameNumber + " used " + mfu + " times " + " corresponding to the page: " + iR);
        writeStep1.setText("Frame that is removed is: " + removeFrameNumber + " used " + mfu + " times " + " corresponding to the page: " + iR);
        writeStep1.setVisible(true);
        int diskNumber = -1;
        //step 2
        assert pageTableEntry != null;
        if (pageTableEntry.getDiskNumber() == -1) {
            diskNumber = storage.getFrames().size();
            storage.getFrames().add(diskNumber,removeFrame);
            pageTable.getPageEntry().get(iR).setDiskNumber(diskNumber);
        }else{
            if(pageTableEntry.isDirtyBit()){
                storage.getFrames().get(pageTableEntry.getDiskNumber()).setData(removeFrame.getData());
            }
        }
        //step 3
        memory.getFrames().remove(removeFrame);
        //step 4
        pageTable.getPageEntry().get(iR).setPresent(false);
        pageTable.getPageEntry().get(iR).setFrameNumber(-1);
        pageTable.getPageEntry().get(iR).setDirtyBit(false);
        pageTable.getPageEntry().get(iR).setTimesUsed(0);
        //step 5
        // put the page in main memory
        memory.getFrames().add(storage.getFrames().get(pageTable.getPageEntry().get((cpu.getPageNumber())).getDiskNumber()));
        pageTable.getPageEntry().get(cpu.getPageNumber()).setFrameNumber(memory.getFrames().size() - 1); //sets the new frame number
        pageTable.getPageEntry().get(cpu.getPageNumber()).setPresent(true);
    }

    public void fifoReplaceAlgorithm(CPU cpu){
        //step 1
        int removeFrameNumber = 0;
        Frame removeFrame = memory.getFrames().get(removeFrameNumber);
        Page pageTableEntry = null;
        int iR = -1;
        int diskNumber = -1;
        for (int i = 0;i < pageTable.getPageEntry().size();i++){
            if(pageTable.getPageEntry().get(i).getFrameNumber() == removeFrameNumber){
                pageTableEntry = pageTable.getPageEntry().get(i);
                iR = i;
            }
        }
        //step 2
        assert pageTableEntry != null;
        if (pageTableEntry.getDiskNumber() == -1) {
            diskNumber = storage.getFrames().size();
            storage.getFrames().add(diskNumber,removeFrame);
            pageTable.getPageEntry().get(iR).setDiskNumber(diskNumber);
        }else{
            if(pageTableEntry.isDirtyBit()){
                storage.getFrames().get(pageTableEntry.getDiskNumber()).setData(removeFrame.getData());
            }
        }
        //step 3
        memory.getFrames().remove(removeFrame);
        //step 4
        System.out.println("Frame that is removed is: " + removeFrameNumber + " corresponding to the page: " + iR);
        writeStep1.setText("Frame that is removed is: " + removeFrameNumber + " corresponding to the page: " + iR);
        writeStep1.setVisible(true);
        pageTable.getPageEntry().get(iR).setPresent(false);
        pageTable.getPageEntry().get(iR).setFrameNumber(-1);
        pageTable.getPageEntry().get(iR).setDirtyBit(false);
        pageTable.getPageEntry().get(iR).setTimesUsed(0);
        //step 5
        // put the page in main memory
        memory.getFrames().add(storage.getFrames().get(pageTable.getPageEntry().get((cpu.getPageNumber())).getDiskNumber()));
        pageTable.getPageEntry().get(cpu.getPageNumber()).setFrameNumber(memory.getFrames().size() - 1); //sets the new frame number
        pageTable.getPageEntry().get(cpu.getPageNumber()).setPresent(true);

    }

    public void lruReplaceAlgorithm(CPU cpu){
        //step 1
        int removeFrameNumber = 0;
        int lru = Integer.MAX_VALUE;
        Page pageTableEntry = null;
        int iR = -1;
        for(int i = 0; i<pageTable.getPageEntry().size();i++){
            if(pageTable.getPageEntry().get(i).isPresent()){
                if(pageTable.getPageEntry().get(i).getLru() < lru){
                    lru = pageTable.getPageEntry().get(i).getLru();
                    removeFrameNumber = pageTable.getPageEntry().get(i).getFrameNumber();
                    pageTableEntry = pageTable.getPageEntry().get(i);
                    iR = i;
                }
            }
        }
        Frame removeFrame = memory.getFrames().get(removeFrameNumber);
        System.out.println("Frame that is removed is: " + removeFrameNumber + " last used at " + lru + " count, corresponding to the page: " + iR);
        writeStep1.setText("Frame that is removed is: " + removeFrameNumber + " last used at " + lru + " count, corresponding to the page: " + iR);
        writeStep1.setVisible(true);
        int diskNumber = -1;
        //step 2
        assert pageTableEntry != null;
        if (pageTableEntry.getDiskNumber() == -1) {
            diskNumber = storage.getFrames().size();
            storage.getFrames().add(diskNumber,removeFrame);
            pageTable.getPageEntry().get(iR).setDiskNumber(diskNumber);
        }else{
            if(pageTableEntry.isDirtyBit()){
                storage.getFrames().get(pageTableEntry.getDiskNumber()).setData(removeFrame.getData());
            }
        }
        //step 3
        memory.getFrames().remove(removeFrame);
        //step 4
        pageTable.getPageEntry().get(iR).setPresent(false);
        pageTable.getPageEntry().get(iR).setFrameNumber(-1);
        pageTable.getPageEntry().get(iR).setDirtyBit(false);
        pageTable.getPageEntry().get(iR).setTimesUsed(0);
        //step 5
        // put the page in main memory
        memory.getFrames().add(storage.getFrames().get(pageTable.getPageEntry().get((cpu.getPageNumber())).getDiskNumber()));
        pageTable.getPageEntry().get(cpu.getPageNumber()).setFrameNumber(memory.getFrames().size() - 1); //sets the new frame number
        pageTable.getPageEntry().get(cpu.getPageNumber()).setPresent(true);
    }
}
