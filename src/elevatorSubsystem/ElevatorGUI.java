package elevatorSubsystem;

import static main.Floor.NUMBER_OF_FLOORS;
import static elevatorSubsystem.Elevator.NUMBER_OF_ELEVATORS;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ElevatorGUI implements Runnable {
    private JFrame frame;
    private JPanel mainPanel;
    private JTextArea[] status;
    private JLabel[] elevator1;
    private JLabel[] elevator2;
    private JLabel[] elevator3;
    private JLabel[] elevator4;
    private Runnable[] threads;
    private boolean[] signalUpdate;
    private ArrayList<Elevator> elevators;

    public ElevatorGUI() {
        frame = new JFrame("Elevator Simulation");
        mainPanel = new JPanel(new BorderLayout());
        status = new JTextArea[NUMBER_OF_ELEVATORS];
        elevators = new ArrayList<>();
        elevator1 = new JLabel[NUMBER_OF_FLOORS];
        elevator2 = new JLabel[NUMBER_OF_FLOORS];
        elevator3 = new JLabel[NUMBER_OF_FLOORS];
        elevator4 = new JLabel[NUMBER_OF_FLOORS];
        threads = new Runnable[NUMBER_OF_ELEVATORS];
        signalUpdate = new boolean[NUMBER_OF_ELEVATORS];
        createElevatorShafts();
        for (int i = 0; i < NUMBER_OF_ELEVATORS; ++i)
            status[i] = new JTextArea(0, 12);
        frame.add(mainPanel);
        createTopLayer();
        createWestLayer();
        createCenterLayer();
        createEastLayer();
    }

    private void createTopLayer() {
        JPanel topLayer = new JPanel(new BorderLayout());
        JPanel west = new JPanel();
        JPanel east = new JPanel();
        JPanel header = new JPanel(new GridLayout(1, 4));
        JLabel floors = new JLabel("Floors");
        JLabel status = new JLabel("Status");
        JLabel[] elevatorHeaders = new JLabel[NUMBER_OF_ELEVATORS];
        for (int i = 0; i < NUMBER_OF_ELEVATORS; ++i) {
            elevatorHeaders[i] = new JLabel("Elevator " + (i + 1));
            header.add(elevatorHeaders[i]);
        }
        west.add(floors);
        east.add(status);
        topLayer.add(west, BorderLayout.WEST);
        topLayer.add(header, BorderLayout.CENTER);
        topLayer.add(east, BorderLayout.EAST);
        mainPanel.add(topLayer, BorderLayout.NORTH);
    }

    private void createWestLayer() {
        JPanel west = new JPanel(new GridLayout(22, 1));
        JLabel[] floors = new JLabel[NUMBER_OF_FLOORS];
        for (int i = 0; i < NUMBER_OF_FLOORS; ++i)
            floors[i] = new JLabel("Floor " + (i + 1));

        for (int i = NUMBER_OF_FLOORS - 1; i >= 0; --i)
            west.add(floors[i]);

        mainPanel.add(west, BorderLayout.WEST);
    }

    private void createCenterLayer() {
        JPanel center = new JPanel(new GridLayout(1, NUMBER_OF_ELEVATORS));
        JPanel elevator1Panel = new JPanel(new GridLayout(NUMBER_OF_FLOORS, 1));
        JPanel elevator2Panel = new JPanel(new GridLayout(NUMBER_OF_FLOORS, 1));
        JPanel elevator3Panel = new JPanel(new GridLayout(NUMBER_OF_FLOORS, 1));
        JPanel elevator4Panel = new JPanel(new GridLayout(NUMBER_OF_FLOORS, 1));
        for (int row = 0; row < NUMBER_OF_ELEVATORS; ++row) {
            for (int col = NUMBER_OF_FLOORS - 1; col >= 0; --col) {
                switch (row) {
                    case 0 -> elevator1Panel.add(elevator1[col]);
                    case 1 -> elevator2Panel.add(elevator2[col]);
                    case 2 -> elevator3Panel.add(elevator3[col]);
                    case 3 -> elevator4Panel.add(elevator4[col]);
                    default -> throw new IllegalStateException("Unexpected value: " + row);
                }
            }
        }
        center.add(elevator1Panel);
        center.add(elevator2Panel);
        center.add(elevator3Panel);
        center.add(elevator4Panel);
        mainPanel.add(center, BorderLayout.CENTER);
    }

    private void createEastLayer() {
        JPanel east = new JPanel(new GridLayout(4, 1));

        for (int i = NUMBER_OF_ELEVATORS - 1; i >= 0; --i) {
            String elevatorStatusText = "Elevator " + (i + 1) + "\n" +
                    "Direction: \n" + "Door: \n" + "Operational: ";
            status[i].setText(elevatorStatusText);
            status[i].setBorder(BorderFactory.createLineBorder(Color.black));
            east.add(status[i]);
        }

        mainPanel.add(east, BorderLayout.EAST);
    }

    public void startGUI() {
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void updateElevatorStatus(int elevatorID) {
        ElevatorState moving = elevators.get(elevatorID).getMoving();
        ElevatorState door = elevators.get(elevatorID).getDoor();
        boolean operational = elevators.get(elevatorID).getOperational();
        int elevatorNumber = elevators.get(elevatorID).getElevatorNumber();

        String elevatorStatusText = "Elevator " + elevatorNumber + "\n" +
                "Direction: " + moving + "\n" + "Door: " + door + "\n" + "Operational: " + operational;
        status[elevatorNumber - 1].setText(elevatorStatusText);
    }

    public void updateElevatorLocation(int elevatorID) {

        int row = elevators.get(elevatorID).getFloorNumber() - 1;
        int elevatorNumber = elevators.get(elevatorID).getElevatorNumber() - 1;
        boolean occupied = elevators.get(elevatorID).getOccupied();
        boolean operational = elevators.get(elevatorID).getOperational();
        clearColumn(elevatorNumber);
        if (operational) {
            switch (elevatorNumber) {
                case 0 -> {
                    elevator1[row].setForeground(occupied ? Color.green : Color.red);
                    elevator1[row].setBackground(occupied ? Color.green : Color.red);
                }
                case 1 -> {
                    elevator2[row].setForeground(occupied ? Color.green : Color.red);
                    elevator2[row].setBackground(occupied ? Color.green : Color.red);
                }
                case 2 -> {
                    elevator3[row].setForeground(occupied ? Color.green : Color.red);
                    elevator3[row].setBackground(occupied ? Color.green : Color.red);
                }
                case 3 -> {
                    elevator4[row].setForeground(occupied ? Color.green : Color.red);
                    elevator4[row].setBackground(occupied ? Color.green : Color.red);
                }
            }
        } else {
            setColor(elevatorNumber, row, Color.black);
        }


    }

    private void setColor(int elevatorNumber, int floorNumber, Color color) {
        switch (elevatorNumber) {
            case 0 -> {
                elevator1[floorNumber].setForeground(color);
                elevator1[floorNumber].setBackground(color);
            }
            case 1 -> {
                elevator2[floorNumber].setForeground(color);
                elevator2[floorNumber].setBackground(color);
            }
            case 2 -> {
                elevator3[floorNumber].setForeground(color);
                elevator3[floorNumber].setBackground(color);
            }
            case 3 -> {
                elevator4[floorNumber].setForeground(color);
                elevator4[floorNumber].setBackground(color);
            }
        }
    }

    public void addElevator(Elevator elevator) {
        elevators.add(elevator);
    }

    @Override
    public void run() {
        createElevatorUpdatorThreads();
        for (Runnable r : threads) {
            new Thread(r).start();
        }

    }

    private void clearColumn(int col) {
        for (int i = 0; i < NUMBER_OF_FLOORS; ++i) {
            setColor(col, i, Color.white);
        }

    }

    private void createElevatorShafts() {
        for (int i = 0; i < NUMBER_OF_FLOORS; i++) {
            elevator1[i] = new JLabel();
            elevator2[i] = new JLabel();
            elevator3[i] = new JLabel();
            elevator4[i] = new JLabel();
            elevator1[i].setPreferredSize(new Dimension(150, 10));
            elevator2[i].setPreferredSize(new Dimension(150, 10));
            elevator3[i].setPreferredSize(new Dimension(150, 10));
            elevator4[i].setPreferredSize(new Dimension(150, 10));
            elevator1[i].setOpaque(true);
            elevator2[i].setOpaque(true);
            elevator3[i].setOpaque(true);
            elevator4[i].setOpaque(true);
            elevator1[i].setBackground(Color.white);
            elevator2[i].setBackground(Color.white);
            elevator3[i].setBackground(Color.white);
            elevator4[i].setBackground(Color.white);
            elevator1[i].setForeground(Color.white);
            elevator2[i].setForeground(Color.white);
            elevator3[i].setForeground(Color.white);
            elevator4[i].setForeground(Color.white);
            elevator1[i].setBorder(BorderFactory.createLineBorder(Color.black));
            elevator2[i].setBorder(BorderFactory.createLineBorder(Color.black));
            elevator3[i].setBorder(BorderFactory.createLineBorder(Color.black));
            elevator4[i].setBorder(BorderFactory.createLineBorder(Color.black));

        }
    }
    public void setSignalUpdate(int threadID){
        signalUpdate[threadID] = true;
    }

    private void createElevatorUpdatorThreads() {
        for (int i = 0; i < NUMBER_OF_ELEVATORS; i++) {
            int finalI = i;
            signalUpdate[i] = true;
            threads[i] = () -> {
                while (true) {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(signalUpdate[finalI]){
                        updateElevatorLocation(finalI);
                        updateElevatorStatus(finalI);
                        signalUpdate[finalI] = false;
                    }
                }
            };
        }
    }

}
