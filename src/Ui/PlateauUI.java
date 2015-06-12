package Ui;
import Jeu.*;
import java.awt.*;
public class PlateauUI extends Canvas{
    private Monopoly monopoly;
    
    private Image background;
    
    public PlateauUI(Monopoly m){
        this.setMonopoly(m);
        setSize(750, 746);
        setBackground(Color.white);
        background = Toolkit.getDefaultToolkit().getImage("src/Data/PlateauMonopoly.png"); 
    }
    public Monopoly getMonopoly() {
        return monopoly;
    }
    private void setMonopoly(Monopoly monopoly) {
        this.monopoly = monopoly;
    }
    
    
    @Override
    public void paint(Graphics g){
        g.drawImage(background, 0, 0, this);
        this.paintCase(g);
        this.paintImmobilier(g);
        this.paintJoueurs(g);
    }
    
    public void paintJoueurs(Graphics g) {
        for (Joueur j : this.getMonopoly().getJoueurs()){
            g.setColor(j.getColor());
            Dimension dJ = this.getCoordonnéeJoueur(j);
            g.fillOval(dJ.width, dJ.height, 25, 25);
        }
    }
    
    public void paintCase(Graphics g) {
        for (CarreauPropriete cP : this.getMonopoly().getCarreauxPropriete()) {
            if (cP.getProprietaire()!=null) {
                g.setColor(cP.getProprietaire().getColor());
                Dimension dC = this.getCoordonnéeCase(cP);
                if (cP.getNum()<11 || (cP.getNum()>20 && cP.getNum()<31)) {
                    g.fillRect(dC.width, dC.height, 59, 20);
                } else {
                    g.fillRect(dC.width, dC.height, 20, 59);
                }
            }
        }
    }
    
    public void paintImmobilier(Graphics g){
        for (CarreauPropriete cP : this.getMonopoly().getCarreauxPropriete()) {
            if (cP.getClass().getSimpleName().equals("ProprieteAConstruire")) {
                ProprieteAConstruire pAC = (ProprieteAConstruire) cP;
                Dimension dP = this.getCoordonnéeCase(cP);
                if (pAC.getImmobilier()>0 && pAC.getImmobilier()<5) {
                    g.setColor(Color.green);
                    for (int i = 0 ; i<pAC.getImmobilier() ; i++) {
                        g.fillRect(dP.width+i*4, dP.height+5, 10, 10);
                    }
                } else if (pAC.getImmobilier()==5) {
                    g.setColor(Color.red);
                    g.fillRect(dP.width+30, dP.height+2, 16, 16);
                }
            }
        }
    }
    
    public void update(){
        update(this.getGraphics());
    }
    
    public Dimension getCoordonnéeJoueur(Joueur j) {
        int tampon = 750/26;
        if (j.getPositionCourante().getNum()<11) {
            return new Dimension(tampon*4 + (750 * (10-j.getPositionCourante().getNum()))/12,this.getSize().height-tampon-tampon);
        } else if (j.getPositionCourante().getNum()>10 && j.getPositionCourante().getNum()<21) {
            return new Dimension(tampon+tampon,tampon*4 + (750 * (20-j.getPositionCourante().getNum()))/12);
        } else if (j.getPositionCourante().getNum()>20 && j.getPositionCourante().getNum()<31) {
            return new Dimension(tampon*2 + (750 * (j.getPositionCourante().getNum()-21))/12,tampon+tampon);
        } else {
            return new Dimension(this.getSize().width-tampon-tampon,tampon*2 + (750 * (j.getPositionCourante().getNum()-31)));
        }
    }
    
    public Dimension getCoordonnéeCase(CarreauPropriete c) {
        if (c.getNum()<11) {
            return new Dimension(98+(62*(10-c.getNum())),652);
        } else if (c.getNum()>10 && c.getNum()<21) {
            return new Dimension(76,96+(62*(20-c.getNum())));
        } else if (c.getNum()>20 && c.getNum()<31) {
            return new Dimension(98+(62*(c.getNum()-22)),73);
        } else {
            return new Dimension(654,96+(63*(20-c.getNum())));
        }
    }
}