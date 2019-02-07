/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartProduction {
    private static Contenedor contenedor;
    public static void main(String[] args) {
        contenedor=new Contenedor();
        Queue<Integer> queue = new LinkedBlockingQueue<>();

        //while (true) {
        //queue.
        System.out.println("Inicio");
        new Producer(contenedor, Long.MAX_VALUE).start();

        //let the producer create products for 5 seconds (stock).
        try {
            System.out.println("FIN1");
            Thread.sleep(3000);

        } catch (InterruptedException ex) {
            Logger.getLogger(StartProduction.class.getName()).log(Level.SEVERE, null, ex);
        }

        new Consumer(contenedor).start();
        try {
            System.out.println("FIN2");
            Thread.sleep(1000);

        } catch (InterruptedException ex) {
            Logger.getLogger(StartProduction.class.getName()).log(Level.SEVERE, null, ex);
            // }
        }
    }

}
