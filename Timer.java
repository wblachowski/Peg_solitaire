
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

class Timer extends Thread {
         long s;
         long stop;
         boolean count;
         Timer() {
             count=true;
         }
         public String seconds10(long s){
             s=s/10;
             s=s%100;
             return Long.toString(s/10)+Long.toString(s%10);  
         }
         public String seconds(long s){
             s=s/1000;
             s=s%60;
             return Long.toString(s/10)+Long.toString(s%10)+":";  
         }
         public String minutes(long s){
             s=s/1000;
             s=s/3600;
             return Long.toString(s/10)+Long.toString(s%10)+":";  
         }
         public void stop_counting(){
             count=false;
             
         }
         public void continue_counting(boolean halt,long time){
             s=System.currentTimeMillis();
             if(halt){stop=time;}else{stop=-1;}
             
             
             count=true;  
         }
         
         public void run() {
             long czas;
             while(true){
                while(count && MainFrame.clicked_any){
                    czas=System.currentTimeMillis()-s;
                    //System.out.println(czas);
                    MainFrame.jLabelTime.setText(minutes(czas)+seconds(czas)+seconds10(czas));
                    if(stop>=0 && czas>=stop){
                        MainFrame.jLabelTime.setText(minutes(stop)+seconds(stop)+seconds10(stop));
                       
                        count=false;
                         MainFrame.endoftime_message();
                        
                        break;
                    }
                }
                 
                 try {
                     Thread.sleep(100);
                 } catch (InterruptedException ex) {
                     Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }
         }
     }
         
 