/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class Producer extends Thread {

    private  LinkedBlockingQueue queue = null;

    private int dataSeed = 0;
    private Random rand=null;
    private final long stockLimit;

    public Producer(LinkedBlockingQueue queue,long stockLimit) {
        this.queue = queue;
        rand = new Random(System.currentTimeMillis());
        this.stockLimit=stockLimit;
    }
    @Override
    public synchronized  void run() {
        while (queue.remainingCapacity()>1 ) {
            
            dataSeed = dataSeed + rand.nextInt(100);
            System.out.println("Producer added " + dataSeed);
            queue.add(dataSeed);
            //queue.add(dataSeed);
            //queue.size()<10
            /**
            if(queue.size()-1<queue.){
                System.out.println("Entramos");
                queue.add(dataSeed);
            }else{
                System.out.println("Salimos");
                interrupt();
            }
  **/
            try {
                Thread.sleep(1000);
                if(queue.remainingCapacity()==0 ){
                    queue.wait();
                }
                
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
