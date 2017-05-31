
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wblachowski
 */
 class Simulation extends Thread{
     int h,w;
     String style;
     
     Simulation(int he){
             h=he;
             w=he;
     }
    public void run(){
        int x,y,mx=0,my=0;
        boolean move;
     while(MainFrame.checkmoves()){
    
        move=false; 
        Random rand=new Random();
         y=rand.nextInt(h);
         x=rand.nextInt(w);
         int i=y*h+x;
        while(MainFrame.blocksv[y][x]!=1 || !move){
              i++;
              y=i/h;
              x=i%h;
              y=y%h;
              if(MainFrame.blocksv[y][x]==1 && x>1 && y>1){
                if(MainFrame.blocksv[y-1][x]==1 && MainFrame.blocksv[y-2][x]==0){move=true;my=y-2;mx=x;}
                if(MainFrame.blocksv[y+1][x]==1 && MainFrame.blocksv[y+2][x]==0){move=true;my=y+2;mx=x;}
                if(MainFrame.blocksv[y][x-1]==1 && MainFrame.blocksv[y][x-2]==0){move=true;my=y;mx=x-2;}
                if(MainFrame.blocksv[y][x+1]==1 && MainFrame.blocksv[y][x+2]==0){move=true;my=y;mx=x+2;}
              }
        }
        MainFrame.blocks[y][x].setIcon(MainFrame.red);
        
         try {
             Thread.sleep(500);
         } catch (InterruptedException ex) {
             Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
         }
        MainFrame.blocks[my][mx].setIcon(MainFrame.white);
        MainFrame.blocksv[my][mx]=1;
        MainFrame.blocks[y][x].setIcon(MainFrame.black);
        MainFrame.blocksv[y][x]=0;
        MainFrame.blocks[(y+my)/2][(x+mx)/2].setIcon(MainFrame.black);
        MainFrame.blocksv[(y+my)/2][(x+mx)/2]=0;
        MainFrame.nofblocks--;
        MainFrame.jLabelPionki1.setText(Integer.toString(MainFrame.nofblocks));
        try {
             Thread.sleep(500);
         } catch (InterruptedException ex) {
             Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
         }
     }
     MainFrame.endofsimulation_message();
    }
    
 

}
