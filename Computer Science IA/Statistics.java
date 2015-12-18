import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

public class Statistics
{
   private JFrame mainFrame = new JFrame("Statistics Program");
   private DefaultListModel<String> mdl = new DefaultListModel<String>();
   private JList<String> outputList = new JList<String>(mdl);
   
   public Statistics()
   {
      JPanel mainPanel = new JPanel(new BorderLayout());
      JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 10, 10));
      
      ActionListener listener = new ClickListener(this);
      
      JButton input = new JButton("Click to add input");
      input.addActionListener(listener);
      input.setActionCommand("input");
      
      JButton compute = new JButton("Compute");
      compute.addActionListener(listener);
      compute.setActionCommand("compute");
      
      JButton graph = new JButton("Show Graph");
      graph.addActionListener(listener);
      graph.setActionCommand("graph");
      
      JButton reset = new JButton("Reset");
      reset.addActionListener(listener);
      reset.setActionCommand("reset");
      
      JButton save = new JButton("Save");
      save.addActionListener(listener);
      save.setActionCommand("save");
      
      JButton load = new JButton("Load");
      load.addActionListener(listener);
      load.setActionCommand("load");
      
      JButton color = new JButton("Color");
      color.addActionListener(listener);
      color.setActionCommand("color");
      
      JScrollPane listPane = new JScrollPane(outputList);
      listPane.setPreferredSize(new Dimension(250, 200));
      
      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      mainFrame.getRootPane().setDefaultButton(input);
      
      buttonPanel.add(input);
      buttonPanel.add(compute);
      buttonPanel.add(graph);
      buttonPanel.add(reset);
      buttonPanel.add(save);
      buttonPanel.add(load);
      
      mainPanel.add(buttonPanel, BorderLayout.WEST);
      mainPanel.add(listPane, BorderLayout.CENTER);
      mainFrame.add(mainPanel);
      mainFrame.pack();
      mainFrame.setLocationRelativeTo(null);
      mainFrame.setVisible(true);
   }
   
   public void output(String str)
   {
      mdl.addElement(str);
      outputList.ensureIndexIsVisible(mdl.getSize() - 1);
   }
   
   public void clearList()
   {
      mdl.removeAllElements();
   }
   
   public static void main(String args[])
   {
      new Statistics();
   } 
}

class ClickListener implements ActionListener
{
   private String indicator;
   private Statistics stats;
   private int killSum = 0;
   private int deathSum = 0;
   private int assistSum = 0;
   private int count = 0;
   private double kda[] = new double[10];
   private int kill[] = new int[10];
   private int death[] = new int[10];
   private int assist[] = new int[10];
   private double sortKDA[] = new double[10];
   private int sortKill[] = new int[10];
   private int sortDeath[] = new int[10];
   private int sortAssist[] = new int[10];
   private double killMedian = 0;
   private double deathMedian = 0;
   private double assistMedian = 0;
   private double kdaMedian = 0;
   
   public ClickListener(Statistics stats)
   {
      this.stats = stats;
   }
      
   public void actionPerformed(ActionEvent e)
   {
      Scanner input = new Scanner(System.in);
      JFrame frame = new JFrame("Graph");
      if ("input".equals(e.getActionCommand()))
      {
         JSpinner kills = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
         JSpinner deaths = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
         JSpinner assists = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
         
         JButton btnOk = new JButton("Ok");
         JButton btnCncl = new JButton("Cancel");
         
         JLabel killLabel = new JLabel("Kills");
         JLabel deathLabel = new JLabel("Death");
         JLabel assistLabel = new JLabel("Assist");
         
         JPanel inputPanel = new JPanel(new GridLayout(4, 2));
         JDialog inputDialog = new JDialog();
         
         inputPanel.add(killLabel);
         inputPanel.add(kills);
         inputPanel.add(deathLabel);
         inputPanel.add(deaths);
         inputPanel.add(assistLabel);
         inputPanel.add(assists);
         inputPanel.add(btnOk);
         inputDialog.getRootPane().setDefaultButton(btnOk);
         
         btnOk.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               stats.output("Your KDA is " + (Integer) kills.getValue() + "/" + (Integer) deaths.getValue() + "/" + (Integer) assists.getValue());
               killSum += (Integer) kills.getValue();
               deathSum += (Integer) deaths.getValue();
               assistSum += (Integer) assists.getValue();
               
               int killNum = (Integer)kills.getValue();
               int assistNum = (Integer)assists.getValue();
               int deathNum = (Integer)deaths.getValue();
               
               kda[count] = (killNum + assistNum) / (double)deathNum;
               kill[count] = killNum;
               assist[count] = assistNum;
               death[count] = deathNum;
               
               sortKDA[count] = (killNum + assistNum) / (double)deathNum;
               sortKill[count] = killNum;
               sortDeath[count] = deathNum;
               sortAssist[count] = assistNum;
               
               count += 1;
               inputDialog.setVisible(false);
            }
         });
         
         inputPanel.add(btnCncl);
         
         btnCncl.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               inputDialog.setVisible(false);
            }
         });
         
         inputDialog.add(inputPanel);
         inputDialog.pack();
         inputDialog.setVisible(true);
      }   
      
      else if ("compute".equals(e.getActionCommand()))
      {
         if (count != 0)
         {
            DecimalFormat decForm = new DecimalFormat("0.0");
            double aveKills = killSum / (double) count;
            double aveDeaths = deathSum / (double) count;
            double aveAssists = assistSum / (double) count;
            
            sort(sortKill, 0, count - 1);
            sort(sortDeath, 0, count - 1);
            sort(sortAssist, 0, count - 1);
            sortDouble(sortKDA, 0, count - 1);
            
            stats.output("Your KDA in " + count + " game(s) is " + decForm.format(aveKills) + "/" + decForm.format(aveDeaths) + "/" + decForm.format(aveAssists));
            if ((count - 1) % 2 == 0)
            {
               int divi = (count - 1) / 2;
               killMedian = sortKill[divi];
               deathMedian = sortDeath[divi];
               assistMedian = sortAssist[divi];
               kdaMedian = sortKDA[divi];
               
               stats.output("");
               stats.output("Kill Median: " + decForm.format(killMedian));
               stats.output("Death Median: " + decForm.format(deathMedian));
               stats.output("Assist Median: " + decForm.format(assistMedian));
               stats.output("KDA Median: " + decForm.format(kdaMedian));
            }
            
            else
            {
               double divi = (double) (count - 1) / 2;
               int upper = (int) Math.ceil(divi);
               int lower = (int) Math.floor(divi);
               
               killMedian = (sortKill[upper] + sortKill[lower]) / (double) 2;
               deathMedian = (sortDeath[upper] + sortDeath[lower])  / (double) 2;
               assistMedian = (sortAssist[upper] + sortAssist[lower]) /(double) 2;
               kdaMedian = (sortKDA[upper] + sortAssist[lower]) / (double) 2;
               
               stats.output("Kill Median: " + decForm.format(killMedian));
               stats.output("Death Median: " + decForm.format(deathMedian));
               stats.output("Assist Median: " + decForm.format(assistMedian));
               stats.output("KDA Median: " + decForm.format(kdaMedian));
            }
         }
         
         else
            JOptionPane.showMessageDialog(null, "Please enter data.");
      }
      
      
      else if ("graph".equals(e.getActionCommand()))
      {
         JFrame askFrame = new JFrame("Graph Frame");
         JPanel mainPanel = new JPanel(new BorderLayout());
         JPanel labelPanel = new JPanel();
         JPanel askPanel = new JPanel();
         
         JLabel askLbl = new JLabel();
         askLbl.setText("Select the type of graph");
         
         JButton btnKDA = new JButton("KDA");
         JButton btnKills = new JButton("Kills");
         JButton btnDeaths = new JButton("Deaths");
         JButton btnAssists = new JButton("Assists");
         
         labelPanel.add(askLbl);
         askPanel.add(btnKDA);
         btnKDA.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               indicator = "KDA";
               askFrame.setVisible(false);
               createGraph(frame);
            }
         });
         
         askPanel.add(btnKills);
         
         btnKills.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               indicator = "kills";
               askFrame.setVisible(false);
               createGraph(frame);
            }
         });
         
         askPanel.add(btnDeaths);
         
         btnDeaths.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               indicator = "deaths";
               askFrame.setVisible(false);
               createGraph(frame);
            }
         });
         
         askPanel.add(btnAssists);
         
         btnAssists.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent e)
            {
               indicator = "assists";
               askFrame.setVisible(false);
               createGraph(frame);
            }
         });
         
         mainPanel.add(labelPanel, BorderLayout.CENTER);
         mainPanel.add(askPanel, BorderLayout.SOUTH);
         
         askFrame.add(mainPanel);
         askFrame.pack();
         askFrame.setVisible(true);
      }
      
      else if ("reset".equals(e.getActionCommand()))
      {
         frame.removeAll();
         reset();   
      }
      
      else if ("save".equals(e.getActionCommand()))
      {
         createFile();
      }
      
      else if ("load".equals(e.getActionCommand()))
      {
         readFile();
      }
      
      else if ("color".equals(e.getActionCommand()))
      {
         JFrame colorFrame = new JFrame("Color Frame");
         JPanel mainPanel = new JPanel(new BorderLayout());
         JPanel buttonPanel = new JPanel();
         JPanel lblPanel = new JPanel();
         JLabel colorLbl = new JLabel();
         JButton backColor = new JButton("Change Background");
         JButton btnColor = new JButton("Change Button");
         JButton textColor = new JButton("Change Text");
         
           
      }  
   }
   
   public void draw(Graphics g)
   {  
      int pointCount = 0;
      int increment = 0;
      int xCoord[] = new int[10];
      int yCoord[] = new int[10];
      setup(g);
      
      for (int k = 0; k <= kda.length - 1; k++)
      {
         double point = 0;
         if (indicator == "KDA")
         {
            point = kda[k];
            g.setColor(Color.yellow);
         }
         else if (indicator == "kills")
         {
            point = kill[k];
            g.setColor(Color.red);
         }
         else if (indicator == "deaths")
         {
            point = death[k];
            g.setColor(Color.blue);
         }
         else if (indicator == "assists")
         {
            point = assist[k];
            g.setColor(Color.green);
         }   
            
         if (point != 0)
         {
            g.fillOval(increment + 95,(int) (695 - point * 50 / 3), 10, 10);
            xCoord[k] = increment + 100;
            yCoord[k] = (int) (700 - point * 50 / 3);
         }   
         increment += 50;
         pointCount += 1;
      }
      
      if (pointCount >= 0)
      {
         Graphics2D g2 = (Graphics2D)g;
         g2.setStroke(new BasicStroke(5));
         for (int k = 0; k <= pointCount - 2; k++)
         {
            if ((xCoord[k] != 0) && (xCoord[k+1] != 0)) 
               g2.drawLine(xCoord[k], yCoord[k], xCoord[k+1], yCoord[k+1]);
         }
      }   
   }
   
   public void setup(Graphics g)
   {
      int count = 0;
      int yAxis = 0;
      for (int k = 0; k <= 700; k +=50)
      {
         count += 1;
         g.drawLine(k + 100, 700, k + 100, 0);      // Vertical
         g.drawLine(100, k, 800, k);                // Horizontal 
         g.drawString("" + count, k + 98, 713);
         g.drawString("" + yAxis, 85, 700 - k);
         yAxis += 3;
      }
      g.drawString("KDA", 30, 400);
      g.drawString("Game", 400, 750);
   } 
   
   public void createGraph(JFrame frame)
   {
      JPanel graphPanel = new JPanel(){
         @Override
         public void paintComponent(Graphics g)
         {
            super.paintComponent(g);
            draw(g);              
         }
      };
      graphPanel.setPreferredSize(new Dimension(800, 800));
      frame.add(graphPanel);
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
   }
   
   public void createFile()
   {
      try
      {
         String name = JOptionPane.showInputDialog(null, "Enter the name of the file.");
         FileWriter outFile = new FileWriter(name + ".dat");
         BufferedWriter outStream = new BufferedWriter(outFile);
         for (int k = 0; k < kill.length - 1; k++)
         {
            if (kill[k] != 0)
            {
               outStream.write("" + kill[k]);
               outStream.newLine();
               outStream.write("" + death[k]);
               outStream.newLine();
               outStream.write("" + assist[k]);
               outStream.newLine();
            }
         }
         outStream.close();
      } catch(IOException ex)
      {
         System.err.println("Error reading file: " + ex.getMessage());
      }
   }
   
   public void readFile()
   {
      try
      {
         String s1, s2, s3;
         reset();
         JFileChooser fc = new JFileChooser();
         fc.showOpenDialog(null);
         File file = fc.getSelectedFile();
         if (file == null)
            System.exit(0);
         FileReader inFile = new FileReader(file.getName());   
         BufferedReader inStream = new BufferedReader(inFile);
         while ((s1 = inStream.readLine()) != null && (s2 = inStream.readLine()) != null && (s3 = inStream.readLine()) != null)
         {
            stats.output("Your KDA is " + s1 + "/" + s2 + "/" + s3);
            kill[count] = Integer.parseInt(s1);
            death[count] = Integer.parseInt(s2);
            assist[count] = Integer.parseInt(s3);
            kda[count] = (kill[count] + assist[count]) / (double) death[count];
            sortKill[count] = Integer.parseInt(s1);
            sortDeath[count] = Integer.parseInt(s2);
            sortAssist[count] = Integer.parseInt(s3);
            sortKDA[count] = (kill[count] + assist[count]) / (double) death[count];
            killSum += Integer.parseInt(s1);
            deathSum += Integer.parseInt(s2);
            assistSum += Integer.parseInt(s3);
            count += 1;
            
         } 
      } catch(IOException ex)
      {
         System.err.println("Error reading file: " + ex.getMessage());
      }
   }
   
   public void reset()
   {
       killSum = 0;
       deathSum = 0;
       assistSum = 0;
       count = 0;
       stats.clearList();
       indicator = "";
       for (int k = 0; k < kda.length - 1; k++)
       {
         kda[k] = 0;
         kill[k] = 0;
         death[k] = 0;
         assist[k] = 0;
         sortKDA[k] = 0;
         sortKill[k] = 0;
         sortDeath[k] = 0;
         sortAssist[k] = 0;
       }
   }
   
   public void sort(int[] list, int first, int last)
   {
      int k = first;
      int r = last;
      int mid = list[first + (last - first) / 2];
      
      while (k <= r)
      {
         while (list[k] < mid)
         {
            k++;
         }
         
         while (list[r] > mid)
         {
            r--;
         }
         
         if (k <= r)
         {
            int temp = list[r];
            list[r] = list[k];
            list[k] = temp;
            k++;
            r--;
         }
      }
      
      if (first < r)
         sort(list, first, r);
      if (k < last)
         sort(list, k, last);
   }
   
   public void sortDouble(double[] list, int first, int last)
   {
      int k = first;
      int r = last;
      double mid = list[first + (last - first) / 2];
      
      while (k <= r)
      {
         while (list[k] < mid)
         {
            k++;
         }
         
         while (list[r] > mid)
         {
            r--;
         }
         
         if (k <= r)
         {
            double temp = list[r];
            list[r] = list[k];
            list[k] = temp;
            k++;
            r--;
         }
      }
      
      if (first < r)
         sortDouble(list, first, r);
      if (k < last)
         sortDouble(list, k, last);
   }
}
