package oopTurtleGraphics;

import uk.ac.leedsbeckett.oop.OOPGraphics;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;


public class TurtleGraphics extends OOPGraphics {
    private Integer distance;
    private Integer angle;
    private String color;
    String[] cmd;

    List<String> usedCommand = new ArrayList<String>();

    public TurtleGraphics() {
        JFrame mainFrame = new JFrame();
        mainFrame.setLayout(new FlowLayout());

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        mainFrame.setJMenuBar(menuBar);

        // Create File menu
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        // Create Load Image menu item
        JMenuItem loadImageMenuItem = new JMenuItem("Load Image");
        loadImageMenuItem.addActionListener(e -> loadImage());
        fileMenu.add(loadImageMenuItem);

        // Create Save Image menu item
        JMenuItem saveImageMenuItem = new JMenuItem("Save Image");
        saveImageMenuItem.addActionListener(e -> saveImage());
        fileMenu.add(saveImageMenuItem);

        // Create Save Command menu item
        JMenuItem saveCommandMenuItem = new JMenuItem("Save Command");
        saveCommandMenuItem.addActionListener(e -> saveCommand());
        fileMenu.add(saveCommandMenuItem);

        // Create Load Command menu item
        JMenuItem loadCommandMenuItem = new JMenuItem("Load Command");
        loadCommandMenuItem.addActionListener(e -> loadCommand());
        fileMenu.add(loadCommandMenuItem);

        mainFrame.add(this);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }


    public class PolygonFrame extends JFrame {
        public PolygonFrame() {
            setSize(275, 230);
            setVisible(true);
        }

        public void paint(Graphics g) {
            super.paint(g);

            int xValues[] = {20, 40, 50, 30, 20, 15};
            int yValues[] = {50, 50, 60, 80, 80, 60};
            Polygon polygon1 = new Polygon(xValues, yValues, 6);

            g.drawPolygon(polygon1);
        }
    }

    void square(int side) {
        penDown();

        turnLeft(90);
        forward(side);

        turnLeft(90);
        forward(side);

        turnLeft(90);
        forward(side);

        turnLeft(90);
        forward(side);
    }

    void triangle(int side) {
        penDown();
        turnLeft(120);
        forward(side);

        turnLeft(120);
        forward(side);

        turnLeft(120);
        forward(side);
    }

    void triangle(int side1, int side2, int side3) {
        drawTriangle(side1, side2, side3);
    }

    private void drawTriangle(int e, int f, int g) {
        double a = (double) e;
        double b = (double) f;
        double c = (double) g;

        double cosA = (b * b + c * c - a * a) / (2 * b * c);
        double cosB = (a * a + c * c - b * b) / (2 * a * c);
        double cosC = (a * a + b * b - c * c) / (2 * a * b);
        double angleA = Math.toDegrees(Math.acos(cosA));
        double angleB = Math.toDegrees(Math.acos(cosB));
        double angleC = Math.toDegrees(Math.acos(cosC));
        int ang1 = (int) angleA;
        int ang2 = (int) angleB;
        int ang3 = (int) angleC;
        int x = 180 - ang1;
        int y = 180 - ang2;
        int z = 180 - ang3;
        penUp();
        forward(100);
        turnLeft(90);
        penDown();
        forward(g);
        turnLeft(y);
        forward(e);
        turnLeft(z);
        forward(f);
        penUp();
        reset();
    }

    boolean saveImage = false;
    boolean saveCommand = false;

    public void saveImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try {
                BufferedImage image = getBufferedImage();
                if (image != null) {
                    String format = "jpg"; // You can change the format as needed
                    ImageIO.write(image, format, selectedFile);
                    saveImage = true;
                } else {
                    JOptionPane.showMessageDialog(null, "No image to save.");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Unable to save the file: " + e.getMessage());
            }
        }
    }

    public void loadImage() {
        reset();
        clear();

        if (saveImage) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File inputFile = fileChooser.getSelectedFile();

                try {
                    BufferedImage image = ImageIO.read(inputFile);
                    if (image != null) {
                        setBufferedImage(image);
                    } else {
                        JOptionPane.showMessageDialog(null, "Couldn't load the file: Invalid image format.");
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Couldn't load the file: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Image is not saved yet.");
        }
    }

    public void saveCommand() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Command File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Limit file type to text files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File outputFile = fileChooser.getSelectedFile();

            try {
                FileWriter writer = new FileWriter(outputFile);
                for (String command : usedCommand) {
                    writer.write(command + "\n");
                }
                writer.close();
                JOptionPane.showMessageDialog(null, "Commands saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                saveCommand = true; // Assuming saveCommand is a class variable
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error while saving file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (result == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(null, "Operation cancelled by user.", "Cancelled", JOptionPane.WARNING_MESSAGE);
        }
    }


    public void loadCommand() {
        clear();

        StringBuilder loadedCommands = new StringBuilder();

        if (saveCommand) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File inputFile = fileChooser.getSelectedFile();

                try {
                    FileReader fileReader = new FileReader(inputFile);
                    BufferedReader reader = new BufferedReader(fileReader);
                    String line;

                    while ((line = reader.readLine()) != null) {
                        if (!line.trim().equalsIgnoreCase("saveimage")) {
                            processCommand(line); // Process each command read from the file
                            loadedCommands.append(line).append("\n"); // Appends loaded command to StringBuilder
                        }
                    }
                    reader.close();

                    JOptionPane.showMessageDialog(null, "Commands loaded and executed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                    // Show loaded commands
                    JOptionPane.showMessageDialog(null, loadedCommands.toString(), "Loaded Commands", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error while loading file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (result == JFileChooser.CANCEL_OPTION) {
                JOptionPane.showMessageDialog(null, "Load operation cancelled by user.", "Cancelled", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No commands saved yet.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }



    	
    	
    public void superabout() {
    	super.about();
    }
    	
@Override
    public void about() {
    	//N
        clear();
        reset();
        setStroke(5);
        forward(100);
        turnRight();
        forward(380);
        turnRight();
        penDown();
        forward(150);
        turnRight(160);
        forward(166);
        turnLeft(160);
        forward(150);
        
        //I
        
        setStroke(5);
        turnRight(90);
        penUp();
        setPenColour(Color.GREEN);
        forward(25);
        penDown();
        forward(100);
        turnRight(180);
        forward(50);
        turnLeft(90);
        forward(150);
        turnRight(90);
        penUp();
        forward(50);
        turnRight(180);
        penDown();
        forward(100);
        penUp();
        
//R
        setStroke(5);
        forward(30);
        turnLeft(90);
        penDown();
        setPenColour(Color.YELLOW);
        forward(150);
        turnRight(90);
        for (int i = 0; i < 9; i++) {
            forward(17);
            turnRight(22);
        }
        turnRight(210);
        forward(90);
        turnLeft(45);
 //T

        setStroke(5);
        setPenColour(Color.BLUE);
        penUp();
        forward(50);
        turnLeft(92);
        penDown();
        forward(150);
        turnLeft(90);
        forward(50);
        turnRight(180);
        forward(100);

  //I      
        setStroke(5);
        penUp();
        setPenColour(Color.WHITE);
        forward(30);
        penDown();
        forward(100);
        turnRight(180);
        forward(50);
        turnLeft(90);
        forward(150);
        turnRight(90);
        penUp();
        forward(50);
        turnRight(180);
        penDown();
        forward(100);
        penUp();
//M
        setStroke(5);
        setPenColour(Color.ORANGE);
        forward(35);
        turnLeft(90);
        penDown();
        forward(150);
        turnRight(160);
        forward(166);
        turnLeft(150);
        forward(166);
        turnRight(165);
        forward(165);
    }
@Override
public void clear() {
	super.clear();
  setxPos(20);
  setyPos(200);
  
}

//RESET
@Override
public void reset() {
	super.reset();
	}

	
    @Override
    public void processCommand(String command) {
        command = command.toLowerCase();
        cmd = command.split(" ");

        boolean validInstruction = false;
        try {
            while (!validInstruction) {
                if (cmd[0].equals("about")) {
                    if (cmd.length > 1) {
                    	JOptionPane.showMessageDialog(null, "Invalid command!");
                    } else {
                       superabout();
                        usedCommand.add(command);
                    }
                    validInstruction = true;
                    
                }
                
                if (cmd[0].equals("name")) {
                    	if (cmd.length > 1) {
                    		JOptionPane.showMessageDialog(null, "Invalid command!");
                    	} else {
                    		about();
                    		usedCommand.add(command);
                    }
                    validInstruction = true;}
               else if (cmd[0].equals("reset")) {
                    if (cmd.length > 1) {
                        JOptionPane.showMessageDialog(null, "Invalid command!");
                    } else {
                        reset();
                    }
                    usedCommand.add(command);
                    validInstruction = true;
                } else if (cmd[0].equals("clear")) {
                    if (cmd.length > 1) {
                        JOptionPane.showMessageDialog(null, "Invalid command!");
                    } else {
                        clear();
                        usedCommand.add(command);
                    }
                    validInstruction = true;
                } else if (cmd[0].equals("saveimage")) {
                    if (cmd.length > 1) {
                        JOptionPane.showMessageDialog(null, "Invalid command!");
                    } else {
                        saveImage();
                        usedCommand.add(command);
                    }
                    validInstruction = true;
                } else if (cmd[0].equals("loadimage")) {
                    if (cmd.length > 1) {
                        JOptionPane.showMessageDialog(null, "Invalid command!");
                    } else {
                        loadImage();
                        usedCommand.add(command);
                    }
                    validInstruction = true;
                } else if (cmd[0].equals("savecommand")) {
                    if (cmd.length > 1) {
                        JOptionPane.showMessageDialog(null, "Invalid command!");
                    } else {
                        saveCommand();
                        usedCommand.add(command);
                    }
                    validInstruction = true;
                } else if (cmd[0].equals("loadcommand")) {
                    if (cmd.length > 1) {
                        JOptionPane.showMessageDialog(null, "Invalid command!");
                    } else {
                        loadCommand();
                        usedCommand.add(command);
                    }
                    validInstruction = true;
                } else if (cmd[0].equals("pendown")) {
                    if (cmd.length > 1) {
                        JOptionPane.showMessageDialog(null, "Invalid command!");
                    } else {
                        penDown();
                        usedCommand.add(command);
                    }
                    validInstruction = true;
                } else if (cmd[0].equals("penup")) {
                    if (cmd.length > 1) {
                        JOptionPane.showMessageDialog(null, "Invalid command!");
                    } else {
                        penUp();
                        usedCommand.add(command);
                    }
                    validInstruction = true;
                } else if (cmd[0].equals("pencolor")) {
                    if (cmd.length > 2) {
                        JOptionPane.showMessageDialog(null, "Invalid command!");
                    } else {
                        color = String.valueOf(cmd[1]);
                        switch (color) {
                            case "red":
                                setPenColour(Color.RED);
                                break;
                            case "orange":
                                setPenColour(Color.ORANGE);
                                break;
                            case "green":
                                setPenColour(Color.GREEN);
                                break;
                            case "blue":
                                setPenColour(Color.BLUE);
                                break;
                            case "white":
                                setPenColour(Color.WHITE);
                                break;
                            default:
                                JOptionPane.showMessageDialog(null, "No pen colour is selected.");
                                break;
                        }
                    }
                    usedCommand.add(command);
                    validInstruction = true;
                } else if (cmd[0].equals("turnleft")) {
                    if (cmd.length > 2) {
                        JOptionPane.showMessageDialog(null, "Invalid command! Need 2 parameter.");
                    } else if (cmd.length == 1) {
                        turnLeft();
                        usedCommand.add(command);
                    } else if (cmd.length == 2) {
                        angle = Integer.valueOf(cmd[1]);
                        turnLeft(angle);
                        usedCommand.add(command);
                    }
                    validInstruction = true;
                } else if (cmd[0].equals("turnright")) {
                    if (cmd.length > 2) {
                        JOptionPane.showMessageDialog(null, "Invalid command! Need 2 parameter.");
                    } else if (cmd.length == 1) {
                        turnRight();
                        usedCommand.add(command);
                    } else if (cmd.length == 2) {
                        angle = Integer.valueOf(cmd[1]);
                        turnRight(angle);
                        usedCommand.add(command);
                    }
                    validInstruction = true;
                } else if (cmd[0].equals("forward")) {
                    if (cmd.length != 2) {
                        JOptionPane.showMessageDialog(null, "Invalid command! Need 2 parameter.");
                    } else {
                        distance = Integer.valueOf(cmd[1]);
                        forward(distance);
                        usedCommand.add(command);
                    }
                    validInstruction = true;
                } else if (cmd[0].equals("backward")) {
                    if (cmd.length != 2) {
                        JOptionPane.showMessageDialog(null, "Invalid command! Need 2 parameter.");
                    } else {
                        distance = Integer.valueOf(cmd[1]);
                        forward(-distance);
                        usedCommand.add(command);
                    }
                    validInstruction = true;
                } else if (cmd[0].equals("penwidth")) {
                    if (cmd.length != 2) {
                        JOptionPane.showMessageDialog(null, "Invalid command! Need 2 parameter.");
                    } else {
                        int width = Integer.valueOf(cmd[1]);
                        setStroke(width);
                        usedCommand.add(command);
                    }
                    validInstruction = true;
                    
                }
                else if (cmd[0].equals("triangle")) {
                	
                	if (cmd.length<2 || cmd.length==3) {
                        JOptionPane.showMessageDialog(null, "Invalid!! side for triangle");
                    }
                	
                	try {
                		if(cmd.length==2) {
                			int side = Integer.valueOf(cmd[1]);
	                        triangle(side);
                		}
                		else if(Integer.parseInt(cmd[1])>0 && Integer.parseInt(cmd[2])>0 && Integer.parseInt(cmd[3])>0) {
                			triangle(Integer.parseInt(cmd[1]),Integer.parseInt(cmd[2]), Integer.parseInt(cmd[3]));
	                	}
                		
                		usedCommand.add(command);
                        validInstruction = true;
                		
                	}catch(Exception ex) {
                		JOptionPane.showMessageDialog(null, ex);
                	}
                    
                } else if (cmd[0].equals("square")) {
                    if (cmd.length > 2) {
                        JOptionPane.showMessageDialog(null, "Invalid command!");
                    } else {
                        int side = Integer.valueOf(cmd[1]);
                        square(side);
                    }
                    usedCommand.add(command);
                    validInstruction = true;
                } else if (cmd[0].equals("circle")) {
                    if (cmd.length > 2) {
                        JOptionPane.showMessageDialog(null, "Invalid command! Need 1 parameter.");
                    } else if (cmd.length == 2) {
                        int radius = Integer.valueOf(cmd[1]);
                        circle(radius);
                        usedCommand.add(command);
                    }
                    validInstruction = true;
                    
                } 
                if (!cmd[0].equals("reset") &&
                        !cmd[0].equals("clear") &&
                        !cmd[0].equals("saveimage") &&
                        !cmd[0].equals("loadimage") &&
                        !cmd[0].equals("savecommand") &&
                        !cmd[0].equals("loadcommand") &&
                        !cmd[0].equals("pendown") &&
                        !cmd[0].equals("penup") &&
                        !cmd[0].equals("pencolor") &&
                        !cmd[0].equals("turnleft") &&
                        !cmd[0].equals("turnright") &&
                        !cmd[0].equals("forward") &&
                        !cmd[0].equals("backward") &&
                        !cmd[0].equals("penwidth") &&
                        !cmd[0].equals("triangle") &&
                        !cmd[0].equals("square") &&
                        !cmd[0].equals("circle") &&
                        !cmd[0].equals("about") &&
                        !cmd[0].equals("name"))
                    {

                        
                        JOptionPane.showMessageDialog(null, "Invalid command!", "Error", JOptionPane.ERROR_MESSAGE);
                        validInstruction = true;
                    }
                    
                }
                
                
                  } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Invalid command! Too many parameters provided.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid command! Non-numeric data is found.");
        }
    }
}