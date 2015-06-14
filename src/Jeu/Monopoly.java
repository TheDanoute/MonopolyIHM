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
            ProprieteAConstruire pAC;
            for (CarreauPropriete cP : this.getCarreauxPropriete()){
                if (cP.getNum()<=4) {
                    cP.setProprietaire(joueurs.get(2));
                    pAC = (ProprieteAConstruire) cP;
                    pAC.setImmobilier(2);
                } else if (cP.getNum()>37) {
                    cP.setProprietaire(joueurs.get(1));
                    pAC = (ProprieteAConstruire) cP;
                    pAC.setImmobilier(5);
                } else if (cP.getNum()>=7 && cP.getNum()<=10){
                    cP.setProprietaire(joueurs.get(0));
                } else if (cP.getNum()==6 || cP.getNum()==16) {
                    cP.setProprietaire(joueurs.get(2));
                }
            }
            joueurs.get(2).removeCash(1451);
            joueurs.get(0).removeCash(143);
            joueurs.get(1).addCash(352);
            joueurs.get(0).addCartePrison();
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
                this.getPlateau().update();
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
        
         public void construire(ProprieteAConstruire pAC) {
            ArrayList<ProprieteAConstruire> listP = pAC.getProprietaire().getProprietesAConstruire(pAC.getCouleur());
            boolean ok = true;
            int immobilier = pAC.getImmobilier();
            if (immobilier>5) {
                ok = false;
            } else {
                if (listP.size()!=pAC.getNbPropriete()) {
                ok = false;
            } else {
                for (ProprieteAConstruire p : listP) {
                    if (p.getImmobilier()<immobilier) {
                        ok = false;
                    } else if (p.isHypotheque()) {
                        ok = false;
                    }
                }
            }
            if (ok) {
                pAC.construire();
            }
        }
         }
         
        public void detruire(ProprieteAConstruire pAC) throws Exception {
            ArrayList<ProprieteAConstruire> listP = pAC.getProprietaire().getProprietesAConstruire(pAC.getCouleur());
            boolean ok = true;
            int immobilier = pAC.getImmobilier();
            if (immobilier<1) {
                ok = false;
            } else {
                int max = 0;
                for (ProprieteAConstruire p : listP) {
                    max += p.getImmobilier();
                    if (p.getImmobilier()>immobilier) {
                        ok = false;
                    }
                }
                if (max<1) {
                   throw new Exception("Aucun de vos groupes de propriétés ne contiennent des constructions"); 
                }
            if (ok) {
                pAC.detruire();
            }
            }
        }
         
        public void gestion(){
            gUi = new GestionUI(this, jTemp);
            gUi.setVisible(true);
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
