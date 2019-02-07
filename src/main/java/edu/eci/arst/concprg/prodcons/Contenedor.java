/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author 2098325
 */
public class Contenedor {
    
    
    Queue<Integer> queue = new LinkedBlockingQueue<>();

    private final int capacidad = 6000;
    private int contenido = queue.size();
    private boolean conteLlneo = Boolean.FALSE;

    //MEtodo para extraer Contenido del contenedor
    public synchronized int get() {
        
        
        while (queue.isEmpty()) {
            try {
                //Pausamos el hilo de ejecucion ya que no se puede extraer
                wait();
            } catch (InterruptedException e) {
                System.err.println("Contenedor: Error en get -> " + e.getMessage());
            }

        }
     
        int value=0;
        //System.out.println("Cuanto daremos"+value);
        System.out.println("tAMAÑO DEL QUE"+queue.size());
        //if (queue.size() < value) {
        if(!queue.isEmpty()){
             
            value = queue.size();
            System.out.println("Cuanto daremos"+value);
            contenido -= value;
            //queue.clear();
        } 
        System.out.println("Dando");
        System.out.println("CANTIDAD EN CONTENEDOR: " + contenido);
        conteLlneo = Boolean.TRUE;

        notify();
        return value;
    }

    //Metodo para colocar mas contenido en el contenedor
    public synchronized void put(int value) throws InterruptedException {
        while (conteLlneo) {
            try {
                //Pausamos el hilo hasta que haya hueco para colocar
                wait();
            } catch (InterruptedException e) {
                System.err.println("Contenedor: Error en put -> " + e.getMessage());
            }

        }

        if (contenido + value >= capacidad) {
            queue.add(value);
            contenido = capacidad;
            conteLlneo = Boolean.TRUE;
        } else {
            queue.add(value);
            contenido += value;
        }
        System.out.println("CANTIDAD EN CONTENEDOR: " + contenido);
        notify();
    }

    public Boolean getEstado() {
        return conteLlneo;
    }
}
