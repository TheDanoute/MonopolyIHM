package Jeu;

import Jeu.Monopoly;
import Jeu.ProprieteAConstruire;
import java.awt.Color;

import java.util.ArrayList;

public class Joueur {
	private String nomJoueur;
	private int cash;
	private Monopoly monopoly;
	private ArrayList<Compagnie> compagnies;
	private ArrayList<Gare> gares;
	private Carreau positionCourante;
	private ArrayList<ProprieteAConstruire> proprietesAConstruire;
        private int compDouble;
        private int enPrison;
        private ArrayList<CartePrison> cartePrison ;
        private int desDepart;
        private Color couleur;

    public Joueur(String nomJoueur,Color couleur, Monopoly monopoly) {
        this.nomJoueur = nomJoueur;
        this.monopoly = monopoly;
        this.setCash(1500);
        this.setPrison(false);
        compagnies = new ArrayList<>();
        gares = new ArrayList<>();
        proprietesAConstruire = new ArrayList<>();
        desDepart = monopoly.lancerDe();
        cartePrison = new ArrayList<>();
        this.setPositionCourante(1);
        this.couleur=couleur;
    }

    public Carreau getPositionCourante() {
        return positionCourante;
    }

    public ArrayList<Compagnie> getCompagnies() {
        return compagnies;
    }

    public ArrayList<Gare> getGares() {
        return gares;
    }

    public ArrayList<ProprieteAConstruire> getProprietesAConstruire() {
        return proprietesAConstruire;
    }
    
    public ArrayList<ProprieteAConstruire> getProprietesAConstruire(CouleurPropriete c) {
        ArrayList<ProprieteAConstruire> list = new ArrayList<>();
        for (ProprieteAConstruire p : proprietesAConstruire) {
            if (p.getCouleur()==c) {
                list.add(p);
            }
        }
        return list;
    }
    
    public Color getColor() {
        return couleur;
    }
    
    public Monopoly getMonopoly(){
        return monopoly;
    }
    
    public void setPositionCourante(Carreau positionCourante) {
        this.positionCourante = positionCourante;
    }
    
    public int getNBTourPrison(){
        return enPrison;
    }
    
    public void ajouterTourPrison() {
        enPrison++;
    }
    
    public void setPositionCourante(int p) {
       if (p>40) {
           p-=40; 
           if (p!=1) {
           this.addCash(200);
           getMonopoly().getjUi().passageDepart(this);
           }
       }
       this.setPositionCourante(monopoly.getCarreau(p));
    }

        
        
        public void enPrison() {
            this.setPositionCourante(monopoly.getCarreau(11));
            this.setPrison(true);
        }
        
        public void sortPrison() {
            this.setPrison(false);
        }
        
        public void utilCartePrison(){
            Carte c = cartePrison.get(0);
            c.action(this);
            cartePrison.remove(c);
            monopoly.retourCarte(c);
        }
        private void setPrison(boolean p){
            if (p) {
                enPrison=1;
            } else {
                enPrison=0;
            }
        }
        public boolean getPrison(){
            if (enPrison>0) {
                return true;
            } else {
                return false;
            }
        }
        
        public int getNBCartePrison() {
            return cartePrison.size();
        }
        
        public void addCartePrison(Carte c) {
            CartePrison cP = (CartePrison) c;
            cartePrison.add(cP);
        }
        
	public int getCash() {
		return this.cash;
	}

	private void setCash(int aC) {
		this.cash = aC;
	}

	public void addCash(int aC) {
            // Ajoute au joueur le montant aC
            if (aC>0){
                this.cash+=aC;
            } else {// Dans le cas où le joueur tire une carte avec un malus, aC sera négatif
                this.removeCash(-aC);
            }
	}

	public void removeCash(int aC){
                // Soustrait au joueur le montant aC
		this.cash-=aC;
                // Si le cash du joueur est négatif après le retrait il doit vendre pour récuperer son négatif ou il fait faillite
                if (this.getCash()<0){
                getMonopoly().getjUi().cBientotLaFin(this);
                    if (this.jEssaye(0)) {
                        getMonopoly().getjUi().okJeGere();
                    } else {
                        getMonopoly().getjUi().cLaFin(this);
                        this.faillite();
                    }
                }
	}
        
        public boolean jEssaye(int objectif) {
            boolean vendre = true ;
                boolean hypotheque = true ;
                    while (this.getCash()<objectif && (vendre || hypotheque))    {
                        String rep = getMonopoly().getjUi().chooseLaFin();
                        switch(rep) {
                            case "detruire":
                            {
                                if (vendre) {
                                    try {
                                        this.getMonopoly().detruire(this);
                                    } catch(Exception e){
                                        getMonopoly().gettUi().message(e.getMessage());
                                        vendre = false;
                                    }
                                } else {
                                    getMonopoly().getjUi().cLaFinDetruire();
                                }
                                 break;
                            }
                            case "hypotheque":
                            {
                                if(hypotheque) {
                                    try {
                                        this.getMonopoly().hypotheque(this,false);
                                    } catch(Exception e){
                                        getMonopoly().gettUi().message(e.getMessage());
                                        hypotheque = false;
                                    }
                                } else {
                                    getMonopoly().getjUi().cLaFinHypo();
                                }
                                 break;
                            }
                        }
                        getMonopoly().getjUi().printCashVous(this);
                    }
                   return !(this.getCash()<objectif);
                }
        
        public boolean sortDuJeu() {
            if (getMonopoly().getjUi().sortDuJeu(this)) {
                this.faillite();
                return true;
            } else {
                return false;
            }
        }
        
        private void faillite() {
                        for (ProprieteAConstruire p : proprietesAConstruire) {
                            p.retourBanque();
                        }
                        for (Compagnie c : compagnies) {
                           c.retourBanque();
                        }
                        for (Gare g : gares) {
                            g.retourBanque();
                        }
                        this.getMonopoly().getJoueurs().remove(this);
        }

	public String getNomJoueur() {
		return this.nomJoueur;
	}

	private void setNomJoueur(String aN) {
		this.nomJoueur = aN;
	}

	public void payerLoyer(int aLoyer) {
		this.cash-=aLoyer;
	}

	public int getNombreGare() {
		return gares.size();
	}
        
        public int getNombreCompagnies() {
            return compagnies.size();
        }
        
        public int getDesDepart() {
            return desDepart;
        }
        
        protected void setDesDepart(int newsomme) {
            this.desDepart=newsomme;
        }
        
        public void addPropriete(ProprieteAConstruire p) {
            proprietesAConstruire.add(p);
        }
        public void removePropriete(ProprieteAConstruire p) {
            proprietesAConstruire.remove(p);
        }
        public void addGare(Gare g){
            gares.add(g);
        }
        public void removeGare(Gare g){
            gares.remove(g);
        }
        public void addCompagnie(Compagnie c){
            compagnies.add(c);
        }
        public void removeCompagnie(Compagnie c){
            compagnies.remove(c);
        }
        public void addCartePrison (){
            cartePrison.add(new CartePrison("CC","P"));
        }
        public void removeCartePrison(){
            cartePrison.remove(0);
        }
        public  ArrayList<CarreauPropriete> listeProprietesEchangeables (){
        ArrayList<CarreauPropriete> listP = new ArrayList<CarreauPropriete>();
        for (Compagnie c : this.getCompagnies()){
                listP.add(c);
        }
        for (Gare g : this.getGares()){
                listP.add(g);
        } 
        for (CouleurPropriete c : CouleurPropriete.values()) {
            if (!this.getProprietesAConstruire(c).isEmpty()) {
                int immo = 0;
                for (ProprieteAConstruire p : this.getProprietesAConstruire(c)) {
                    immo+=p.getImmobilier();
                }
                if (immo==0) {
                    for (ProprieteAConstruire p : this.getProprietesAConstruire(c)) {
                            listP.add(p);
                    }
                }
            }
        }
        return listP;
    }
}
