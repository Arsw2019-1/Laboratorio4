package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Immortal extends Thread {

    private ImmortalUpdateReportCallback updateCallback = null;
    private int myIndex;
    private int health;
    private AtomicInteger contenedor;
    private int defaultDamageValue;
    private boolean execute = true;
    private final List<Immortal> immortalsPopulation;
    private boolean death = false;
    private final String name;
    boolean fi=false;
    private final Random r = new Random(System.currentTimeMillis());
    private boolean pause = false;

    public Immortal(String name, List<Immortal> immortalsPopulation, int health, int defaultDamageValue, ImmortalUpdateReportCallback ucb, AtomicInteger contenedor) {
        super(name);
        this.updateCallback = ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = health;
        this.defaultDamageValue = defaultDamageValue;
        this.contenedor = contenedor;
        myIndex = immortalsPopulation.indexOf(this);
    }

    public void setExecute(boolean execute) {
        this.execute = execute;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public boolean getPause() {
        return pause;
    }

    public void pause() {
        pause = true;
    }

    public void contin() {
        pause = false;
    }

    public void run() {

        if(immortalsPopulation.size()==1){
            fin();
        }
        
        while (execute&&!fi) {
            try {
                if (pause) {
                    synchronized (immortalsPopulation) {
                        contenedor.incrementAndGet();
                        immortalsPopulation.wait();
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Immortal.class.getName()).log(Level.SEVERE, null, ex);

            }
            Immortal im;

            myIndex = immortalsPopulation.indexOf(this);

            int nextFighterIndex = r.nextInt(immortalsPopulation.size());

            //avoid self-fight
            if (nextFighterIndex == myIndex) {
                nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
            }

            im = immortalsPopulation.get(nextFighterIndex);

            this.fight(im);
            try {
                deadIm();
            } catch (InterruptedException ex) {
                Logger.getLogger(Immortal.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public void deadIm() throws InterruptedException {
        //for (int i =0; i<immortalsPopulation.size();i++) {
        int i = 0;
        int tamaño = immortalsPopulation.size();
        while (i< tamaño&& tamaño>1) {
            
            System.out.println("i es :"+i+"  tamaño :"+tamaño);
            System.out.println("que sale "+(i< tamaño));
            //tamaño = immortalsPopulation.size();
            Immortal tem = immortalsPopulation.get(i);
            //System.out.println("El inmortal es: "+tem);
            //System.out.println("que es i "+i+"quees tam"+tamaño);
            //System.out.println("Cual es el indice del inmortal "+immortalsPopulation.indexOf(tem));
            if (tem.health == 0 ) {
                //synchronized (immortalsPopulation) {
                    //Thread.sleep(1000);
//                    immortalsPopulation.indexOf(i);
                    immortalsPopulation.remove(immortalsPopulation.indexOf(tem));
                    System.out.println("Sacando");
                    //immortalsPopulation.remove(temp);
                    updateCallback.processReport( "sacando");
                    tamaño--;
                    i--;
                    //tem.suspend();
                //}
            } 

            System.out.println("valor antes de i "+i);

            i=i+1;
            System.out.println("valor despues de  de i "+i);
            System.out.println("i :"+i+"tamaño"+tamaño);
            System.out.println("Tamaño del arreglo de inmortales : "+immortalsPopulation.size());
            if(tamaño==1){
                fin();
                System.out.println("Rompiendo");
                break;
            
            }
        }
    }

    public void fight(Immortal i2) {
        Immortal firts = i2;
        Immortal second = this;
        if (myIndex < i2.myIndex) {
            firts = this;
            second = i2;
        }
        
        //if(firts!=second){
        synchronized (firts) {
            if((firts!=second)){
            synchronized (second) {
                if (i2.getHealth() > 0 && second.getHealth() > 0) {
                    //if (i2.getHealth() > 0) {                    
                    i2.changeHealth(i2.getHealth() - defaultDamageValue);
                    this.health += defaultDamageValue;
                    updateCallback.processReport("Fight: " + this + " vs " + i2 + "\n");
                } else {
                    death = true;
                    //immortalsPopulation.remove(i2);
                    updateCallback.processReport(this + " says:" + i2 + " is already dead!\n");

                }
            }
            }
        }//}
    }

    public void changeHealth(int v) {
        health = v;
    }

    public boolean getDeat() {
        return death;

    }

    public int getHealth() {
        return health;
    }

    @Override
    public String toString() {

        return name + "[" + health + "]";
    }
    public void fin(){
        fi=true;
    }

}
