package Jeu;

import Ui.*;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Monopoly {
	private int nbMaisons = 32;
	private int nbHotels = 12;
        private PaquetsCartes cartes;
	private HashMap<Integer,Carreau> carreaux = new HashMap<>();
	private ArrayList<Joueur> joueurs = new ArrayList<>();
        private ArrayList<Joueur> joueursTemp = new ArrayList<>();
	private Interface interface_2;
        private FenêtreInit init;
        private Plateau plateau;
        private Joueur  jTemp;
        private TexteUI tUi;
        private ProprieteUI pUi;
        private JoueurUI jUi;
        private CarreauUI cUi;
        private int compteurDoubleJoueur;
        private CarteUI caUi;
        private GestionUI gUi;
        
	public Monopoly(String dataFilename){
            compteurDoubleJoueur=0;
            this.jouerUnePartie(dataFilename);
	}

	private void buildGamePlateau(String dataFilename)
	{
		try{
			ArrayList<String[]> data = readDataFile(dataFilename, ",");
			Groupe groupe = new Groupe();
                        String couleur = "";
                        for(int i=0; i<data.size(); ++i){
				String caseType = data.get(i)[0];
				if(caseType.compareTo("P") == 0){
                                        if (!couleur.equals(data.get(i)[3])) {
                                             couleur = data.get(i)[3];
                                             groupe = new Groupe(Integer.valueOf(data.get(i)[11]),couleur);
                                        }
                                        ArrayList<Integer> prix = new ArrayList<>(); //Création de la liste de prix de loyer
                                        for (int j=5;j<11;j++) {
                                            prix.add(Integer.valueOf(data.get(i)[j]));
                                        }
                                        ProprieteAConstruire pAC = new ProprieteAConstruire(Integer.valueOf(data.get(i)[1]),data.get(i)[2],this,Integer.valueOf(data.get(i)[4]),prix,groupe,Integer.valueOf(data.get(i)[11]));
                                        this.addCarreau(pAC);
                                }
                                
				else if(caseType.compareTo("G") == 0){
                                        Gare gare = new Gare (Integer.valueOf(data.get(i)[1]),data.get(i)[2],this,Integer.valueOf(data.get(i)[3]));
                                        this.addCarreau(gare);
                                }
				else if(caseType.compareTo("C") == 0){
                                        Compagnie comp = new Compagnie(Integer.valueOf(data.get(i)[1]),data.get(i)[2],this,Integer.valueOf(data.get(i)[3]));
                                        this.addCarreau(comp);
				}
                                else if(caseType.compareTo("CP") == 0){
                                        CarreauPrison cp = new CarreauPrison(Integer.valueOf(data.get(i)[1]),data.get(i)[2],this);
                                        this.addCarreau(cp);
				}
				else if(caseType.compareTo("CT") == 0){
                                        CarteType t;
                                        if (data.get(i)[2].equals("Chance")) {
                                            t = CarteType.chance;
                                        } else {
                                            t = CarteType.communautaire;
                                        }
                                        CarreauTirage cT = new CarreauTirage(Integer.valueOf(data.get(i)[1]),data.get(i)[2],this,t);
                                        this.addCarreau(cT);
				}
				else if(caseType.compareTo("CA") == 0){
                                        CarreauArgent ca = new CarreauArgent(Integer.valueOf(data.get(i)[1]),data.get(i)[2],this,Integer.valueOf(data.get(i)[3]));
                                        this.addCarreau(ca);
				}
				else if(caseType.compareTo("CM") == 0){
                                        CarreauMouvement cM = new CarreauMouvement(Integer.valueOf(data.get(i)[1]),data.get(i)[2],this);
                                        this.addCarreau(cM);
				}
				else
					tUi.errorFile(0);
			}
			
		} 
		catch(FileNotFoundException e){
                    tUi.errorFile(1);
		}
		catch(IOException e){
                    tUi.errorFile(2);
		}
	}
        
        public TexteUI gettUi() {
            return tUi;
        }

        public CarteUI getCaUi() {
            return caUi;
        }
        
        
        
        public Plateau getPlateau() {
            return plateau;
        }

        public Joueur getjTemp() {
            return jTemp;
        }

        public JoueurUI getjUi() {
            return jUi;
        }

        public CarreauUI getcUi() {
            return cUi;
        }

        public ProprieteUI getpUi() {
            return pUi;
        }

        
        
        private void addCarreau(Carreau c) {
            carreaux.put(c.getNum(), c);
            TexteUI.printInfo(c);
        }
	
	private ArrayList<String[]> readDataFile(String filename, String token) throws FileNotFoundException, IOException
	{
		ArrayList<String[]> data = new ArrayList<String[]>();
		
		BufferedReader reader  = new BufferedReader(new FileReader("src/Data/"+filename));
		String line = null;
		while((line = reader.readLine()) != null){
			data.add(line.split(token));
		}
		reader.close();
		
		return data;
	}

        
        public void initPartieGraph(){
            init = new FenêtreInit(this);
            init.setVisible(true);
            
        }
        

        
        public void ajouterJoueur (String nomJ,Color c) {
            Joueur j = new Joueur(nomJ,c, this);
            joueurs.add(j);
            System.out.println(nomJ+" a été ajouté.");
            System.out.println("Son premier lancé de dés : " + j.getDesDepart());
        }
             
        private ArrayList<Joueur> trieRecursif(ArrayList<Joueur> list) {
            trierListeJoueurs(list);
            int compteur = 0; //Compteur qui bloque la première itération identique
            int i = 0;
            ArrayList<Joueur> listTemp = new ArrayList<>();//Liste des joueurs ayant obtenu le même score
            while(i<list.size()-1) {
                if (list.get(compteur).getDesDepart()!=list.get(i+1).getDesDepart()) {
                    if (i-compteur<1) { //Le compteur augmente si il n'y a pas eu d'itération identique précédemment
                        compteur=i+1;
                    } else {
                        for (int j = compteur;j<=i;j++){ //Boucle des joueurs ayant obtenu le même score
                            int value = lancerDe(); 
                            tUi.trieJoueur(list.get(j), value);
                            list.get(j).setDesDepart(value);
                            listTemp.add(list.get(j));
                        }
                        for (Joueur jou : trieRecursif(listTemp)) { //Tri de la liste temporaire par la même méthode
                            list.set(compteur, jou); //Remplissage pas à pas depuis la première itération (gelé par le compteur)
                            compteur++;         
                        }
                        listTemp.clear();
                    }
                } else {
                }
                i++;
            }
            if (i-compteur>=1) { //Test si la fin de la liste contient des valeurs identiques
                for (int j = compteur;j<=i;j++){
                    int value = lancerDe();
                    tUi.trieJoueur(list.get(j), value);
                    list.get(j).setDesDepart(value);
                    listTemp.add(list.get(j));
                }
                for (Joueur jou : trieRecursif(listTemp)) {
                    list.set(compteur, jou);
                    compteur++;
                }
            }
            return list; //retourne le nouvel ordre de joueurs
        }
                     
        public void trierListeJoueurs(ArrayList<Joueur> listeJoueurs) { //Méthode permetant de trier une liste de joueur par ordre décroissant de leur résultat du lancé de dé
            Collections.sort(listeJoueurs, new Comparator<Joueur>() {
                @Override
                public int compare(Joueur j1, Joueur j2) {
                return Integer.compare(j2.getDesDepart(),j1.getDesDepart());
                }   
            });
        }
        

	public void action(Joueur aJ) {
                aJ.getPositionCourante().action(aJ);
	}

        public void jouerUnePartie(String dataFilename) {
            this.buildGamePlateau(dataFilename);
            this.initPartieGraph();
            cartes = new PaquetsCartes("cartes_"+dataFilename,this);
        }
        public void lancerPlateau(){
            plateau = new Plateau(this);
            plateau.setVisible(true);
        }
        public void lancerUnePartie(){
            caUi = new CarteUI(this);
            tUi= new TexteUI(this);
            pUi = new ProprieteUI(this);
            jUi = new JoueurUI(this);
            cUi = new CarreauUI(this);
            joueurs = this.trieRecursif(joueurs);         
            plateau.messageLog("Ordre des joueurs");
            for (Joueur jou : joueurs) {
                plateau.messageLog(""+ jou.getNomJoueur());
            }
            jTemp=joueurs.get(0);
            plateau.messageLog("*********************************************************");
            plateau.messageLog("Au tour de " + jTemp.getNomJoueur());
            jTemp.addCartePrison();
            jUi.afficheProprietes(jTemp);
            plateau.setValues(jTemp);
            }

  
	/*public void achatPropriete(Joueur aJ, CarreauPropriete cP) {
		if (aJ.getCash()<cP.getPrix()){
                    tUi.message(aJ.getNomJoueur()+" n'a pas assez d'argent pour acheter la propriété");
                }else{
                    tUi.message("Le joueur "+aJ.getNomJoueur()+" a achété la propriété '"+cP.getNom()+"'");
                    cP.setProprietaire(aJ);
                    aJ.removeCash(cP.getPrix());
                    tUi.message(aJ.getNomJoueur()+"a désormais "+aJ.getCash()+" €");
                }
	}*/
        
        /*public void jouerUnCoup(Joueur aJ) {
        jTemp = aJ;
        plateau.messageLog("Vous avez "+aJ.getCash()+"€");
        plateau.messageLog("Vos propriété(s) :");
        for (Compagnie c : aJ.getCompagnies()){
            plateau.messageLog("Propriété : "+c.getNom());
            
        }
        for (Gare g : aJ.getGares()){
            plateau.messageLog("Propriété : "+g.getNom());
            
        }
        for (ProprieteAConstruire p : aJ.getProprietesAConstruire()){
            plateau.messageLog("Propriété : "+p.getNom());
            plateau.messageLog("Groupe : "+p.getGroupe().getCouleur().toString());
            plateau.messageLog("Avec "+p.getImmobilier()+" construction(s)");
        }
       
        
        String rep = plateau.getAction();
        while (10>0) { 
                rep = plateau.getAction();
                switch(rep) {
                case "construire":
                {
                    this.construire(aJ);
                    break;
                }
                case "echanger":
                {
                    this.echanger(aJ);
                    break;
                }
                case "detruire":
                {
                    try {
                        this.detruire(aJ);
                    } catch (Exception e) {
                        plateau.messageLog(e.getMessage());
                    }
                    break;
                }
                case "hypotheque":
                {
                    try {
                        this.hypotheque(aJ,true);
                    } catch (Exception e) {
                        plateau.messageLog(e.getMessage());
                    }
                    break;
                }
                case "lancerDes":
                {
                    this.lancerDes(aJ, s);
                }
                
            }
            rep = tUi.question("Que voulez-vous faire ? (lancerDes/construire/echanger/detruire/hypotheque)");
        }
        
    }*/
              
        public void lancerDes(int s){
            plateau.messageLog("Vous avancer");
            boolean twice = true;
                if (jTemp.getPrison()) {
                    this.action(jTemp);
                    twice = false;
                } else {
                    twice = lancerDesAvancer(jTemp,s);
                    plateau.update();
                    if(twice && compteurDoubleJoueur>1){
                        // Si le joueur fait 3 doubles d'affilé, il va en prison.
                        jTemp.enPrison();
                        plateau.messageLog("Le joueur "+jTemp.getNomJoueur()+" a fait trois double de suite, il est envoyé en prison!");
                        twice = false;
                    }else{
                        this.action(jTemp);
                        twice=false;
                    }
                }
                if(!twice) {
                    joueurs.remove(jTemp);
                    joueurs.add(jTemp);
                    jTemp=joueurs.get(0);
                    compteurDoubleJoueur=0;
                    plateau.messageLog("***************************************");
                    plateau.messageLog("Au tour de " + jTemp.getNomJoueur());
                    jUi.afficheProprietes(jTemp);
                    plateau.setValues(jTemp);
                } else {
                    compteurDoubleJoueur++;
                }
        }
        
	public boolean lancerDesAvancer(Joueur aJ,int s) {
            //s = 0 si le joueur sort de prison en faisant un double
            //tUi.message("Nom : "+aJ.getNomJoueur());
            int d1,d2,sD;
            if (s==0) {
            d1 = lancerDe();
            d2 = lancerDe();
            sD = d1+d2;
            tUi.dés(d1, d2);
            } else {
                //d1 et d2 sont égaux pour return true
                d1 = 0;
                d2 = 0;
                sD = s;
            }
            // Donne des informations sur la somme des dés, la position actuelle du joueur et la position qu'il occupe après avoir avancé.
            tUi.printAncienCarreau(aJ.getPositionCourante());
            Avancer(aJ, sD);
            tUi.printNouveauCarreau(aJ.getPositionCourante());
            // Donne les noms, positions, argent, propriétés de tous les joueurs de la partie.
            /*for (Joueur j : joueurs){
            tUi.message("Nom : "+j.getNomJoueur());
            tUi.message("Position : "+j.getPositionCourante());
            tUi.message("Argent : "+j.getCash());
            tUi.message("Propriété(s) :");
            for (Compagnie c : j.getCompagnies()){
            tUi.message(c.getNom());
            }
            for (Gare g : j.getGares()){
            tUi.message(g.getNom());
            }
            for (ProprieteAConstruire p : j.getProprietesAConstruire()){
            tUi.message(p.getNom());
            tUi.message("Groupe : "+p.getGroupe());
            tUi.message("Avec "+p.getImmobilier()+" construction(s)");
            }
            }*/
            return d1==d2;
	}

	public int lancerDe() {
            Random gene = new Random();               
            return gene.nextInt(6)+1;         
	}

	public void Avancer(Joueur aJ, int aNumCase) {
            aJ.setPositionCourante(aJ.getPositionCourante().getNum()+aNumCase);
	}
        
         public void construire(Joueur j) {
            pUi.construction();
            HashMap<CouleurPropriete,ArrayList<ProprieteAConstruire>> list = new HashMap<>(); //Liste par groupe de couleur de toutes les proprietes du joueur
            for (ProprieteAConstruire p : j.getProprietesAConstruire()) {
                if (!list.containsKey(p.getGroupe().getCouleur())) { //Création de la nouvel liste (dans la hashmap) pour un nouveau groupe
                    ArrayList<ProprieteAConstruire> sousList = new ArrayList<>();
                    list.put(p.getGroupe().getCouleur(),sousList); //Nouvelle valeur de hashMap
                }
                list.get(p.getGroupe().getCouleur()).add(p); //Nouvelle valeur de l'arrayList de la hashMap
            }
            for (CouleurPropriete c : list.keySet()) { //Boucle de vérification pour les groupes complets disponibles à la construction
                if (list.get(c).size()!=list.get(c).get(0).getNbPropriete()) {
                    list.remove(c);
                }
            }
            
            if (list.isEmpty()) {
                pUi.aucunGroupeComplet(); //fin
            } else {
                CouleurPropriete coul = pUi.chooseGroupe(list);
                    ArrayList<ProprieteAConstruire> listP = list.get(coul); //Récupération des proprietes du groupe selectionné
                        boolean stop = false; //Boolean de construction rapide dans sur les mêmes propriétés
                        for (ProprieteAConstruire p : listP) {
                            if (p.isHypotheque()) {
                                pUi.erreurHypo();
                                stop = false;
                            }
                        }
                        while (!stop) {
                            int max = listP.get(0).getImmobilier(); //Max sera la valeur immobilère max des terrains (0 à 5)
                            boolean onAChanger = false; //Restera false si les terrains ont le même nombre d'immobilier
                            for (ProprieteAConstruire p : listP) {
                                if (p.getImmobilier()!=max) {
                                    onAChanger = true;
                                    if (p.getImmobilier()>max) {
                                        max = p.getImmobilier();
                                    }
                                }
                            }
                            if (!onAChanger && max==5) {
                                stop=true;
                                pUi.erreurHotel(); //fin
                            } else {
                                HashMap<Integer,ProprieteAConstruire> listPPotentiel = new HashMap<>(); //Liste contenant les proprietés constructibles
                                if (!onAChanger) {
                                    max++; //Tous les terrains ont la même valeur immobilière mais pas encore d'hotel
                                }
                                pUi.construireSur();
                                for (ProprieteAConstruire p : listP) {
                                        if (p.getImmobilier()<max) {
                                        listPPotentiel.put(p.getNum(), p); //Remplissage de la liste
                                        pUi.printPropriete(p);
                                        }
                                }
                                if (max>4 && this.getNbHotels()<1) { //Vérification de maisons et hotels restant à la banque
                                    stop = pUi.errorBanque(true);
                                } else if (max<5 && this.getNbMaisons()<1) {
                                    stop = pUi.errorBanque(false);
                                } else { //Choix du terrain
                                    int num = pUi.chooseNum();
                                    try {
                                        listPPotentiel.get(num).construire();
                                    } catch(NullPointerException e) {
                                        pUi.errorConstruction();
                                    }
                                    //Boucle de reconstruction rapide
                                        stop = !pUi.encoreConstruire();
                                }
                            }
                        }
                    }
        }
         
         public void detruire(Joueur j) throws Exception {
             HashMap<CouleurPropriete,ArrayList<ProprieteAConstruire>> listP = new HashMap<>();
             ArrayList<ProprieteAConstruire> pros;
             for (CouleurPropriete c : CouleurPropriete.values()) {
                 pros = j.getProprietesAConstruire(c);
                 if (!pros.isEmpty() && pros.get(0).getNbPropriete()==pros.size()) {
                    int immo = 0;
                     for(ProprieteAConstruire p : pros) {
                        immo+=p.getImmobilier();
                    }
                    if (immo>0) {
                        listP.put(pros.get(0).getCouleur(), pros);
                    } 
                 }
             }
             if (listP.isEmpty()) {
                 throw new Exception("Aucun de vos groupes de propriétés ne contiennent des constructions");
             } else { 
               pros = listP.get(pUi.chooseGroupe(listP));
               boolean stop = false;
               while (!stop) {
                   int min = pros.get(0).getImmobilier(); 
                   boolean onAChanger = false; 
                   for (ProprieteAConstruire p : pros) {
                        if (p.getImmobilier()!=min) {
                            onAChanger = true;
                            if (p.getImmobilier()<min) {
                                min = p.getImmobilier();
                            }
                       }
                   }
                   if (!onAChanger) {
                       min--;
                   }
                   HashMap<Integer,ProprieteAConstruire> listPPotentiel = new HashMap<>();
                   pUi.printDetruire();
                   for (ProprieteAConstruire p : pros) {
                       if (p.getImmobilier()>min) {
                           listPPotentiel.put(p.getNum(), p);
                           pUi.printPropriete(p);
                       }
                   }
                   if (min<0) {
                       stop = pUi.errorVide();
                   } else {
                       try {
                            listPPotentiel.get(pUi.chooseNum()).detruire();
                            jUi.printCashVous(j);
                       } catch(NullPointerException e){
                           pUi.errorDestruction();
                       }
                        stop = !pUi.encoreDetruire();
                   }
               }
             }
         }
         
        public void gestion(){
            gUi = new GestionUI(this, jTemp);
            gUi.setVisible(true);
        }
        
        public void hypotheque(Joueur j,boolean display) throws Exception{
            String rep = pUi.menuHypo(display);
            switch(rep) {
                case "lever":
                {
                    HashMap<Integer,CarreauPropriete> list = new HashMap<>();
                    pUi.printListHypo();
                    for (ProprieteAConstruire p : j.getProprietesAConstruire()) {
                        if (p.isHypotheque()) {
                            list.put(p.getNum(), p);
                            pUi.printProprieteProprietaire(p);
                        }
                    }
                    for (Gare g : j.getGares()) {
                        if (g.isHypotheque()) {
                            list.put(g.getNum(), g);
                            pUi.printGare(g);
                        }
                    }
                    for (Compagnie c : j.getCompagnies()) {
                        if (c.isHypotheque()) {
                            list.put(c.getNum(), c);
                            pUi.printCompagnie(c);
                        }
                    }
                    if (list.isEmpty()){
                        pUi.errorHypo();
                    } else {
                        boolean ok = false;
                        CarreauPropriete c = (CarreauPropriete)carreaux.get(2); //Initialisation de la variable
                        while (!ok) {
                            try {
                                c = list.get(pUi.chooseHypo());
                                ok = true;
                            } catch (java.lang.NullPointerException e) {
                                pUi.errorNonHypo();
                            }
                        }
                        pUi.leverHypo(c);
                        if (j.getCash()<c.getPrixHypotheque()) {
                            jUi.errorArgent(j);
                        } else {
                            if (pUi.continuerHypo()) {
                                c.leverHypotheque();
                                jUi.printCashVous(j);
                            }
                        }
                    }
                    break;
                }
                case "hypotheque":
                {
                    HashMap<Integer,CarreauPropriete> list = new HashMap<>();
                    pUi.hypoDispo();
                    for (CouleurPropriete c : CouleurPropriete.values()) {
                        ArrayList<ProprieteAConstruire> pros = j.getProprietesAConstruire(c);
                        boolean ok = true;
                        if(!pros.isEmpty()) {
                            if (pros.size()==pros.get(0).getNbPropriete()) {
                                int nb = 0;
                                for (ProprieteAConstruire p : pros) {
                                    nb+=p.getImmobilier();
                                }
                                if (nb>0) {
                                    ok = false;
                                }
                            }
                            if (ok) {
                                for (ProprieteAConstruire p : pros) {
                                    if (!p.isHypotheque()){
                                        list.put(p.getNum(), p);
                                        pUi.printProprieteProprietaire(p);
                                    }
                                }
                            }
                        }    
                    }
                    for (Gare g : j.getGares()) {
                        if (!g.isHypotheque()) {
                            list.put(g.getNum(), g);
                            pUi.printGare(g);
                        }
                    }
                    for (Compagnie c : j.getCompagnies()) {
                        if (!c.isHypotheque()) {
                            list.put(c.getNum(), c);
                            pUi.printCompagnie(c);
                        }
                    }
                    if (list.isEmpty()){
                        throw new Exception("Vous n'avez aucune propriété disponible à l'hypotheque");
                    } else {
                       boolean ok = false;
                        CarreauPropriete c = (CarreauPropriete)carreaux.get(2); //Initialisation de la variable
                        while (!ok) {
                            try {
                                c = list.get(pUi.chooseHypo());
                                ok = true;
                            } catch (java.lang.NullPointerException e) {
                                pUi.errorHypoNonProposee();
                            }
                        }
                        pUi.printHypo(c);
                            if (pUi.continuerHypo()) {
                                c.hypotheqer();
                                jUi.printCashVous(j);
                            }
                        }
                    }
                }
            }
        
        
        public void echanger (Joueur j1, Joueur j2, Echange e1, Echange e2){
            for (CarreauPropriete cp : e1.getListP()){
                cp.setProprietaire(j2);
            }
            for (CarreauPropriete cp : e2.getListP()){
                cp.setProprietaire(j1);
            }
            j1.addCash(e2.getSomme());
            j2.removeCash(e2.getSomme());

            j2.addCash(e1.getSomme());
            j1.removeCash(e1.getSomme());
                                   
        }

        
	public int getPosition(Joueur aJ) {
                return aJ.getPositionCourante().getNum();
	}
        
        public Carreau getCarreau (int c){
            return this.carreaux.get(c);
        }
        
        public void retourCarte(Carte c){
            cartes.retourCarte(c);
        }
    /**
     * @return the nbMaisons
     */
    public int getNbMaisons() {
        return nbMaisons;
    }

    /**
     * @param nbMaisons the nbMaisons to set
     */
    public void setNbMaisons(int nbMaisons) {
        this.nbMaisons = nbMaisons;
    }

    /**
     * @return the nbHotels
     */
    public int getNbHotels() {
        return nbHotels;
    }

    /**
     * @param nbHotels the nbHotels to set
     */
    public void setNbHotels(int nbHotels) {
        this.nbHotels = nbHotels;
    }
    
    public boolean removeHotel() {
        if (nbHotels>0) {    
            nbHotels--;
            return true;
        } else {
            return false;
        }
    }
    
    public void addHotel() {
        nbHotels++;
    }
    
    public boolean removeMaison() {
        if (nbMaisons>0) {    
            nbMaisons--;
            return true;
        } else {
            return false;
        }
    }
    
    public void addMaison() {
        nbMaisons++;
    }
    
    public void addMaison(int i) {
        nbMaisons+=i;
    }
    public CarreauPropriete getCarreauPropriete(String s){
        for (CarreauPropriete cp : this.getCarreauxPropriete()){
            if (cp.getNom().equals(s)){
                return cp;
            }
        } 
        return null;
    }
    public ArrayList<CarreauPropriete> getCarreauxPropriete(){
        ArrayList<CarreauPropriete> listeCp = new ArrayList<>();
        for (Carreau c : carreaux.values()){
            if(c.getClass().toString().equals("class Jeu.ProprieteAConstruire") || c.getClass().toString().equals("class Jeu.Gare") || c.getClass().toString().equals("class Jeu.Compagnie")) {
                listeCp.add((CarreauPropriete) c);
            }
        }
        return listeCp;            
    }
    
    /**
     * @return the carreaux
     */
    public HashMap<Integer,Carreau> getCarreaux() {
        return carreaux;
    }

    /**
     * @param carreaux the carreaux to set
     */
    public void setCarreaux(HashMap<Integer,Carreau> carreaux) {
        this.carreaux = carreaux;
    }

    /**
     * @return the joueurs
     */
    public ArrayList<Joueur> getJoueurs() {
        return joueurs;
    }

    /**
     * @param joueurs the joueurs to set
     */
    public void setJoueurs(ArrayList<Joueur> joueurs) {
        this.joueurs = joueurs;
    }

    /**
     * @return the interface_2
     */
    public Interface getInterface_2() {
        return interface_2;
    }

    /**
     * @param interface_2 the interface_2 to set
     */
    public void setInterface_2(Interface interface_2) {
        this.interface_2 = interface_2;
    }

    public PaquetsCartes getCartes() {
        return cartes;
    }
    
}
