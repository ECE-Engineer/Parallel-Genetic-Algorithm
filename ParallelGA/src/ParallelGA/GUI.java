package ParallelGA;

import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kyle Zeller
 * This class provides access to the GUI and threads that are running multiple instances of the Genetic Algorithm
 * in order to find an optimal solution.
 */

public class GUI extends JFrame {
    /**
     * The constructor calls initComponents() to initialize all of the components of the graphical user interface
     */
    public GUI() {
        initComponents();
    }

    /**
     * Initializes all the components within the GUI
     */
    private void initComponents() {
        panel1 = new JPanel();
        label1 = new JLabel();
        button1 = new JButton();
        label3 = new JLabel();
        textField1 = new JTextField();
        label4 = new JLabel();
        panel2 = new JPanel();
        labelWinnerChromosome = new Canvas();
        //create the canvases for the populations
        canvases = new Canvas[canvasNum];
        for (int i = 0; i < canvasNum; ++i) {
            canvases[i] = new Canvas();
        }

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        //======== this ========
        Container contentPane = getContentPane();

        //======== panel1 ========
        {
            panel1.setBackground(new Color(51, 204, 255));

            //---- label1 ----
            label1.setText("Genetic Algorithm");
            label1.setHorizontalAlignment(SwingConstants.CENTER);
            label1.setFont(new Font("Segoe UI", Font.BOLD, 30));
            label1.setForeground(Color.black);

            //---- button1 ----
            button1.setText("OK");
            button1.setBackground(new Color(51, 204, 255));
            button1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    try {
                        okButton(evt);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            //---- label3 ----
            label3.setText("Please Select EVEN Core amount (32 - " + MAX_CORES + "):");
            label3.setFont(label3.getFont().deriveFont(label3.getFont().getStyle() | Font.BOLD));
            label3.setForeground(Color.black);


            //---- label4 ----
            label4.setText("Best Fit Chromosome:");
            label4.setFont(label4.getFont().deriveFont(label4.getFont().getStyle() | Font.BOLD));
            label4.setForeground(Color.black);

            labelWinnerChromosome.setPreferredSize(new Dimension(200, 200));
            labelWinnerChromosome.setMaximumSize(new Dimension(200, 200));
            labelWinnerChromosome.setMinimumSize(new Dimension(200, 200));
            labelWinnerChromosome.setBackground(Color.black);

            //adjusts the layout of the left panel
            GroupLayout panel1Layout = new GroupLayout(panel1);
            panel1Layout.setHonorsVisibility(false);
            panel1.setLayout(panel1Layout);
            panel1Layout.setHorizontalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGroup(panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGap(152, 152, 152)
                                .addComponent(label1)
                            )
                        )
                    )
                    .addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                            //adjusts the gap to the left of the text
                        .addGap(60, 114, Short.MAX_VALUE)
                        .addGroup(panel1Layout.createParallelGroup()
                            .addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                                .addComponent(button1)
                                .addGap(276, 276, 276)
                            )
                            .addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                                    .addComponent(labelWinnerChromosome)
                                    .addGap(210, 210, 210)
                            )
                            .addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                                .addGroup(panel1Layout.createParallelGroup()
                                    .addGroup(panel1Layout.createSequentialGroup()
                                        .addComponent(label3)
                                        .addGap(45, 45, 45)
                                        .addComponent(textField1, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                    )
                                )
                                    //adjusts the gap to right of the text
                                .addGap(128, 128, 128)
                            )
                        )
                    )
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGroup(panel1Layout.createParallelGroup()
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addGap(235, 235, 235)
                                .addComponent(label4)
                            )
                        )
                    )
            );
            panel1Layout.setVerticalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                            //adjusts the gap above the top text
                        .addContainerGap()
                        .addComponent(label1)
                            //adjusts the gap between both texts
                        .addGap(81, 81, 81)
                        .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(label3, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
                            //adjusts the gap below the bottom text
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addComponent(button1)
                        .addComponent(label4, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                         .addComponent(labelWinnerChromosome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            //adjusts the gap below the image
                        .addGap(386, 386, 386)
                    )
            );
        }

        //======== panel2 ========
        {
            for (int i = 0; i < canvasNum; ++i) {
                canvases[i].setPreferredSize(new Dimension(200, 200));
                canvases[i].setMaximumSize(new Dimension(200, 200));
                canvases[i].setMinimumSize(new Dimension(200, 200));
                canvases[i].setBackground(Color.black);
            }

            //adjusts the layout of the right panel
            GroupLayout panel2Layout = new GroupLayout(panel2);
            panel2.setLayout(panel2Layout);
            panel2Layout.setHorizontalGroup(
                panel2Layout.createParallelGroup()
                    .addGroup(panel2Layout.createSequentialGroup()
                        .addGroup(panel2Layout.createParallelGroup()
                            .addGroup(panel2Layout.createSequentialGroup()
                                .addGroup(panel2Layout.createParallelGroup()
                                        .addComponent(canvases[0])
                                )
                                    .addGap(10, 10, 10)
                                .addGroup(panel2Layout.createParallelGroup()
                                        .addComponent(canvases[1])
                                )
                                    .addGap(10, 10, 10)
                                .addGroup(panel2Layout.createParallelGroup()
                                        .addComponent(canvases[2])
                                )
                                    .addGap(10, 10, 10)
                                .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(canvases[3])
                                )
                            )
                            .addGroup(panel2Layout.createSequentialGroup()
                                    .addGroup(panel2Layout.createParallelGroup()
                                            .addComponent(canvases[4])
                                    )
                                    .addGap(10, 10, 10)
                                    .addGroup(panel2Layout.createParallelGroup()
                                            .addComponent(canvases[5])
                                    )
                                    .addGap(10, 10, 10)
                                    .addGroup(panel2Layout.createParallelGroup()
                                            .addComponent(canvases[6])
                                    )
                                    .addGap(10, 10, 10)
                                    .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                            .addComponent(canvases[7])
                                    )
                            )
                            .addGroup(panel2Layout.createSequentialGroup()
                                    .addGroup(panel2Layout.createParallelGroup()
                                            .addComponent(canvases[8])
                                    )
                                    .addGap(10, 10, 10)
                                    .addGroup(panel2Layout.createParallelGroup()
                                            .addComponent(canvases[9])
                                    )
                                    .addGap(10, 10, 10)
                                    .addGroup(panel2Layout.createParallelGroup()
                                            .addComponent(canvases[10])
                                    )
                                    .addGap(10, 10, 10)
                                    .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                            .addComponent(canvases[11])
                                    )
                            )
                            .addGroup(panel2Layout.createSequentialGroup()
                                    .addGroup(panel2Layout.createParallelGroup()
                                            .addComponent(canvases[12])
                                    )
                                    .addGap(10, 10, 10)
                                    .addGroup(panel2Layout.createParallelGroup()
                                            .addComponent(canvases[13])
                                    )
                                    .addGap(10, 10, 10)
                                    .addGroup(panel2Layout.createParallelGroup()
                                            .addComponent(canvases[14])
                                    )
                                    .addGap(10, 10, 10)
                                    .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                            .addComponent(canvases[15])
                                    )
                            )
                        )
                    )
            );
            panel2Layout.setVerticalGroup(
                panel2Layout.createParallelGroup()
                    .addGroup(panel2Layout.createSequentialGroup()
                        .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(canvases[0], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(canvases[1], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(canvases[2], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(canvases[3], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        )
                            .addGap(10, 10, 10)
                         .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(canvases[4], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(canvases[5], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(canvases[6], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(canvases[7], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                         )
                            .addGap(10, 10, 10)
                         .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(canvases[8], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(canvases[9], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(canvases[10], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(canvases[11], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                             )
                            .addGap(10, 10, 10)
                         .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(canvases[12], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(canvases[13], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(canvases[14], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(canvases[15], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                         )
                    )
            );
        }

        //adjusts the layout of the contentpane
        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                //adjusts the whitespace to the left of the text
                                .addContainerGap()
                                .addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                //adjusts the whitespace to the right of the text
                                .addGap(103, 103, 103)
                        )
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                //adjusts the whitespace above the text
                                .addContainerGap()
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(panel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                //adjusts the whitespace below the text
                                .addContainerGap()
                        )
        );
        //adjusts the frame
        setSize(1485, 892);
        setLocationRelativeTo(getOwner());
    }

    //constant for core limit
    private final int MAX_CORES = 88;
    //constant for canvas limit
    private final int canvasNum = 16;
    //constant for chromosome grid size limit
    private final int ChromosomeGridSize = 8;
    //constant for when to swap elements
    private final int swapingGenerationConst = 50;
    //constant for the limit of generations
    private final int generationNum = 10000;
    //constant for waiting
    private final int sleepConst = 10;
    //constant for the timeout
    private final int timeoutConst = 10;
    //constant for the minimal error
    private final double errorDiffThreshold = 0.005;
    //left panel
    private JPanel panel1;
    //text at the top
    private JLabel label1;
    //button
    private JButton button1;
    //text to the left of the textfield
    private JLabel label3;
    //textfield
    private JTextField textField1;
    //bottom text
    private JLabel label4;
    //right panel
    private JPanel panel2;
    //array of canvases
    private Canvas[] canvases;
    //canvas for the final solution
    private Canvas labelWinnerChromosome;
    //array of bufferedimages
    BufferedImage bufferedImage[];
    //bufferedimage for the final solution
    BufferedImage bufferedImageChrom = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
    //array of integer arrays
    int[][] pixels;
    //array of integers for the final solution
    int[] pixelsChrom  = ((DataBufferInt) bufferedImageChrom.getRaster().getDataBuffer()).getData();
    //array of chromosomes
    Chromosome[] bestChromosomes;
    //blocking queue
    BlockingQueue<Chromosome> queue;

    /**
     * Renders the image of the final solution
     * @param c canvas to draw on
     * @param arr array of students
     */
    private void renderChromosome(Canvas c, Student[] arr) {
        BufferStrategy bs = c.getBufferStrategy();
        if (bs == null) {
            c.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        int gridSize = ((int)Math.sqrt(pixelsChrom.length));

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                pixelsChrom[(i * gridSize) + j] = arr[(((int)i/ChromosomeGridSize) * ChromosomeGridSize) + ((int)j/ChromosomeGridSize)].getStuAff()*256*256;
            }
        }

        g.drawImage(bufferedImageChrom, 0, 0, 200, 200, c);
        g.dispose();
        bs.show();
    }

    /**
     * Renders an image of the given chromosome array
     * @param c canvas to draw on
     * @param arr array of chromosomes
     * @param bufferSelect bufferedimage
     * @param pixelArr integer array
     */
    private void renderPopulations(Canvas c, Chromosome[] arr, BufferedImage bufferSelect, int[] pixelArr) {

        BufferStrategy bs = c.getBufferStrategy();
        if (bs == null) {
            c.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        int gridSize = ((int)Math.sqrt(pixelArr.length));

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                pixelArr[(i * gridSize) + j] = arr[(((int)i/ChromosomeGridSize) * ChromosomeGridSize) + ((int)j/ChromosomeGridSize)].getAverageRGB();////////I COULD CHANGE THIS TO RENDER THE INDIVIDUAL CHROMOSOMES!!!! instead of their averages
            }
        }

        g.drawImage(bufferSelect, 0, 0, 200, 200, c);
        g.dispose();
        bs.show();
    }

    /**
     * Runs all N-specified threads in parallel to calculate the best solution for seating arrangements whereas
     * each thread will run a Genetic Algorithm.
     * @param evt user clicks button
     * @throws Exception
     */
    private void okButton(java.awt.event.ActionEvent evt) throws Exception {
        //set all the fields
        String line = "";
        line = textField1.getText();
        //initialize the exchanger
        Exchanger<Population> exchanger = new Exchanger<>();

        //check to see if the user gave valid input
        if (!line.isEmpty()) {
            //set number of cores
            int coreNum = Integer.parseInt(line);
            bufferedImage = new BufferedImage[canvasNum];
            pixels = new int[canvasNum][];

            if (coreNum >= 16 && coreNum < 89 && coreNum%2==0) {

                //initialize array of the best solutions
                bestChromosomes = new Chromosome[coreNum];
                //initialize the ArrayBlockingQueue
                queue = new ArrayBlockingQueue<Chromosome>(coreNum);

                //start the thread that will wait for all the sub-threads to finish handing off their best solutions
                new Thread(() -> {
                    int incrementer = 0;
                    while(true) try {
                        Chromosome tempChrom;
                        while((tempChrom = queue.take()).getFitness() !=0) {
                            Thread.sleep(10);
                            bestChromosomes[incrementer] = tempChrom;

                            //check to see if all the sub-threads finished handing off their best solutions
                            if (incrementer == (coreNum-1)) {

                                //find the overall best solution and display it
                                double[] fitArr = new double[coreNum];
                                for (int j = 0; j < coreNum; ++j) {
                                    fitArr[j] = bestChromosomes[j].getFitness();
                                }

                                final double[] copy = new double[fitArr.length];
                                System.arraycopy(fitArr, 0, copy, 0, copy.length);

                                Arrays.sort(fitArr);

                                int pos = 0;
                                for (int j = 0; j < coreNum; ++j) {
                                    if (fitArr[0]==copy[j]) {
                                        pos = j;
                                    }
                                }
                                this.renderChromosome(labelWinnerChromosome, bestChromosomes[pos].getSeatArr());
                                this.renderChromosome(labelWinnerChromosome, bestChromosomes[pos].getSeatArr());
                                this.renderChromosome(labelWinnerChromosome, bestChromosomes[pos].getSeatArr());
                                String answer = "Fitness Value is:\t" + bestChromosomes[pos].getFitness();
                                JOptionPane.showMessageDialog(this, answer);
                                break;
                            }
                            ++incrementer;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();

                Thread[] threads = new Thread[coreNum];
                for (int i = 0; i < coreNum; ++i) {

                    //initialize only 16 canvas to render on
                    if (i < canvasNum) {
                        bufferedImage[i] = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
                        pixels[i] = ((DataBufferInt) bufferedImage[i].getRaster().getDataBuffer()).getData();
                    }

                    BufferedImage threadBufferedImage = (i < canvasNum) ? bufferedImage[i] : null;
                    int[] threadPixels = (i < canvasNum) ? pixels[i] : null;
                    Canvas threadCanvas = (i < canvasNum) ? canvases[i] : null;

                    int renderingNum = i;
                    //start the threads that will run the Genetic Algorithms until they meet an error threshold or generation count criteria
                    threads[renderingNum] = new Thread(() -> {
                        GA geneticAlgorithm = new GA();
                        int counter = 0;
                        double pastVal = 0;
                        double currentVal;
                        while(true) try {
                            Population[] allPopulations = geneticAlgorithm.getPopulations();

                            //render the images of the first 16 threads
                            if (renderingNum < canvasNum) {
                                this.renderPopulations(threadCanvas, geneticAlgorithm.getBestPopulation().getChromosomes(), threadBufferedImage, threadPixels);
                            }
                            try {
                                //swap between 2 threads every 25 generations
                                if (counter%swapingGenerationConst == 0) {
                                    Thread.sleep(renderingNum*sleepConst);
                                    //pick a random population
                                    int pos = ThreadLocalRandom.current().nextInt(allPopulations.length);
                                    Population popElement = allPopulations[pos];
                                    exchanger.exchange(popElement, renderingNum*sleepConst*timeoutConst, TimeUnit.MILLISECONDS);

                                    //set the changes
                                    for (int j = 0; j < allPopulations.length; ++j) {
                                        if (j == pos) {
                                            allPopulations[j] = popElement;
                                            break;
                                        }
                                    }
                                }

                                currentVal = geneticAlgorithm.getGenerationAverage();

                                if (counter > 0) {
                                    double percentDiff = Math.abs((double)Math.abs(Math.abs(pastVal) - Math.abs(currentVal))/(double)((pastVal + currentVal)/2))*100;
                                    if (counter == generationNum || percentDiff < errorDiffThreshold) {
                                        //USE EXCHANGERS TO UPDATE THE THE CHROMOSOME BEST SOLUTION ON THE MAIN THREAD
                                        Chromosome threadBestChromosome = geneticAlgorithm.getBestChromosomeOverall();
                                        try {
                                            Thread.sleep(renderingNum);
                                            queue.put(threadBestChromosome);
                                        } catch (InterruptedException f) {
                                            f.printStackTrace();
                                        }
                                        break;
                                    }
                                }

                                pastVal = currentVal;
                                ++counter;
                                geneticAlgorithm = new GA(geneticAlgorithm.nextGeneration());
                            } catch (java.util.concurrent.TimeoutException g){

                                currentVal = geneticAlgorithm.getGenerationAverage();

                                if (counter > 0) {
                                    double percentDiff = Math.abs((double)Math.abs(Math.abs(pastVal) - Math.abs(currentVal))/(double)((pastVal + currentVal)/2))*100;
                                    if (counter == generationNum || percentDiff < errorDiffThreshold) {
                                        //USE EXCHANGERS TO UPDATE THE THE CHROMOSOME BEST SOLUTION ON THE MAIN THREAD
                                        Chromosome threadBestChromosome = geneticAlgorithm.getBestChromosomeOverall();
                                        try {
                                            Thread.sleep(renderingNum);
                                            queue.put(threadBestChromosome);
                                        } catch (InterruptedException f) {
                                            f.printStackTrace();
                                        }
                                        break;
                                    }
                                }

                                pastVal = currentVal;

                                ++counter;
                                geneticAlgorithm = new GA(geneticAlgorithm.nextGeneration());
				            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }

                //start all the threads
                for (Thread thread : threads) {
                    thread.start();
                }
            }
        }
    }
}
