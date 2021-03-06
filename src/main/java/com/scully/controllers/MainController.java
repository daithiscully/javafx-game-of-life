package com.scully.controllers;

import com.scully.Main;
import com.scully.models.Cell;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainController implements Initializable {

  // FXML variables
  @FXML
  private Canvas canvas;


  // Custom variables
  private int[] rows;
  private int[] columns;

  private List<Cell> cells;


  public MainController() {
    log.info("Main Controller constructor...");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    canvas.setHeight(Main.STAGE_HEIGHT);
    canvas.setWidth(Main.STAGE_WIDTH);
    rows = new int[(int) canvas.getHeight()];
    columns = new int[(int) canvas.getWidth()];

    log.info("rows : {}", rows.length);
    log.info("columns : {}", columns.length);

    // Cell creating loop
    createCells();
    log.info("cells : {}", cells.size());

    Task task = new Task() {
      @Override
      protected Object call() throws Exception {
        runGameOfLifeLoop();
        return null;
      }
    };
    new Thread(task).start();

  }

  private void runGameOfLifeLoop() {
    int loopCounter = 0;
    while (loopCounter < 1000000000) {
      try {
        Thread.sleep(1_000);
        // TODO: the logic for updating the status of Alive for each cell
        cells.forEach(cell -> cell.setAlive(Math.random() < 0.5));
        loopCounter++;
      } catch (InterruptedException e) {
        log.error(e.getMessage());
      }
    }
  }

  private void createCells() {
    cells = new ArrayList<>();
    for (int i = 0; i < rows.length; i = i + Cell.getHeight()) {
      for (int j = 0; j < columns.length; j = j + Cell.getWidth()) {
        Cell cell = new Cell();
        cell.setXPosition(i);
        cell.setYPosition(j);
        cell.setGraphicsContext(canvas.getGraphicsContext2D());
        cell.setNeighbours();
        log.info("cell @ {} : {} has {} neighbours", cell.getXPosition(), cell.getYPosition(), cell.getNeighbours().size());
        cells.add(cell);
      }
    }
    // TODO: set the initial cluster of alive cells
    cells.forEach(cell -> cell.setAlive(Math.random() < 0.5));
  }



}
