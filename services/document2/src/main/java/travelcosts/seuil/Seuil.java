package travelcosts.seuil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class Seuil {

    private List<String[]> monSeuil = new LinkedList();

    public Seuil(){
        URL url; // The URL to read
        HttpURLConnection conn; // The actual connection to the web page
        BufferedReader rd; // Used to read results from the web page
        String line; // An individual line of the web page HTML
        String result = ""; // A long string containing all the HTML
        try {
            url = new URL("https://www.economie.gouv.fr/dgfip/fichiers_taux_chancellerie/missiontxt");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                String[] parts = line.split("\\s+");
                monSeuil.add(new String[]{parts[0], parts[1],parts[2] , parts[4]});
            }
            rd.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double calculateSeuil(String Pays,String date,String currency){
        double leseuil= 150; //valeur par défaut
        if(monSeuil!=null){
            for(int i=0;i<monSeuil.size();i++){
                String[] seuil = monSeuil.get(i);
                if(seuil[0].equals(Pays)){
                    if(seuil[2].equals(currency)){
                        if(isPossibleDate(date,seuil[1])){
                            return Double.parseDouble(seuil[3])/10000;
                        }
                    }
                }
            }
        }
        return leseuil;
    }


    public boolean isPossibleDate(String dateSpend , String dateSeuil){
        String[] partspend = dateSpend.split("/");
        String[] partseuil = dateSeuil.split("/");
        int anneeSpend = 0;
        int anneeSeuil = 0;
        int moisSpend = 0;
        int moisSeuil= 0;
        int jourSpend = 0;
        int jourSeuil= 0;
        if(partspend.length==3){
            for(int i=0;i<partspend.length;i++){
                anneeSpend = Integer.parseInt(partspend[2]);
                anneeSeuil= Integer.parseInt(partseuil[2]);
                moisSpend = Integer.parseInt(partspend[1]);
                moisSeuil= Integer.parseInt(partseuil[1]);
                jourSpend = Integer.parseInt(partspend[0]);
                jourSeuil= Integer.parseInt(partseuil[0]);
            }
            if(anneeSpend < anneeSeuil){
                return false;
            }else if(anneeSpend == anneeSeuil){
                if(moisSpend < moisSeuil){
                    return false;
                }else if(moisSpend == moisSeuil){
                    if(jourSpend < jourSeuil){
                        return false;
                    }
                    else{
                        return true;
                    }
                }else{
                    return true;
                }
            }else{
                return true;
            }

        }else{
            return false;
        }
    }

    public List<String[]> getMonSeuil() {
        return monSeuil;
    }
}