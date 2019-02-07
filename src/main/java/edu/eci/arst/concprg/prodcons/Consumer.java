/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;
import java.util.Random;

/**
 *
 * @author hcadavid
 */
public class Consumer extends Thread{
    private final Contenedor contenedor;
    private Queue<Integer> queue;
    private Random aleatorio;
    
    
    public Consumer(Contenedor contenedor){
        //this.queue=queue;
        this.contenedor=contenedor;
        aleatorio = new Random();
    }
    
    @Override
    public synchronized void run() {
        while (true) {

            if (!contenedor.getEstado()) {
                //int elem=queue.poll();
                int dato=aleatorio.nextInt(10)+1;
                int t=contenedor.get();
                System.out.println("Consumer consumes "+t); 
                try {
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
            }
            
        }
    }
}
