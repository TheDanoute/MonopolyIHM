/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jeu;
import Ui.CarteUI;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author DanJeux
 */
public class PaquetsCartes {
    private ArrayList<Carte> cartesChances;
    private ArrayList<Carte> cartesChancesPoubelle;
    private ArrayList<Carte> cartesCommunautaires;
    private ArrayList<Carte> cartesCommunautairesPoubelle;
    private Monopoly monop;
    
    public PaquetsCartes(String dataFilename, Monopoly monop) {
        cartesChances = new ArrayList<>();
        cartesChancesPoubelle = new ArrayList<>();
        cartesCommunautaires = new ArrayList<>();
        cartesCommunautairesPoubelle = new ArrayList<>();
        this.monop=monop;
        this.initPaquetsCartes(dataFilename);
        // Mélange des listes de cartes
        Collections.shuffle(cartesChances);
        Collections.shuffle(cartesCommunautaires);
    }
    
    private void initPaquetsCartes(String dataFilename) {
        try{
            ArrayList<String[]> data = readDataFile(dataFilename, ",");

            for(int i=0; i<data.size(); ++i){
                String caseType = data.get(i)[1];
                switch (caseType) {
                    case "A":
                        {
                            CarteArgent c = new CarteArgent(data.get(i)[0],data.get(i)[2],Integer.valueOf(data.get(i)[3]),monop);
                            this.addCarte(c);
                            break;
                        }
                    case "M":
                        {
                            CarteMouvement c = new CarteMouvement(data.get(i)[0],data.get(i)[2],Integer.valueOf(data.get(i)[3]),false,monop);
                            this.addCarte(c);
                            break;
                        }
                    case "MA":
                        {
                            CarteMouvement c = new CarteMouvement(data.get(i)[0],data.get(i)[2],Integer.valueOf(data.get(i)[3]),true,monop);
                            this.addCarte(c);
                            break;
                        }
                    case "P":
                        {
                            CartePrison c = new CartePrison(data.get(i)[0],data.get(i)[2],monop);
                            this.addCarte(c);
                            break;
                        }
                    case "S":
                        {
                            CarteSpecial c = new CarteSpecial(data.get(i)[0],data.get(i)[3],Integer.valueOf(data.get(i)[2]),monop);
                            this.addCarte(c);
                            break;
                        }
                    default:
                        monop.gettUi().errorFile(0);
                        break;
                }
                
	    }
        } catch(FileNotFoundException e){
            monop.gettUi().errorFile(1);
	} catch(IOException e){
            monop.gettUi().errorFile(2);
	}
    }
    
    private void addCarte(Carte c){
        if(c.getType()==CarteType.communautaire){
            cartesCommunautaires.add(c);
        } else {
            cartesChances.add(c);
        }
    }
    private ArrayList<String[]> readDataFile(String filename, String token) throws FileNotFoundException, IOException
	{
		ArrayList<String[]> data = new ArrayList<>();
		
		BufferedReader reader  = new BufferedReader(new FileReader("src/Data/"+filename));
		String line = null;
		while((line = reader.readLine()) != null){
			data.add(line.split(token));
		}
		reader.close();
		
		return data;
	}
    
    public Carte piochezCarteChance() {
        // Si la liste cartesChances est vide, cette dernière recupère la liste cartesChancesPoubelle puis elle est mélangée. 
        // La liste poubelle est ensuite vidée. 
        if(cartesChances.isEmpty()) {
            cartesChances = cartesChancesPoubelle;
            Collections.shuffle(cartesChances);
            cartesChancesPoubelle.clear();
        }
        Carte c = cartesChances.get(0);
        cartesChances.remove(0);
        return c;
    }
    
    public Carte piochezCarteCommunautaire() {
        // Si la liste cartesCommunautaires est vide, cette dernière recupère la liste cartesCommunautairesPoubelle puis elle est mélangée. 
        // La liste poubelle est ensuite vidée.
        if(cartesCommunautaires.isEmpty()) {
            cartesCommunautaires = cartesCommunautairesPoubelle;
            Collections.shuffle(cartesCommunautaires);
            cartesCommunautairesPoubelle.clear();
        }
        Carte c = cartesCommunautaires.get(0);
        cartesCommunautaires.remove(0);
        return c;
    }
    
    public void retourCarte(Carte c){
        // Permet de mettre une carte dans la liste poubelle, si c'est une carte chance, dans la liste carteChancesPoubelle; sinon
        // dans la liste carteCommunautairesPoubelle.
        if (c.getType()==CarteType.chance) {
            cartesChancesPoubelle.add(c);
        } else {
            cartesCommunautairesPoubelle.add(c);
        }
    }


}
