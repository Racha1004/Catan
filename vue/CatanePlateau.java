package vue;

import java.util.ArrayList;
import javax.swing.*;

import jeu.*;
import plateau.*;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

public class CatanePlateau extends JPanel{
	Point p;
	private Location loc = new Location(1,2);
	private final int cote=100;
	private final int marge=100;
	private Jeu jeu;
	private ArrayList<Joueur> joueurs;
	private Tuile[][] tuiles;
	private Route[][][] routes;
	private Construction[][] constructions;
	private int etat=0;
	/*
	 * etat=0 rien à faire
	 * etat=1 deplacer le voleur
	 * etat=2 placer une colonie
	 * etat=3 placer une route
	 * etat=4 placer une ville
	 * etat=5 placer une colonie sans route*/
	private int index=16;
	private boolean distribuerRessources=false;
	class Souris extends MouseAdapter{
		public void mouseClicked(MouseEvent e) {
			Point p = new Point(e.getX(),e.getY());
			if(etat==1) {
				if(p!=null) {
					Location loc = pxToTuile(p);
					if(loc!=null) {
						if(jeu.getPlateau().deplacerVoleur(loc)) {
							index--;
						}
						if(index==0) {
							etat=0;
						}
					}
				}
			}
			else {
				if(etat==2) {
					if(p!=null) {
						Location loc = pxToConstruction(p);
						if(loc!=null) {
							if(jeu.getPlateau().ajouterConstruction(loc, Lanceur.getJoueurCourant())) {
								index--;
							}
							if(index==0) {
								etat=0;
							}
						}
					}
				}
				else {
					if(etat==3) {
						if(p!=null) {
							AreteLocation loc = pxToRoute(p);
							if(loc!=null) {
								if(jeu.getPlateau().ajouterRoute(loc, Lanceur.getJoueurCourant())) {
									
									index--;
								}
								if(index==0) {
									etat=0;
								}
							}
						}
					}
					else {
						if(etat==4) {
							if(p!=null) {
								Location loc = pxToConstruction(p);
								if(loc!=null) {
									if(jeu.getPlateau().ajouterVille(loc, Lanceur.getJoueurCourant())) {
										index--;
										etat=3;
									}
									if(index==0) {
										etat=0;
									}
								}
							}
						}
						else {
							if(etat==5) {
								if(p!=null) {
									Location loc = pxToConstruction(p);
									if(loc!=null) {
										if(jeu.getPlateau().ajouterConstructionSansRoute(loc, Lanceur.getJoueurCourant())) {
											index--;
											etat=4;
											if(distribuerRessources) {
												ArrayList<Tuile> tuile = jeu.getPlateau().getAdjacenteTuiles(loc);
												for(Tuile t : tuile) {
													if(t != null) {
														Lanceur.getJoueurCourant().RecevoirRessources(t.nomRessouce(),1);
													}
												}
											}
										}
										if(index==0) {
											etat=0;
										}
									}
								}
							}
						}
					}
				}
			}
			repaint();
		}
	}
	
	public CatanePlateau() {
		
		
	}
	public void setJeu(ArrayList<Joueur> joueurs) {
		jeu = new Jeu(Lanceur.joueurs);
		this.joueurs=Lanceur.joueurs;
		tuiles = jeu.getPlateau().getTuiles();
		routes = jeu.getPlateau().getRoutes();
		constructions = jeu.getPlateau().getConstructions();
		setBackground(Color.WHITE);
		MouseListener m = new Souris();
		addMouseListener(m);
	}
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 17));
		super.paintComponent(g2);
		if(tuiles!=null && routes!=null && constructions!=null ) {
			for(int i=1; i<tuiles.length-1 ; i++) {
				for(int j=1; j<tuiles[0].length-1 ; j++) {
					drawCarre(tuiles[i][j],g2);
					drawType(tuiles[i][j],g2);
					drawCircle(tuiles[i][j],g2);
					drawNumber(tuiles[i][j],g2);
					drawVoleur(tuiles[i][j],g2);

				}
			}
			
			ports(g2);
			for(int i=1; i<routes.length ; i++) {
				for(int j=1; j<routes[0].length ; j++) {
					for(int o=0 ; o<routes[0][0].length ; o++) {
						if(!(i==5 && o==0)&&!(j==5 && o==1)) {
							drawRoute(routes[i][j][o],false,g2);
						}
					}
				}
			}
			for(int i=1; i<constructions.length; i++) {
				for(int j=1; j<constructions[0].length; j++) {
					drawConstruction(constructions[i][j],false,g2);
				}
			}
			p = MouseInfo.getPointerInfo().getLocation();
			SwingUtilities.convertPointFromScreen(p,this);
			if(etat==1) {
				Location loc = pxToTuile(p);
				if(loc!=null) {
					deplacerVoleur(loc,g2);
				}
			}
			else {
				if( etat==2 || etat==4 || etat==5 ) {
					Location loc = pxToConstruction(p);
					if(loc!=null) {
						drawConstruction(constructions[loc.getX()][loc.getY()],true,g2);
					}
				}
				else {
					if(etat==3) {
						AreteLocation loc = pxToRoute(p);
						if(loc != null) {
							drawRoute(routes[loc.getX()][loc.getY()][loc.getArete()],true,g2);
						}
					}
				}
			}
		}
		
		
		
		
		
	}
	public void drawCarre(Tuile tuile , Graphics2D g2) {
		int x = tuile.getLocation().getY();
		int y = tuile.getLocation().getX();
		String type = tuile.getType();
		switch(type) {
		case "Desert":
			g2.setColor(Color.WHITE);
			break;
		case "Foret":
			g2.setColor(Color.GREEN	);
			break;
		case "Pré":
			g2.setColor(new Color(0, 100, 0));
			break;
		case "Champs":
			g2.setColor(Color.YELLOW);
			break;
		case "Colline":
			g2.setColor(new Color(167, 103, 38));
			break;
		case "Montagne":
			g2.setColor(Color.GRAY);
			break;
		default:
			g2.setColor(Color.WHITE);
			break;
		}
		g2.fillRect(x*cote, y*cote ,cote,cote);
		g2.setColor(Color.BLACK);
		g2.drawRect(x*cote, y*cote ,cote,cote);
	}
	public void drawType(Tuile tuile , Graphics2D g2) {
		if(tuile.getType()==null) {
			return;
		}
		int x=tuile.getLocation().getY();
		int y = tuile.getLocation().getX();
		g2.setColor(Color.BLACK);
		g2.drawString(tuile.getType(), x*cote+5, y*cote+25);
	}
	public void drawNumber(Tuile tuile , Graphics2D g2) {
		if(tuile.getEthiquette()==0) {
			return;
		}
		int x=tuile.getLocation().getY();
		int y = tuile.getLocation().getX();
		if(tuile.getEthiquette()==6 || tuile.getEthiquette()==8) {
			g2.setColor(Color.RED);
		}
		else {
			g2.setColor(Color.BLACK);
		}
		g2.drawString("" + tuile.getEthiquette(), x*cote+43, y*cote+55);
	}
	public void drawVoleur(Tuile tuile, Graphics2D g2) {
		if (!tuile.aVoleur()) {
			return;
		}
		int x = tuile.getLocation().getY();
		int y = tuile.getLocation().getX();
		g2.setColor(Color.BLACK);
		g2.fillOval(x*cote+30, y*cote+30 ,40,40);
		g2.drawOval(x*cote+30, y*cote+30 ,40,40);
	}
	public void deplacerVoleur(Location loc , Graphics2D g2) {
		int x = loc.getY();
		int y = loc.getX();
		if(tuiles[y][x].aVoleur()) {
			return;
		}
		g2.setColor(Color.BLACK);
		g2.fillOval(x*cote+30, y*cote+30 ,40,40);
		g2.setColor(Color.BLACK);
		g2.drawOval(x*cote+30, y*cote+30 ,40,40);
	}
	public void drawCircle(Tuile tuile, Graphics2D g2) {
		if (tuile.getEthiquette()==0) {
			return;
		}
		int x = tuile.getLocation().getY();
		int y = tuile.getLocation().getX();
		g2.setColor(Color.WHITE);
		g2.fillOval(x*cote+30, y*cote+30 ,40,40);
		g2.setColor(Color.BLACK);
		g2.drawOval(x*cote+30, y*cote+30 ,40,40);
	}
	public void ports(Graphics2D g2) {
		AffineTransform reset = g2.getTransform();
		//Laine 2:1
		g2.setColor(new Color(119,181,254));
		g2.fillRect(2*cote, cote-25 ,cote,cote/4);
		g2.setColor(Color.BLACK);
		g2.drawRect(2*cote, cote-25 ,cote,cote/4);
		g2.translate(2*cote+70,cote-15);
		g2.rotate(Math.toRadians(180));
		g2.drawString(" 2:1 ",0,0);
		g2.setTransform(reset);
		g2.drawString("Laine",2*cote+20,cote-30);
		
		//General 3:1
		g2.setColor(new Color(119,181,254));
		g2.fillRect(4*cote, cote-25 ,cote,cote/4);
		g2.setColor(Color.BLACK);
		g2.drawRect(4*cote, cote-25 ,cote,cote/4);
		g2.translate(4*cote+70,cote-15);
		g2.rotate(Math.toRadians(180));
		g2.drawString(" 3:1 ",0,0);
		g2.setTransform(reset);
		
		// Minerai 2:1
		g2.setColor(new Color(119,181,254));
		g2.fillRect(1*cote, 5*cote ,cote,cote/4);
		g2.setColor(Color.BLACK);
		g2.drawRect(1*cote, 5*cote ,cote,cote/4);
		g2.drawString(" 2:1 ",1*cote+35,5*cote+20);
		g2.drawString("Minerai",1*cote+20,5*cote+45);
		
		// Argile 2:1
		g2.setColor(new Color(119,181,254));
		g2.fillRect(3*cote, 5*cote ,cote,cote/4);
		g2.setColor(Color.BLACK);
		g2.drawRect(3*cote, 5*cote ,cote,cote/4);
		g2.drawString(" 2:1 ",3*cote+35,5*cote+20);
		g2.drawString("Argile",3*cote+25,5*cote+45);
		//générale 3:1
	    g2.setColor(new Color(119,181,254));
   		g2.fillRect(cote-25, 1*cote ,cote/4,cote);
		g2.setColor(Color.BLACK);
		g2.drawRect(cote-25, 1*cote ,cote/4,cote);
		g2.translate(cote-20, 1*cote+30);
		g2.rotate(Math.toRadians(90));
		g2.drawString(" 3:1 ",0,0);
	    //Blé 2:1
				g2.setTransform(reset);
				g2.setColor(new Color(119,181,254));
				g2.fillRect(cote-25, 3*cote ,cote/4,cote);
				g2.setColor(Color.BLACK);
				g2.drawRect(cote-25, 3*cote ,cote/4,cote);
				g2.translate(cote-20, 3*cote+30);
				g2.rotate(Math.toRadians(90));
				g2.drawString(" 2:1 ",0,0);
				g2.setTransform(reset);
				g2.translate(cote-45, 3*cote+30);
				g2.rotate(Math.toRadians(90));
				g2.drawString("Blé",0,0);
		//Bois 2:1
				g2.setTransform(reset);
				g2.setColor(new Color(119,181,254));
				g2.fillRect(5*cote, 2*cote ,cote/4,cote);
				g2.setColor(Color.BLACK);
				g2.drawRect(5*cote, 2*cote ,cote/4,cote);
				g2.translate(5*cote+20, 2*cote+70);
				g2.rotate(Math.toRadians(270));
				g2.drawString(" 2:1 ",0,0);
				g2.setTransform(reset);
				g2.translate(5*cote+45, 2*cote+70);
				g2.rotate(Math.toRadians(270));
				g2.drawString("Bois",0,0);
		//générale 3:1
				g2.setTransform(reset);
				g2.setColor(new Color(119,181,254));
				g2.fillRect(5*cote, 4*cote ,cote/4,cote);
				g2.setColor(Color.BLACK);
				g2.drawRect(5*cote, 4*cote ,cote/4,cote);
				g2.translate(5*cote+20, 4*cote+70);
				g2.rotate(Math.toRadians(270));
				g2.drawString(" 3:1 ",0,0);
				g2.setTransform(reset);
	}
	public void drawRoute(Route r, boolean enDeplacement , Graphics2D g2) {
		Joueur joueur = r.getJoueur();
		if(joueur==null) {
			if(enDeplacement)
				g2.setColor(Color.WHITE);
			else
				return;
		}
		else {
			if(enDeplacement)
				return;
			else
				g2.setColor(joueur.getCouleur());
		}
		int x = r.getLocation().getY();
		int y = r.getLocation().getX();
		int arete = r.getLocation().getArete();
		if(arete==0) {
			g2.fillRect(x*cote, y*cote+12 ,5,75);
			g2.setColor(Color.BLACK);
			g2.drawRect(x*cote, y*cote+12 ,5,75);
		}
		else {
			if(arete==1) {
				g2.fillRect(x*cote+12, y*cote ,75,5);
				g2.setColor(Color.BLACK);
				g2.drawRect(x*cote+12, y*cote ,75,5);
			}
		}
	}
	public void drawConstruction(Construction c , boolean enDeplacement ,Graphics2D g2) {
		Joueur joueur = c.getJoueur();
		if(etat==2 || etat==5) {
			if(joueur==null) {
				if(enDeplacement)
					g2.setColor(Color.WHITE);
				else
					return;
			}
			else {
				if(enDeplacement)
					return;
				else
					g2.setColor(joueur.getCouleur());
			}
		}
		else {
			if(etat==4) {
				if(joueur==null) return;
				if(joueur==Lanceur.getJoueurCourant()) {
					if(enDeplacement) {
						if(c.getType()==0) {
							g2.setColor(Color.WHITE);
						}
						else {
							g2.setColor(joueur.getCouleur());
						}
						
					}
					else {
						g2.setColor(joueur.getCouleur());
					}
				}
				else {
					g2.setColor(joueur.getCouleur());
				}
			}
			else {
				if(joueur==null)
					return;
				else
					g2.setColor(joueur.getCouleur());
			}
		}
		int x = c.getLocation().getY();
		int y = c.getLocation().getX();
		int type = c.getType();
		if(type==0) {
			g2.fillRect(x*cote-15, y*cote-15 ,30,30);
			g2.setColor(Color.BLACK);
			g2.drawRect(x*cote-15, y*cote-15 ,30,30);
		}
		else {
			if(type==1) {
				g2.fillOval(x*cote-15, y*cote-15 ,30,30);
				g2.setColor(Color.BLACK);
				g2.drawOval(x*cote-15, y*cote-15 ,30,30);
			}
		}
	}
	public Location pxToTuile(Point p) {
		double x = p.getY();
		double y = p.getX();
		
		int xCoord = 0;
		int yCoord = 0;
		if(x<=1*cote || x>=5*cote || y<=1*cote || y>=5*cote ) {
			return null;
		}
		if(x>1*cote && x<2*cote) {
			xCoord = 1;
		}
		else {
			if(x>2*cote && x<3*cote) {
				xCoord = 2;
			}
			else {
				if(x>3*cote && x<4*cote) {
					xCoord = 3;
				}
				else {
					if(x>4*cote && x<5*cote) {
						xCoord = 4;
					}
				}
			}
		}
		if(y>1*cote && y<2*cote) {
			yCoord = 1;
		}
		else {
			if(y>2*cote && y<3*cote) {
				yCoord = 2;
			}
			else {
				if(y>3*cote && y<4*cote) {
					yCoord = 3;
				}
				else {
					if(y>4*cote && y<5*cote) {
						yCoord = 4;
					}
				}
			}
		}
		if(xCoord==0 || yCoord==0) return null;
		return new Location(xCoord,yCoord);
	}
	public Location pxToConstruction(Point p) {
		double x = p.getY();
		double y = p.getX();
		
		int xCoord = 0;
		int yCoord = 0;
		if(x>1*cote-30 && x<1*cote+30) {
			xCoord = 1;
		}
		else {
			if(x>2*cote-30 && x<2*cote+30) {
				xCoord = 2;
			}
			else {
				if(x>3*cote-30 && x<3*cote+30) {
					xCoord = 3;
				}
				else {
					if(x>4*cote-30 && x<4*cote+30) {
						xCoord = 4;
					}
					else {
						if(x>5*cote-30 && x<5*cote+30) {
							xCoord = 5;
						}
					}
				}
			}
		}
		if(y>1*cote-30 && y<1*cote+30) {
			yCoord = 1;
		}
		else {
			if(y>2*cote-30 && y<2*cote+30) {
				yCoord = 2;
			}
			else {
				if(y>3*cote-30 && y<3*cote+30) {
					yCoord = 3;
				}
				else {
					if(y>4*cote-30 && y<4*cote+30) {
						yCoord = 4;
					}
					else {
						if(y>5*cote-30 && y<5*cote+30) {
							yCoord = 5;
						}
					}
				}
			}
		}
		if(xCoord==0 || yCoord==0) return null;
		return new Location(xCoord,yCoord);
	}
	public AreteLocation pxToRoute(Point p) {
		double x = p.getX();
		double y = p.getY();
		
			if(y>1*cote-5 && y<1*cote+5) {
				if(x>1*cote && x<5*cote)
				return new AreteLocation(1,(int)x/cote,1);
			}
			if(y>2*cote-5 && y<2*cote+5) {
				if(x>1*cote && x<5*cote)
				return new AreteLocation(2,(int)x/cote,1);
			}
			if(y>3*cote-5 && y<3*cote+5) {
				if(x>1*cote && x<5*cote)
				return new AreteLocation(3,(int)x/cote,1);
			}
			if(y>4*cote-5 && y<4*cote+5) {
				return new AreteLocation(4,(int)x/cote,1);
			}
			if(y>5*cote-5 && y<5*cote+5) {
				if(x>1*cote && x<5*cote)
				return new AreteLocation(5,(int)x/cote,1);
			}
		
			if(x>1*cote-5 && x<1*cote+5) {
				if(y>1*cote && y<5*cote)
				return new AreteLocation((int)y/cote,1,0);
			}
			if(x>2*cote-5 && x<2*cote+5) {
				if(y>1*cote && y<5*cote)
				return new AreteLocation((int)y/cote,2,0);
			}
			if(x>3*cote-5 && x<3*cote+5) {
				if(y>1*cote && y<5*cote)
				return new AreteLocation((int)y/cote,3,0);
			}
			if(x>4*cote-5 && x<4*cote+5) {
				if(y>1*cote && y<5*cote)
				return new AreteLocation((int)y/cote,4,0);
			}
			if(x>5*cote-5 && x<5*cote+5) {
				if(y>1*cote && y<5*cote)
				return new AreteLocation((int)y/cote,5,0);
			}
		
		
		return null;
	}
	public Jeu getJeu() {
		return jeu;
	}
	public int getEtat() {
		return etat;
	}
	public void placerVoleur() {
		index=1;
		etat=1;
	}
	public void placerColonie(int x) {
		index=x;
		etat=2;
	}
	public void placerRoute(int x) {
		index=x;
		etat=3;
	}
	public void placerVille(int x) {
		index=x;
		etat=4;
	}
	public void placerColonieSansRoute(int x) {
		index=x;
		etat=5;
	}
	public void placerInSecoondTour() {
		index=1;
		etat=5;
		distribuerRessources=true;
		
	}
}

