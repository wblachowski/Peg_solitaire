/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wblachowski
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Thread.State.RUNNABLE;
import javax.swing.JOptionPane;
public class MainFrame extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    static int hei,wid;
    static boolean endofgame=false;
    static boolean clicked_any=false;
    static Timer timer;
    Simulation symulacja;
    long stop;
    static int nofblocks;
    static ImageIcon red,white,black;
    static Image red_a,white_a,black_a;
    int lasti,lastj;
    static JButton[][] blocks;
    static int[][] blocksv;
    ActionListener listen=new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean found=false;
            int i=0,j = 0;
            //FINDING SOURCE OF THE CLICK
            for(i=2;i<hei+2;i++){
                for(j=2;j<wid+2;j++){
                    if(e.getSource()==blocks[i][j]){found=true;break;}
                }
                if(found)break;
            }
           
            if(!endofgame)buttonclicked(i,j);
            if(lasti==0 && lastj==0 && !endofgame){
                if(!checkmoves())endofgame_message();
            
            }
        }
    };
      
 
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
  
        hei=9;
        wid=9;
        blocks = new JButton[29][29];
        blocksv=new int[29][29];
        endofgame=false;
        setVisible(true);
        initComponents();
        timer=new Timer();
        symulacja = new Simulation(hei);
        timer.start();
        makeFields(hei,wid);
    }
    public void makeFields(int h, int w){
          hei=h;wid=w;
          
          stop=get_stop_time();
          timer.continue_counting(jCheckBox1.isSelected(),stop);
          jLabelTime.setText("00:00:00");
          clicked_any=false;
          endofgame=false;
          setImage(h,w);
          lasti=0;
          lastj=0;
          String style=ComboBoxStyle.getSelectedItem().toString();
          nofblocks=(h/3*h/3)+4*(h/3)*(h/3-1)-1;
          if(style=="europejska")nofblocks+=2*(1+h/3-2)*(h/3-2);
          jLabelPionki1.setText(Integer.toString(nofblocks));
          for(int i=0;i<h+2;i++){
            for(int j=0;j<w+2;j++){
                if(i<2){blocksv[i][j]=-1;continue;}
                if(i>h-1){blocksv[i][j]=-1;continue;}
                if(j<2){blocksv[i][j]=-1;continue;}
                if(j>w-1){blocksv[i][j]=-1;continue;}
                if(style=="angielska"){
                    if(i<=h/3 && j<=w/3){blocksv[i][j]=-1;continue;}
                    if(i>2*h/3 && j<=w/3){blocksv[i][j]=-1;continue;}
                    if(i<=h/3 && j>2*w/3){blocksv[i][j]=-1;continue;}
                    if(i>2*h/3 && j>2*w/3){blocksv[i][j]=-1;continue;}
                }
                if(style=="europejska"){
                    if(i-1+j-1<=h/3){blocksv[i][j]=-1;continue;}
                    if(j-1+i-1>=2*(h-1)-h/3){blocksv[i][j]=-1;continue;}
                    if(h-1-(i-2)+j-2<=h/3){blocksv[i][j]=-1;continue;}
                    if(h-1-(j-2)+i-2<=h/3){blocksv[i][j]=-1;continue;}
                
                
                }
                blocks[i][j]=new JButton();
                blocks[i][j].setContentAreaFilled(false);
                blocks[i][j].setBorder(null);
                blocks[i][j].addActionListener(listen);
                blocks[i][j].setFocusPainted(false);
                if((i-1)==h/2 && (j-1)==w/2){
                     blocksv[i][j]=0;
                     blocks[i][j].setIcon(black);
                }else{
                    blocksv[i][j]=1;
                    blocks[i][j].setIcon(white);
                }
                blocks[i][j].setSize(jPanel1.getWidth()/w,jPanel1.getHeight()/h);
                jPanel1.add(blocks[i][j]);
                blocks[i][j].setLocation((j-1)*jPanel1.getWidth()/w,(i-1)*jPanel1.getHeight()/h);
            }

        }
        
    }
    public void setImage(int h,int w){
        URL url;
        if(jRadioButton1.isSelected()){url = Main.class.getResource("images/red3.png");}
        else if(jRadioButton2.isSelected()){url = Main.class.getResource("images/red.png");}
        else{url = Main.class.getResource("images/red2.png");};
        red= new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(jPanel1.getWidth()/w, jPanel1.getHeight()/h, Image.SCALE_SMOOTH));
        red_a=red.getImage();
        if(jRadioButton1.isSelected()){url = Main.class.getResource("images/white3.png");}
        else if(jRadioButton2.isSelected()){url = Main.class.getResource("images/white.png");}
        else{url = Main.class.getResource("images/white2.png");};
        white= new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(jPanel1.getWidth()/w, jPanel1.getHeight()/h, Image.SCALE_SMOOTH));
        white_a=white.getImage();
        url = Main.class.getResource("images/black.png");
        black= new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(jPanel1.getWidth()/w, jPanel1.getHeight()/h, Image.SCALE_SMOOTH));
        black_a=black.getImage();
    }    
    public void buttonclicked(int i,int j){
        if(!clicked_any)timer.continue_counting(jCheckBox1.isSelected(), get_stop_time());
        clicked_any=true;
        
        //pierwsze kliknięcie lub po zbiciu (oczywiscie na białe pole)
        if(lasti==0 && lastj==0 && blocksv[i][j]==1){
            if(blocksv[i][j]!=0){
                blocks[i][j].setIcon(red);
                blocksv[i][j]=2;
                lasti=i;
                lastj=j;
            } 
        
        }else
            //po białym klikniecie na biały
            if(blocksv[lasti][lastj]==2 && blocksv[i][j]==1){
                blocksv[lasti][lastj]=1;
                blocksv[i][j]=2;
                blocks[lasti][lastj].setIcon(white);
                blocks[i][j].setIcon(red);
                lasti=i;
                lastj=j;
            }
        else
            //po czerwonym klikniecie na czarny
            if(blocksv[lasti][lastj]==2 && blocksv[i][j]==0){
                
                if(validchoice(lasti,lastj,i,j)){ 
                    lasti=0;
                    lastj=0;
                } 
            }  
    }
    public boolean validchoice(int lasti,int lastj,int i, int j){    
        if(((i==lasti-2 || i==lasti+2) && lastj==j) || (lasti==i && (j==lastj-2 || j==lastj+2))){
            int di=i,dj=j;  //directions
            if(i==lasti-2){di=lasti-1;}else if(i==lasti+2){di=lasti+1;}
            if(j==lastj-2){dj=lastj-1;}else if(j==lastj+2){dj=lastj+1;}
            blocks[i][j].setIcon(white);
            blocks[di][dj].setIcon(black);
            blocks[lasti][lastj].setIcon(black);
            blocksv[i][j]=1;
            blocksv[di][dj]=0;
            blocksv[lasti][lastj]=0;
            nofblocks--;
            jLabelPionki1.setText(Integer.toString(nofblocks));
            return true;
        }
        return false;
            
    }
    public static boolean checkmoves(){
        for(int i=2;i<hei+1;i++){
            for(int j=2;j<wid+1;j++){
                if(blocksv[i][j]!=1)continue;
                if(blocksv[i-1][j]==1 && blocksv[i-2][j]==0){return true;}
                if(blocksv[i+1][j]==1 && blocksv[i+2][j]==0){return true;}
                if(blocksv[i][j-1]==1 && blocksv[i][j-2]==0){return true;}
                if(blocksv[i][j+1]==1 && blocksv[i][j+2]==0){return true;}
            }
            
        }
        timer.stop_counting();
        return false;
    }
    public static void endofgame_message(){
        endofgame=true;
        jCheckBox1.setSelected(false);
       if(nofblocks>1)JOptionPane.showMessageDialog(jPanel1,"Koniec! Na planszy pozostało "+Integer.toString(nofblocks)+" pionków.\nTwój czas to "+jLabelTime.getText(), "Koniec gry",2);
       if(nofblocks==1)JOptionPane.showMessageDialog(jPanel1,"Brawo! Na planszy został tylko jeden pionek.","Gratulacje!",-1);
    }
    public static void endoftime_message(){
        endofgame=true;
        jCheckBox1.setSelected(false);
        JOptionPane.showMessageDialog(jPanel1,"Zadany czas upłynął! Na planszy pozostało "+Integer.toString(nofblocks)+" pionków", "Koniec czasu",2);
       
    }
    public static void endofsimulation_message(){
        endofgame=true;
        jCheckBox1.setSelected(false);
        JOptionPane.showMessageDialog(jPanel1,"Koniec symulacji. Na planszy pozostało "+Integer.toString(nofblocks)+" pionków", "Koniec symulacji",2);
        
    }
    public long get_stop_time()
    {
        long time = 0;
        long jt1=Long.parseLong(jTextField1.getText());
        long jt2=Long.parseLong(jTextField2.getText());
        long jt3=Long.parseLong(jTextField3.getText());
        if(jt1>59){jTextField1.setText("59");}
        if(jt2>59){jTextField1.setText("59");}
        if(jt3>99){jTextField1.setText("99");}
        if(jt1>0 && jt1<10){jTextField1.setText("0"+jTextField1.getText());}
        if(jt2>0 && jt2<10){jTextField2.setText("0"+jTextField2.getText());}
        if(jt3>0 && jt3<10){jTextField3.setText("0"+jTextField3.getText());}
        time+=Long.parseLong(jTextField1.getText())*60*1000;
        time+=Long.parseLong(jTextField2.getText())*1000;
        time+=Long.parseLong(jTextField3.getText())*10;
        return time;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabelCzas = new javax.swing.JLabel();
        jLabelTime = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        ComboBoxStyle = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        ComboBoxSize = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        exit = new javax.swing.JButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabelColor = new javax.swing.JLabel();
        jRadioButton3 = new javax.swing.JRadioButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabelPionki = new javax.swing.JLabel();
        jLabelPionki1 = new javax.swing.JLabel();
        jButtonSimulation = new javax.swing.JButton();

        jMenu1.setText("jMenu1");

        buttonGroup2.add(jRadioButton1);
        buttonGroup2.add(jRadioButton2);
        buttonGroup2.add(jRadioButton3);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Samotnik");
        setBackground(new java.awt.Color(194, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocation(new java.awt.Point(0, 0));
        setResizable(false);

        jPanel1.setBackground(javax.swing.UIManager.getDefaults().getColor("InternalFrame.activeTitleGradient"));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setPreferredSize(new java.awt.Dimension(500, 500));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 498, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 498, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setMaximumSize(new java.awt.Dimension(100, 500));
        jPanel2.setMinimumSize(new java.awt.Dimension(100, 500));
        jPanel2.setPreferredSize(new java.awt.Dimension(100, 500));

        jLabelCzas.setText("Czas:");

        jLabelTime.setText("0");

        jLabel2.setText("Wersja:");

        ComboBoxStyle.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "angielska", "europejska"}));
        ComboBoxStyle.setFocusable(false);
        ComboBoxStyle.setMinimumSize(new java.awt.Dimension(80, 25));
        ComboBoxStyle.setName(""); // NOI18N
        ComboBoxStyle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxStyleActionPerformed(evt);
            }
        });

        jLabel1.setText("Rozmiar:");

        ComboBoxSize.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"3","5","7"}));
        ComboBoxSize.setFocusable(false);
        ComboBoxSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxSizeActionPerformed(evt);
            }
        });

        jButton1.setText("Nowa gra");
        jButton1.setFocusPainted(false);
        jButton1.setMinimumSize(new java.awt.Dimension(80, 25));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        exit.setText("Wyjdź");
        exit.setFocusPainted(false);
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });

        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Niebieski");
        jRadioButton1.setFocusPainted(false);

        jRadioButton2.setText("Biały");
        jRadioButton2.setToolTipText("");
        jRadioButton2.setFocusPainted(false);
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jLabelColor.setText("Kolor pionków:");

        jRadioButton3.setText("Czarny");

        jCheckBox1.setText("Zakończ po czasie:");
        jCheckBox1.setFocusPainted(false);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jTextField1.setText("00");

        jLabel4.setText(":");
        jLabel4.setToolTipText("");

        jTextField2.setText("00");

        jLabel5.setText(":");
        jLabel5.setToolTipText("");

        jTextField3.setText("00");

        jLabelPionki.setText("Pozostałe pionki:");

        jLabelPionki1.setText("32");

        jButtonSimulation.setText("Symulacja");
        jButtonSimulation.setFocusPainted(false);
        jButtonSimulation.setMinimumSize(new java.awt.Dimension(80, 25));
        jButtonSimulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSimulationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addComponent(exit, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
            .addComponent(jSeparator1)
            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ComboBoxStyle, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelCzas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelPionki))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jRadioButton3)
                            .addComponent(jRadioButton1)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ComboBoxSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabelColor)
                            .addComponent(jRadioButton2)
                            .addComponent(jCheckBox1)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel2)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelTime)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelPionki1))
                    .addComponent(jButtonSimulation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCzas)
                    .addComponent(jLabelPionki))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTime)
                    .addComponent(jLabelPionki1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ComboBoxStyle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ComboBoxSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(9, 9, 9)
                .addComponent(jLabelColor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonSimulation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exit)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(122, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(122, 122, 122))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(60, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        symulacja.stop();
        jPanel1.removeAll();
        jPanel1.revalidate();
        jPanel1.repaint(); 
        String combo =ComboBoxSize.getSelectedItem().toString();
        int r=Integer.parseInt(combo);
        makeFields(r*3,r*3);
        
       
    }//GEN-LAST:event_jButton1ActionPerformed

    private void ComboBoxSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxSizeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ComboBoxSizeActionPerformed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        // TODO add your handling code here:
       System.exit(0);
    }//GEN-LAST:event_exitActionPerformed

    private void ComboBoxStyleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxStyleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ComboBoxStyleActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButtonSimulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSimulationActionPerformed
        symulacja.stop();  
        jPanel1.removeAll();
        jPanel1.revalidate();
        jPanel1.repaint(); 
        makeFields(hei,wid);
        symulacja=new Simulation(hei);
        endofgame=true;
        symulacja.start();
    }//GEN-LAST:event_jButtonSimulationActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ComboBoxSize;
    private javax.swing.JComboBox<String> ComboBoxStyle;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton exit;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonSimulation;
    private static javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelColor;
    private javax.swing.JLabel jLabelCzas;
    private javax.swing.JLabel jLabelPionki;
    public static javax.swing.JLabel jLabelPionki1;
    public static javax.swing.JLabel jLabelTime;
    private javax.swing.JMenu jMenu1;
    public static javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
