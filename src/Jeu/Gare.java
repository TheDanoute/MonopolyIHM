package Jeu;


public class Gare extends CarreauPropriete {
        
        private int loyer;
        
        public Gare (int num,String nom,Monopoly m,int p) {
            super(num,nom,m,p);
            this.setLoyer(25);
        }

    
        
        @Override
	public int getLoyer() {
		return loyer*2^(this.getNbPropriete()-1);
	}
        private void setLoyer(int l){
            loyer=l;
        }

        @Override
	public int getNbPropriete() {
		return super.getProprietaire().getNombreGare();
	}

        @Override
        public void action(Joueur j){
            if (this.getProprietaire()==null) {
                if (super.getMonopoly().getpUi().printAchat(this)) {
                    if (j.getCash()>=this.getPrix()) {
                        j.removeCash(this.getPrix());
                        j.getMonopoly().getPlateau().setValues(j);
                        super.getMonopoly().getjUi().printCashVous(j);
                        this.setProprietaire(j);
                    } else {
                        super.getMonopoly().getjUi().errorArgent(j);
                        if (super.getMonopoly().getpUi().jEssaye()) {
                            if (j.jEssaye(super.getPrix())) {
                                j.removeCash(this.getPrix());
                                j.getMonopoly().getPlateau().setValues(j);
                                super.getMonopoly().getjUi().printCashVous(j);
                                this.setProprietaire(j);
                            }
                        }
                    }
                }
            } else if (this.getProprietaire()==j) {
                super.getMonopoly().getpUi().printProprePAC(this);
            } else {
                if (super.getMonopoly().getpUi().toucherLoyer(this)) {
                    int l = this.getLoyer();
                    j.removeCash(l);
                    j.getMonopoly().getPlateau().setValues(j);
                    this.getProprietaire().addCash(l);
                    super.getMonopoly().getjUi().printCashLe(super.getProprietaire());
                    super.getMonopoly().getjUi().printCashLe(j);
                 }
            }
            super.getMonopoly().getPlateau().update();
        }
        @Override
        public void setProprietaire(Joueur j) {
            if (this.getProprietaire()!=null) {
                this.getProprietaire().removeGare(this);
            }
            super.setProprietaire(j);
            j.addGare(this);
        }
        
        /*@Override
        public String getDescription() {
            if (super.isHypotheque()) {
                return "Gare HYPOTHEQUEE : " + super.getDescription();
            } else {
                return "Gare : " + super.getDescription();
            }
        }*/
        
        @Override
        public void retourBanque(){
            super.retourBanque();
            this.setProprietaire(null);
        }
}