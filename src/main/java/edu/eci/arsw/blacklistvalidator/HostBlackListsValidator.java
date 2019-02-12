/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT=5;
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress, int N) throws InterruptedException{
        
        LinkedList<Block> segmentacion=new LinkedList<Block>();
        LinkedList<Integer> blackListOcurrences=new LinkedList<>();
        int ocurrencesCount=0;
        int hilos=0;
        Block hiloTemp;
        int serverRegistration=0;
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
        int numServ=skds.getRegisteredServersCount();
        int checkedListsCount=0;
        int rango=0;
        int contador=0;
        int i=0;
        Boolean cod=true;
        if(N%2==0){
            rango=numServ/N;
            while(i<N & cod){
                Block t= (new Block (contador,rango*(i+1), ipaddress,N));
                t.start();
                t.join();
                segmentacion.add(t);
                contador+=rango;
                ocurrencesCount+=t.ocurrencesCount;
                if(ocurrencesCount>=5){//Con eto se cuenta general para detener a todos
                    checkedListsCount=t.checkedListsCount;
                    serverRegistration=t.registeredServersCount;
                    t.stop();
                    cod=false;
                }
                i++;
            }
            
            if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
                skds.reportAsNotTrustworthy(ipaddress);
            }
            else{
                skds.reportAsTrustworthy(ipaddress);
            }                
            
            LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, serverRegistration});                    }
        else{
        }
        return blackListOcurrences;
    }

    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
      
}
