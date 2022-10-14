package vue;


import java.awt.*;
import javax.swing.*;

import exceptions.*;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import jeu.*;
import plateau.*;


public class PartieBar extends JPanel{
	private VueGraphique jeuView;
	private boolean secondTour = false;
	private int cpt = 0;
	public final static int INTERVAL = 50;
	private Timer timer;
	private ArrayList<JComponent> placePanel = new ArrayList<JComponent>();
	private ArrayList<JComponent> achatColoniePanel = new ArrayList<JComponent>();
	private ArrayList<JComponent> achatRoutePanel = new ArrayList<JComponent>();
	private ArrayList<JComponent> achatVillePanel = new ArrayList<JComponent>();
	private ArrayList<JComponent> mettreEnPlacePanel =  new ArrayList<JComponent>();
	private ArrayList<JComponent> mainPanel =  new ArrayList<JComponent>();
	private ArrayList<JComponent> lancerDeePanel =  new ArrayList<JComponent>();
	private ArrayList<JComponent> constructionPanel =  new ArrayList<JComponent>();
	private ArrayList<JComponent> erreurPanel =  new ArrayList<JComponent>();
	private ArrayList<JComponent> commercePanel =  new ArrayList<JComponent>();
	private ArrayList<JComponent> choixJoueurPanel =  new ArrayList<JComponent>();
	private ArrayList<JComponent> inputRessourcesPanel	= new ArrayList<JComponent>();
	private ArrayList<JComponent> ressourcesPanel	= new ArrayList<JComponent>();
	private ArrayList<JComponent> confirmationPanel	= new ArrayList<JComponent>();
	private ArrayList<JComponent> voleurPanel	= new ArrayList<JComponent>();
	private ArrayList<JComponent> carteDevPanel	= new ArrayList<JComponent>();
	private ArrayList<JComponent> achatCarteDevPanel	= new ArrayList<JComponent>();
	private ArrayList<JComponent> jouerCarteDevPanel	= new ArrayList<JComponent>();
	private ArrayList<JComponent> introductionJoueursPanel	= new ArrayList<JComponent>();
	private ArrayList<JComponent> ajoutJoueursPanel	= new ArrayList<JComponent>();
	private ArrayList<JComponent> typeJoueursPanel	= new ArrayList<JComponent>();
	private ArrayList<String> inputRessources = new ArrayList<String>();
	private ArrayList<String> ressourcesAVendre	= new ArrayList<String>();
	private ArrayList<String> ressourcesAAcheter = new ArrayList<String>();
	private JComponent joueurCourant ;
	private int flag;
	private boolean soumission=true;
	private boolean ressourceChoisie = false;
	private Joueur joueurChoisi;
	private String resSelectioner ;
	private String ressourceASacrifier;
	private String ressourceAchetee;
	private int nombreJoueurs=0;
	private int index=0;
	public PartieBar(VueGraphique jeuView) {
		this.jeuView = jeuView;
		this.setLayout(null);
		joueurCourant = new JLabel("");
		joueurCourant.setBounds(275,10,160,20); 
		//setJoueurCourant(Test.getJoueurCourant());
		this.add(joueurCourant);
		// Place Panel pour afficher les messages
		JLabel message = new JLabel("");
		message.setBounds(250,130,250,20);
		placePanel.add(message);
		// AchatRoutePanel
		JLabel msg1 = new JLabel("");
		msg1.setBounds(250,130,250,20);
		achatRoutePanel.add(msg1);
		JButton annulerAchatRoute = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				constructionPanel();
				Lanceur.getJoueurCourant().annulerAcheterRoute();
			}
		});
		annulerAchatRoute.setText("Annuler l'achat");
		annulerAchatRoute.setBounds(270,260,170,20);
		achatRoutePanel.add(annulerAchatRoute);
		// AchatColoniePanel
		JLabel msg2 = new JLabel("");
		msg2.setBounds(250,130,250,20);
		achatColoniePanel.add(msg2);
		JButton annulerAchatColonie= new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				constructionPanel();
				Lanceur.getJoueurCourant().annulerAcheterColonie();
			}
		});
		annulerAchatColonie.setText("Annuler l'achat");
		annulerAchatColonie.setBounds(270,260,170,20);
		achatColoniePanel.add(annulerAchatColonie);
		// AchatVillePanel
		JLabel msg3 = new JLabel("");
		msg3.setBounds(250,130,250,20);
		achatVillePanel.add(msg3);
		JButton annulerAchatVille = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				constructionPanel();
				Lanceur.getJoueurCourant().annulerAcheterVille();
			}
		});
		annulerAchatVille.setText("Annuler l'achat");
		annulerAchatVille.setBounds(270,260,170,20);
		achatVillePanel.add(annulerAchatVille);
		
		
		//erreur panel
		JLabel erreur = new JLabel("");
		erreur.setBounds(200,130,320,20);
		erreurPanel.add(erreur);
		
		JButton continuer = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				switch(flag) {
				case 0: setPanel(mainPanel); break;
				case 1: setPanel(constructionPanel); break;
				case 2: setPanel(commercePanel); break;
				case 3: setPanel(carteDevPanel); break;
				default: setPanel(mainPanel); break;
				}
			}
		});
		continuer.setText("Continuer");
		continuer.setBounds(280,180,150,20);
		erreurPanel.add(continuer);
		//MettreEnPlacePanel pour mettre en place les colonies et les routes de chaque joueur
		JButton commencer = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				if(!secondTour) {
					if(cpt < Lanceur.getNombreJoueurs()-1) {
						cpt++;
						jeuView.getPlateau().placerColonieSansRoute(1);
						placePanel("Placer votre premiére Colonie.");
						timer = new Timer(INTERVAL,
								new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if(jeuView.getPlateau().getEtat()==5) {
								}
								else {
									timer.stop();
									jeuView.getPlateau().placerRoute(1);
									placePanel("Placer une route");
									timer = new Timer(INTERVAL,
											new ActionListener() {
										public void actionPerformed(ActionEvent evt) {
											if(jeuView.getPlateau().getEtat()==3) {
											}
											else{
												timer.stop();
												//passer au joueur suivant
												Lanceur.joueurSuivant();
												setJoueurCourant(Lanceur.getJoueurCourant());
												mettreEnPlacePanel();
											}
										}
									});
									timer.start();
								}
							}
						});
						timer.start();
						
					}
					else {
						secondTour=true;
						cpt++;
						jeuView.getPlateau().placerColonieSansRoute(1);
						placePanel("Placer votre premiére Colonie.");
						timer = new Timer(INTERVAL,
								new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if(jeuView.getPlateau().getEtat()==5) {
								}
								else {
									timer.stop();
									jeuView.getPlateau().placerRoute(1);
									placePanel("Placer une route");
									timer = new Timer(INTERVAL,
											new ActionListener() {
										public void actionPerformed(ActionEvent evt) {
											if(jeuView.getPlateau().getEtat()==3) {
											}
											else {
												timer.stop();
												mettreEnPlacePanel();
											}
										}
									});
									timer.start();
								}
							}
						});
						timer.start();
					}
				}
				//dans le second tour on fait la répartition des ressources
				else {
					if(cpt>1) {
						cpt--;
						jeuView.getPlateau().placerInSecoondTour();
						placePanel("Placer votre deuxième Colonie.");
						timer = new Timer(INTERVAL,
								new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if(jeuView.getPlateau().getEtat()!=5) {
									timer.stop();
									jeuView.getPlateau().placerRoute(1);
									placePanel("Placer une route");
									timer = new Timer(INTERVAL,
											new ActionListener() {
										public void actionPerformed(ActionEvent evt) {
											if(jeuView.getPlateau().getEtat()!=3) {
												timer.stop();
												//passer au joueur precedant
												Lanceur.joueurPrecedant();
												setJoueurCourant(Lanceur.getJoueurCourant());
												mettreEnPlacePanel();
												
											}
										}
									});
									timer.start();
								}
							}
						});
						timer.start();
						
					}
					else {
						cpt--;
						jeuView.getPlateau().placerInSecoondTour();
						placePanel("Placer votre deuxième Colonie.");
						timer = new Timer(INTERVAL,
								new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if(jeuView.getPlateau().getEtat()!=5) {
									timer.stop();
									jeuView.getPlateau().placerRoute(1);
									placePanel("Placer une route");
									timer = new Timer(INTERVAL,
											new ActionListener() {
										public void actionPerformed(ActionEvent evt) {
											if(jeuView.getPlateau().getEtat()!=3) {
												timer.stop();
												lancerDeePanel();
												
											}
										}
									});
									timer.start();
								}
							}
						});
						timer.start();
					}
				}
			}
		});
		commencer.setText("Jouer");
		commencer.setBounds(280,130,150,20);
		mettreEnPlacePanel.add(commencer);
		//lancement du dée
		JButton deeButton = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				Jeu jeu = jeuView.getPlateau().getJeu();
				//Si le jeu est fini j'affiche le joueur gagnant
				int dee = jeu.lanceeDes();
				if(dee != 7) {
					mainPanel();
				}
				else {

					if (Lanceur.getNombreJoueurs() == 3) {
						int remove = 0;
						if (Lanceur.getJoueur(0).NombreTotatlDeRessources() > 7)
							remove = Lanceur.getJoueur(0).NombreTotatlDeRessources() / 2;
						inputResourcesPanel(remove, Lanceur.getJoueur(0), "Supprimer " + remove + " ressources", false);
						timer = new Timer(INTERVAL,
								new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if (soumission) {
									timer.stop();
									Lanceur.getJoueur(0).EnleverRessources(inputRessources);
									int remove = 0;
									if (Lanceur.getJoueur(1).NombreTotatlDeRessources() > 7)
										remove = Lanceur.getJoueur(1).NombreTotatlDeRessources()/ 2;
									inputResourcesPanel(remove, Lanceur.getJoueur(1), "Supprimer " + remove + " ressources", false);
									timer = new Timer(INTERVAL,
											new ActionListener() {
										public void actionPerformed(ActionEvent evt) {
											if (soumission) {
												timer.stop();
												Lanceur.getJoueur(1).EnleverRessources(inputRessources);
												int remove = 0;
												if (Lanceur.getJoueur(2).NombreTotatlDeRessources() > 7)
													remove = Lanceur.getJoueur(2).NombreTotatlDeRessources()/ 2;
												inputResourcesPanel(remove, Lanceur.getJoueur(2), "Supprimer " + remove + " ressources", false);
												timer = new Timer(INTERVAL,
														new ActionListener() {
													public void actionPerformed(ActionEvent evt) {
														if (soumission) {
															timer.stop();
															Lanceur.getJoueur(2).EnleverRessources(inputRessources);
															jeuView.getPlateau().placerVoleur();
															placePanel("Déplacer le voleur");
															timer = new Timer(INTERVAL,
																	new ActionListener() {
																public void actionPerformed(ActionEvent evt) {
																	if(jeuView.getPlateau().getEtat() == 1){
																	}
																	else {
																		timer.stop();
																		voleurPanel();
																	}
																}
															});
															timer.start();
														}
													}
												});
												timer.start();
											}
										}
									});
									timer.start();
								}
							}
						});
						timer.start();
					}
					else {
						int remove = 0;
						if (Lanceur.getJoueur(0).NombreTotatlDeRessources() > 7)
							remove = Lanceur.getJoueur(0).NombreTotatlDeRessources() / 2;
						inputResourcesPanel(remove, Lanceur.getJoueur(0), "Supprimer " + remove + " ressources", false);
						timer = new Timer(INTERVAL,
								new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								if (soumission) {
									timer.stop();
									Lanceur.getJoueur(0).EnleverRessources(inputRessources);
									int remove = 0;
									if (Lanceur.getJoueur(1).NombreTotatlDeRessources() > 7)
										remove = Lanceur.getJoueur(1).NombreTotatlDeRessources()/ 2;
									inputResourcesPanel(remove, Lanceur.getJoueur(1), "Supprimer " + remove + " ressources", false);
									timer = new Timer(INTERVAL,
											new ActionListener() {
										public void actionPerformed(ActionEvent evt) {
											if (soumission) {
												timer.stop();
												Lanceur.getJoueur(1).EnleverRessources(inputRessources);
												int remove = 0;
												if (Lanceur.getJoueur(2).NombreTotatlDeRessources() > 7)
													remove = Lanceur.getJoueur(2).NombreTotatlDeRessources()/ 2;
												inputResourcesPanel(remove, Lanceur.getJoueur(2), "Supprimer " + remove + " ressources", false);
												timer = new Timer(INTERVAL,
														new ActionListener() {
													public void actionPerformed(ActionEvent evt) {
														if (soumission) {
															timer.stop();
															Lanceur.getJoueur(2).EnleverRessources(inputRessources);	
															int remove=0;
															if (Lanceur.getJoueur(3).NombreTotatlDeRessources() > 7)
																remove = Lanceur.getJoueur(3).NombreTotatlDeRessources()/ 2;
															inputResourcesPanel(remove, Lanceur.getJoueur(3), "Supprimer " + remove + " ressources", false);
															timer = new Timer(INTERVAL,
																	new ActionListener() {
																public void actionPerformed(ActionEvent evt) {
																	if (soumission) {
																		timer.stop();
																		Lanceur.getJoueur(3).EnleverRessources(inputRessources);	
																		jeuView.getPlateau().placerVoleur();
																		placePanel("Déplacer le voleur");
																		timer = new Timer(INTERVAL,
																				new ActionListener() {
																			public void actionPerformed(ActionEvent evt) {
																				if(jeuView.getPlateau().getEtat() == 1){
																				}
																				else {
																					timer.stop();
																					voleurPanel();
																				}
																			}
																		});
																		timer.start();
																	}
																}
															});
															timer.start();
														}
													}
												});
												timer.start();
											}
										}
									});
									timer.start();
								}
							}
						});
						timer.start();
					}
				}
				JLabel nombreDee = new JLabel ("La valeur du dée est : "+dee);
				nombreDee.setBounds(270,50,200,20);
				add(nombreDee);
				repaint();
				validate();
			}});
		deeButton.setText("Lancer le dée");
		deeButton.setBounds(280,130,150,20);
		lancerDeePanel.add(deeButton);
		//mainPanel
		//Construction
		JButton construction = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				constructionPanel();
				JLabel label = new JLabel ("Construction");
				label.setBounds(300,50,200,20);
				add(label);
				repaint();
				validate();
			}
		});
		construction.setText("Construction");
		construction.setBounds(280,100,150,20);
		mainPanel.add(construction);
		//route
		JButton route = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				Jeu jeu = jeuView.getPlateau().getJeu();
				try {
					jeu.AcheterRoute(Lanceur.getJoueurCourant());
					jeuView.getPlateau().placerRoute(1);
					achatRoutePanel("Placer votre route");
					timer = new Timer(INTERVAL,
							new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							if(jeuView.getPlateau().getEtat() == 3){

							}
							else {
								constructionPanel();
								timer.stop();
							}
						}
					});
					timer.start();
				}catch(InsufficientResourcesException e) {
					erreurPanel("Vous n'avez pas assez de ressources !!!");
				}
				catch(CapacityReachedException e) {
					erreurPanel("Vous avez deja construit 15 routes !!!");
			    }
				
			}
		});
		route.setText("Route");
		route.setBounds(300,100,100,20);
		constructionPanel.add(route);
		//colonie
		JButton colonie = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				Jeu jeu = jeuView.getPlateau().getJeu();
				try {
					jeu.AcheterColonie(Lanceur.getJoueurCourant());
					jeuView.getPlateau().placerColonie(1);
					achatColoniePanel("Placer votre colonie");
					timer = new Timer(INTERVAL,
							new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							if(jeuView.getPlateau().getEtat() == 2){

							}
							else {
								constructionPanel();
								timer.stop();
							}
						}
					});
					timer.start();
				}catch(InsufficientResourcesException e) {
					erreurPanel("Vous n'avez pas assez de ressources !!!");
				}
				catch(CapacityReachedException e) {
					erreurPanel("Vous avez deja construit 5 colonies !!!");
			    }
			}
		});
		colonie.setText("Colonie");
		colonie.setBounds(300,160,100,20);
		constructionPanel.add(colonie);
		//ville
		JButton ville = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				Jeu jeu = jeuView.getPlateau().getJeu();
				try {
					jeu.AcheterVille(Lanceur.getJoueurCourant());
					jeuView.getPlateau().placerVille(1);
					achatVillePanel("Placer votre ville");
					timer = new Timer(INTERVAL,
							new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							if(jeuView.getPlateau().getEtat() == 4){

							}
							else {
								constructionPanel();
								timer.stop();
							}
						}
					});
					timer.start();
				}catch(InsufficientResourcesException e) {
					erreurPanel("Vous n'avez pas assez de ressources !!!");
				}
				catch(CapacityReachedException e) {
					erreurPanel("Vous avez deja construit 4 villes !!!");
			    }
			}
		});
		ville.setText("Ville");
		ville.setBounds(300,220,100,20);
		constructionPanel.add(ville);
		//Commerce
		JButton commerce = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				commercePanel();
			}
		});
		commerce.setText("Commerce");
		commerce.setBounds(280,140,150,20);
		mainPanel.add(commerce);
		//commerce interne
		JButton interne = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				choixJoueurPanel();
			}
		});
		interne.setText("Commerce Interne");
		interne.setBounds(255,100,200,20);
		commercePanel.add(interne);
		//création d'une liste déroulante pour les joueurs
		JComboBox<Joueur> joueurBox = new JComboBox<Joueur>();
		joueurBox.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				JComboBox<Joueur> cb = (JComboBox)a.getSource();
				joueurChoisi = (Joueur)cb.getSelectedItem();
				//System.out.println(tradeChoice);
				inputResourcesPanel(-1, Lanceur.getJoueurCourant(), "Ressources à vendre", false);
				timer = new Timer(INTERVAL,
						new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						if(soumission){
							timer.stop();
							ressourcesAVendre = inputRessources;
							inputResourcesPanel(-1, joueurChoisi, "Ressources à acheter", false);
							timer = new Timer(INTERVAL,
									new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									if(soumission){
										timer.stop();
										ressourcesAAcheter = inputRessources;
										jeuView.getPlateau().getJeu().commerceInterne(Lanceur.getJoueurCourant(), joueurChoisi, ressourcesAVendre, ressourcesAAcheter);
										mainPanel();
									}
								}
							});
							timer.start();
						}
					}
				});
				timer.start();
			}
	    });
		joueurBox.setBounds(280,90,150,20);
		choixJoueurPanel.add(joueurBox);
		//Carte Developpement
		JButton carteDev = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				carteDevPanel();
			}
		});
		
		carteDev.setText("Carte Dev");
		carteDev.setBounds(280,180,150,20);
		mainPanel.add(carteDev);
		//CarteDevPanel
		JButton acheterCarteDev = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
					Jeu jeu = jeuView.getPlateau().getJeu();
					try {
						jeu.AcheterCarteDev(Lanceur.getJoueurCourant());
						carteDevPanel();
					}catch(InsufficientResourcesException e) {
						erreurPanel("Vous n'avez pas assez de ressources !!!");
					}
					catch(CapacityReachedException e) {
						erreurPanel("La pioche est vide!!!");
				    }
				}
		});
		acheterCarteDev.setText("Acheter une carte dev");
		acheterCarteDev.setBounds(255,100,200,20);
		carteDevPanel.add(acheterCarteDev);
		JButton jouerCarteDev = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				jouerCarteDevPanel();
			}
		});
		
		jouerCarteDev.setText("Jouer une carte dev");
		jouerCarteDev.setBounds(255,150,200,20);
		carteDevPanel.add(jouerCarteDev);
		//jouer carte dev panel
		JButton consRouteButton = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				Jeu jeu = jeuView.getPlateau().getJeu();
				if(Lanceur.getJoueurCourant().aCarteDev("CONSTRUCTION DE ROUTES")) {
					Lanceur.getJoueurCourant().removeCard("CONSTRUCTION DE ROUTES");
					
					jeuView.getPlateau().placerRoute(2);
					placePanel("Placer les 2 routes");
					
					timer = new Timer(INTERVAL,
							new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							if(jeuView.getPlateau().getEtat() == 3){

							}
							else {
								mainPanel();
								timer.stop();
							}
						}
					});
					timer.start();
				}
				else {
					erreurPanel("Vous n'avez pas cette carte");
				}
			}
		});
		consRouteButton.setText("Construction de routes");
		consRouteButton.setBounds(255,100,200,30);
		jouerCarteDevPanel.add(consRouteButton);
		JButton invention = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				Jeu jeu = jeuView.getPlateau().getJeu();
				if(Lanceur.getJoueurCourant().aCarteDev("INVENTION")) {
					Lanceur.getJoueurCourant().removeCard("INVENTION");
					ressourcesPanel("Vous voulez quelles ressources ?");
					inputResourcesPanel(2,Lanceur.getJoueurCourant(),"Choisissez 2 ressources",true);
					timer = new Timer(INTERVAL,
							new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							if(soumission) {
								timer.stop();
								Lanceur.getJoueurCourant().RecevoirRessources(inputRessources);
								mainPanel();
							}
						}
					});
					timer.start();
				}
				else {
					erreurPanel("Vous n'avez pas cette carte");
				}
			}
		});
		invention.setText("Invention");
		invention.setBounds(255,140,200,30);
		jouerCarteDevPanel.add(invention);
		JButton monopole = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				Jeu jeu = jeuView.getPlateau().getJeu();
				if(Lanceur.getJoueurCourant().aCarteDev("MONOPOLE")) {
					Lanceur.getJoueurCourant().removeCard("MONOPOLE");
					ressourcesPanel("Vous voulez quelles ressources ?");
					timer = new Timer(INTERVAL,
							new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							if(ressourceChoisie) {
								timer.stop();
								ressourceChoisie=false;
								Jeu jeu= jeuView.getPlateau().getJeu();
								jeu.PrendreToutMonopole(Lanceur.getJoueurCourant(),resSelectioner);
								mainPanel();
								
							}
							else {
								timer.stop();
								choixJoueurPanel();
							}
						}
					});
					timer.start();
				}
				else {
					erreurPanel("Vous n'avez pas cette carte");
				}
			}
		});
		monopole.setText("Monople");
		monopole.setBounds(255,180,200,30);
		jouerCarteDevPanel.add(monopole);
		JButton chevalier = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				Jeu jeu = jeuView.getPlateau().getJeu();
				if(Lanceur.getJoueurCourant().aCarteDev("CHEVALIER")) {
					Lanceur.getJoueurCourant().removeCard("CHEVALIER");
					Lanceur.getJoueurCourant().plusdeChevalier();
					jeuView.getPlateau().placerVoleur();
					placePanel("Déplacer le voleur");
					timer = new Timer(INTERVAL,
							new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							if(jeuView.getPlateau().getEtat() == 1) {
							}
							else {
								timer.stop();
								choixJoueurPanel();
							}
						}
					});
					timer.start();
				}
				else {
					erreurPanel("Vous n'avez pas cette carte");
				}
			}
		});
		chevalier.setText("Chevalier");
		chevalier.setBounds(255,220,200,30);
		jouerCarteDevPanel.add(chevalier);
		
		//retourner au main
		JButton returnMainDev = new JButton(new AbstractAction() {
					public void actionPerformed(ActionEvent a) {
						mainPanel();
					}
				});
		returnMainDev.setText("Retourner au main");
		returnMainDev.setBounds(270,260,170,20);
		carteDevPanel.add(returnMainDev);
		carteDevPanel.add(returnMainDev);
		//Fin de la partie
		JButton finPartie = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				Jeu jeu= jeuView.getPlateau().getJeu();
				//la plus grande armé
				if(jeu.finPartie()) {
					Lanceur.setGagnant(jeu.Gagnant());
					finPanel();
				}
				Lanceur.joueurSuivant();
				lancerDeePanel();
			}
		});
		finPartie.setText("Fin Partie");
		finPartie.setBounds(280,220,150,20);
		mainPanel.add(finPartie);
		
		//Input Ressources Panel
		JComboBox<Integer> bois = new JComboBox<Integer>();
		bois.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
			}
		});
		bois.setBounds(100,100,90,20);
		inputRessourcesPanel.add(bois);
		
		JComboBox<Integer> ble = new JComboBox<Integer>();
		ble.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
			}
		});
		ble.setBounds(230,100,90,20);
		inputRessourcesPanel.add(ble);
		
		JComboBox<Integer> minerai = new JComboBox<Integer>();
		minerai.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
			}
		});
		minerai.setBounds(360,100,90,20);
		inputRessourcesPanel.add(minerai);
		
		JComboBox<Integer> argile = new JComboBox<Integer>();
		argile.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
			}
		});
		argile.setBounds(150,200,90,20);
		inputRessourcesPanel.add(argile);
		
		JComboBox<Integer> laine = new JComboBox<Integer>();
		laine.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
			}
		});
		laine.setBounds(280,200,90,20);
		inputRessourcesPanel.add(laine);
		
		JLabel joueur = new JLabel("Joueur : ");
		joueur.setBounds(40,40,200,20);
		inputRessourcesPanel.add(joueur);
		
		JButton soumettre = new JButton();
		soumettre.setText("Soumettre");
		soumettre.setBounds(250,290,200,20);
		inputRessourcesPanel.add(soumettre);
		
		JLabel boisLabel = new JLabel("Bois");
		boisLabel.setBounds(100,70,90,20);
		inputRessourcesPanel.add(boisLabel);
		
		JLabel bleLabel = new JLabel("Blé");
		bleLabel.setBounds(230,70,90,20);
		inputRessourcesPanel.add(bleLabel);
		
		JLabel mineraiLabel = new JLabel("Minerai");
		mineraiLabel.setBounds(360,70,90,20);
		inputRessourcesPanel.add(mineraiLabel);
		
		JLabel argileLabel = new JLabel("Argile");
		argileLabel.setBounds(150,170,90,20);
		inputRessourcesPanel.add(argileLabel);
		
		JLabel laineLabel = new JLabel("Laine");
		laineLabel.setBounds(280,170,90,20);
		inputRessourcesPanel.add(laineLabel);
		
		JButton externe = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				ressourcesPanel("Quelle est la ressource que vous voulez sacrifier?");
				timer = new Timer(INTERVAL,
						new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						if(ressourceChoisie){
							timer.stop();
							ressourceChoisie = false;
							ressourceASacrifier  = resSelectioner;
							ressourcesPanel("Quelle est la ressource que vous voulez acheter?");
							timer = new Timer(INTERVAL,
									new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									if(ressourceChoisie){
										timer.stop();
										ressourceChoisie = false;
										ressourceAchetee= resSelectioner;
										confirmationPanel();
									}
								}
							});
							timer.start();
						}
					}
				});
				timer.start();
			}
		});
		externe.setText("Commerce Externe");
		externe.setBounds(255,150,200,20);
		commercePanel.add(externe);
		JButton boisButton = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				resSelectioner = "BOIS";
				ressourceChoisie = true;
			}
		});
		boisButton.setText("Bois");
		boisButton.setBounds(310,100,90,30);
		ressourcesPanel.add(boisButton);
		JButton bleButton = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				resSelectioner = "BLÉ";
				ressourceChoisie = true;
			}
		});
		bleButton.setText("Blé");
		bleButton.setBounds(310,140,90,30);
		ressourcesPanel.add(bleButton);
		JButton mineraiButton = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				resSelectioner = "MINERAI";
				ressourceChoisie = true;
			}
		});
		mineraiButton.setText("Minerai");
		mineraiButton.setBounds(310,180,90,30);
		ressourcesPanel.add(mineraiButton);
		JButton argileButton = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				resSelectioner = "ARGILE";
				ressourceChoisie = true;
			}
		});
		argileButton.setText("Argile");
		argileButton.setBounds(310,220,90,30);
		ressourcesPanel.add(argileButton);
		JButton laineButton = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				resSelectioner = "LAINE";
				ressourceChoisie = true;
			}
		});
		laineButton.setText("Laine");
		laineButton.setBounds(310,260,90,30);
		ressourcesPanel.add(laineButton);
		JLabel text = new JLabel("");
		text.setBounds(70,50,400,20);
		ressourcesPanel.add(text);
		
		//retourner au main
		JButton returnMain = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				mainPanel();
			}
		});
		returnMain.setText("Retourner au main");
		returnMain.setBounds(270,260,170,20);
		commercePanel.add(returnMain);
		constructionPanel.add(returnMain);
		jouerCarteDevPanel.add(returnMain);
		//confirmation Panel
		JLabel confLabel = new JLabel("");
		confLabel.setBounds(60,100,550,20);
		confirmationPanel.add(confLabel);
		
		JButton confirmer = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				int n= jeuView.getPlateau().getJeu().commerceExterne(Lanceur.getJoueurCourant(), ressourceASacrifier ,ressourceAchetee);
				if(n==0) {
					mainPanel();
				}else if(n==1) {
					erreurPanel("Vous n'avez pas assez de ressources !!");
				}
				
			}
		});
		confirmer.setText("Confirmer");
		confirmer.setBounds(220,200,110,30);
		confirmationPanel.add(confirmer);
		
		JButton refuser = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				mainPanel();
			}
		});
		refuser.setText("Refuser");
		refuser.setBounds(370,200,110,30);
		confirmationPanel.add(refuser);
		//Voleur Panel
		JComboBox<Joueur> joueursAVoler = new JComboBox<Joueur>();
		joueursAVoler.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				JComboBox<Joueur> cb = (JComboBox)a.getSource();
				Joueur j = (Joueur)cb.getSelectedItem();
				jeuView.getPlateau().getJeu().voler(Lanceur.getJoueurCourant(), j);
				mainPanel();
			}
		});
		joueursAVoler.setBounds(280,90,150,20);
		voleurPanel.add(joueursAVoler);
		//IntroductionJoueurPanel
		JLabel label=new JLabel("Entrer le nombre de joueurs");
		label.setBounds(260,60,210,30);
		introductionJoueursPanel.add(label);
		JTextField nbJoueurs = new JTextField (); 
		nbJoueurs.setBounds(310,100,100,30);
		introductionJoueursPanel.add(nbJoueurs);
		JButton valider = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				nombreJoueurs=Integer.valueOf(nbJoueurs.getText());
				if(nombreJoueurs==3 || nombreJoueurs==4) {
					typeJoueursPanel();
				}
			}
		});
		valider.setBounds(315,170,90,30);
		valider.setText("Valider");
		introductionJoueursPanel.add(valider);
		introductionJoueursPanel();
		//Type Joueur Panel
		JLabel label3=new JLabel("");
		label3.setBounds(260,60,210,30);
		typeJoueursPanel.add(label3);
		JButton humain = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				nombreJoueurs=Integer.valueOf(nbJoueurs.getText());
				ajoutJoueursPanel();
			}
		});
		 humain.setBounds(300,150,100,30);
		 humain.setText("Humain");
		typeJoueursPanel.add(humain);
		JButton IA = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				index++;
				Lanceur.ajouterJoueur(new IA());
				if(index==nombreJoueurs) {
					mettreEnPlacePanel();
					jeuView.getPlateau().setJeu(Lanceur.joueurs);
					Lanceur.setNombreJoueurs(nombreJoueurs);
					jeuView.joueurBar.setJoueurBar();
				}
				else {
					typeJoueursPanel();
				}
			}
		});
		IA.setBounds(300,200,100,30);
		IA.setText("IA");
		typeJoueursPanel.add(IA);
		//AjoutJoueurPanel
		JLabel label2=new JLabel("");
		label2.setBounds(260,60,210,30);
		ajoutJoueursPanel.add(label2);
		JTextField nomJoueur = new JTextField (); 
		nomJoueur.setBounds(310,100,100,30);
		ajoutJoueursPanel.add(nomJoueur);
		JButton valider2 = new JButton(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				index++;
				Lanceur.ajouterJoueur(new Joueur(nomJoueur.getText()));
				if(index==nombreJoueurs) {
					mettreEnPlacePanel();
					jeuView.getPlateau().setJeu(Lanceur.joueurs);
					Lanceur.setNombreJoueurs(nombreJoueurs);
					jeuView.joueurBar.setJoueurBar();
				}
				else {
					typeJoueursPanel();
				}
			}
		});
		valider2.setBounds(315,170,90,30);
		valider2.setText("Valider");
		ajoutJoueursPanel.add(valider2);
		
		
		introductionJoueursPanel();
		//mainPanel();
		
	}
	public void setJoueurCourant(Joueur j) {
		JLabel label = (JLabel) joueurCourant;
		label.setText("Au tour de "+j.getNom());
		label.setOpaque(true);
		label.setBackground(j.getCouleur());
	}
	public void placePanel(String msg) {
		((JLabel)placePanel.get(0)).setText(msg);
		setPanel(placePanel);
	}
	public void achatRoutePanel(String msg) {
		((JLabel)achatRoutePanel.get(0)).setText(msg);
		setPanel(achatRoutePanel);
	}
	public void achatColoniePanel(String msg) {
		((JLabel)achatColoniePanel.get(0)).setText(msg);
		setPanel(achatColoniePanel);
	}
	public void achatVillePanel(String msg) {
		((JLabel)achatVillePanel.get(0)).setText(msg);
		setPanel(achatVillePanel);
	}
	public void mettreEnPlacePanel() {
		/*if(Test.getJoueurCourant() instanceof IA) {
			placerColonieRouteTour12(secondTour);
			cpt++;
			if(cpt < Test.getNombreJoueurs()-1) {
				cpt++;
				Test.joueurSuivant();
			}
		}*/
		setPanel(mettreEnPlacePanel);
	}
	public void lancerDeePanel() {
		setJoueurCourant(Lanceur.getJoueurCourant());
		setPanel(lancerDeePanel);
		flag = 0;
	}
	public void mainPanel() {
		setPanel(mainPanel);
		flag=0;
	}
	public void constructionPanel() {
		setPanel(constructionPanel);
		flag=1;
	}
	public void erreurPanel(String s) {
		((JLabel) erreurPanel.get(0)).setText(s);
		setPanel(erreurPanel);
	}
	public void commercePanel() {
		setPanel(commercePanel);
		flag=2;
	}
	public void carteDevPanel() {
		setPanel(carteDevPanel);
		flag=3;
	}
	public void achatCarteDevPanel() {
		setPanel(achatCarteDevPanel);
	}
	public void jouerCarteDevPanel() {
		setPanel(jouerCarteDevPanel);
	}
	public void introductionJoueursPanel() {
		setPanel(introductionJoueursPanel);
	}
	public void typeJoueursPanel() {
		((JLabel)typeJoueursPanel.get(0)).setText(("Choisir le type du joueur "+(index+1)));
		setPanel(typeJoueursPanel);
	}
	public void ajoutJoueursPanel() {
		((JLabel)ajoutJoueursPanel.get(0)).setText(("Choisir le nom du joueur "+(index+1)));
		((JTextField)ajoutJoueursPanel.get(1)).setText((""));
		setPanel(ajoutJoueursPanel);
	}
	public void choixJoueurPanel() {
		AbstractAction action = (AbstractAction) ((JComboBox<Joueur>) choixJoueurPanel.get(0)).getAction();
		((JComboBox<Joueur>) choixJoueurPanel.get(0)).setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				
			}
		});
		((JComboBox<Joueur>) choixJoueurPanel.get(0)).removeAllItems();
		// Populate combo box
		for (int i = 0; i < Lanceur.getNombreJoueurs(); i++) {
			if (!Lanceur.getJoueur(i).equals(Lanceur.getJoueurCourant())) {
				((JComboBox<Joueur>) choixJoueurPanel.get(0)).addItem(Lanceur.getJoueur(i));
			}
		}
		((JComboBox<Joueur>) choixJoueurPanel.get(0)).setAction(action);
		setPanel(choixJoueurPanel);
	}
	public void inputResourcesPanel(int n, Joueur j, String s, boolean invention) {
		soumission = false;
		for (int i = 0; i < 5; i++) {
			((JComboBox<Integer>) inputRessourcesPanel.get(i)).removeAllItems();
		}
		if(invention) {
			for (int r = 0; r <= 2; r++) {
				((JComboBox<Integer>) inputRessourcesPanel.get(0)).addItem(Integer.valueOf(r));
			}
			for (int r = 0; r <= 2; r++) {
				((JComboBox<Integer>) inputRessourcesPanel.get(1)).addItem(Integer.valueOf(r));
			}
			for (int r = 0; r <= 2; r++) {
				((JComboBox<Integer>) inputRessourcesPanel.get(2)).addItem(Integer.valueOf(r));
			}
			for (int r = 0; r <= 2; r++) {
				((JComboBox<Integer>) inputRessourcesPanel.get(3)).addItem(Integer.valueOf(r));
			}
			for (int r = 0; r <= 2; r++) {
				((JComboBox<Integer>) inputRessourcesPanel.get(4)).addItem(Integer.valueOf(r));
			}
		}
		else {
			for (int r = 0; r <= j.getNbrRessource("BOIS"); r++) {
				((JComboBox<Integer>) inputRessourcesPanel.get(0)).addItem(Integer.valueOf(r));
			}
			for (int r = 0; r <= j.getNbrRessource("BLÉ"); r++) {
				((JComboBox<Integer>) inputRessourcesPanel.get(1)).addItem(Integer.valueOf(r));
			}
			for (int r = 0; r <= j.getNbrRessource("MINERAI"); r++) {
				((JComboBox<Integer>) inputRessourcesPanel.get(2)).addItem(Integer.valueOf(r));
			}
			for (int r = 0; r <= j.getNbrRessource("ARGILE"); r++) {
				((JComboBox<Integer>) inputRessourcesPanel.get(3)).addItem(Integer.valueOf(r));
			}
			for (int r = 0; r <= j.getNbrRessource("LAINE"); r++) {
				((JComboBox<Integer>) inputRessourcesPanel.get(4)).addItem(Integer.valueOf(r));
			}
		}
		
		((JLabel) inputRessourcesPanel.get(5)).setText("Joueur : " + j.getNom());

		((JButton) inputRessourcesPanel.get(6)).setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				int sum = 0;
				for (int i = 0; i < 5; i++) {
					sum += ((JComboBox<Integer>) inputRessourcesPanel.get(i)).getSelectedIndex();
				}
				if (n != -1) {
					if (sum != n) {
						return;
					}
				}
				ArrayList<String> output = new ArrayList<String>();

				for (int i = 0; i < ((JComboBox<Integer>) inputRessourcesPanel.get(0)).getSelectedIndex(); i++) {
					output.add("BOIS");
				}
				for (int i = 0; i < ((JComboBox<Integer>) inputRessourcesPanel.get(1)).getSelectedIndex(); i++) {
					output.add("BLÉ");
				}
				for (int i = 0; i < ((JComboBox<Integer>) inputRessourcesPanel.get(2)).getSelectedIndex(); i++) {
					output.add("MINERAI");
				}
				for (int i = 0; i < ((JComboBox<Integer>) inputRessourcesPanel.get(3)).getSelectedIndex(); i++) {
					output.add("ARGILE");
				}
				for (int i = 0; i < ((JComboBox<Integer>) inputRessourcesPanel.get(4)).getSelectedIndex(); i++) {
					output.add("LAINE");
				}

				inputRessources = output;
				soumission = true;
				

			}
		});

		((JButton) inputRessourcesPanel.get(6)).setText(s);

		setPanel(inputRessourcesPanel);
	}
	public void ressourcesPanel(String s) {
		((JLabel) ressourcesPanel.get(5)).setText(s);
		setPanel(ressourcesPanel);
	}
	public void confirmationPanel() {
		int port=-1;
		switch (ressourceAchetee) {
			case "ARGILE": port=1;break;
			case "LAINE": port=2;break;
			case "MINERAI": port=3;break;
			case "BLÉ": port=4;break;
			case "BOIS": port=5;break;
			default:break;
		}
		boolean []ports=Lanceur.getJoueurCourant().getPorts();
		if(port!=-1 && ports[port]) {
			//2
			((JLabel) confirmationPanel.get(0)).setText("Vous allez perdre 2 ressources de type "+ressourceASacrifier+" ( VOUS AVEZ UN PORT '"+ressourceASacrifier+"' )");
		}else if(ports[0]) {
			//3
			((JLabel) confirmationPanel.get(0)).setText("Vous allez perdre 3 ressources de type "+ressourceASacrifier+" ( VOUS AVEZ UN GÉNÉRAL )");
		}else {
			//4
			((JLabel) confirmationPanel.get(0)).setText("Vous allez perdre 4 ressources de type "+ressourceASacrifier+" ( VOUS N'AVEZ PAS DE PORT )");
		}
		setPanel(confirmationPanel);
	}
	public void voleurPanel() {
		//JComboBox<Player> newBox = new JComboBox<Player>();
		AbstractAction action = (AbstractAction) ((JComboBox<Joueur>) voleurPanel.get(0)).getAction();
		((JComboBox<Joueur>) voleurPanel.get(0)).setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent a) {
				
			}
		});
		((JComboBox<Joueur>) voleurPanel.get(0)).removeAllItems();
		Location loc = jeuView.getPlateau().getJeu().getPlateau().getVoleurLocation();
		for (int i = 0; i < jeuView.getPlateau().getJeu().TousLesJoueursAutourDeTuile(loc).size(); i++) {
			if (!jeuView.getPlateau().getJeu().TousLesJoueursAutourDeTuile(loc).get(i).equals(Lanceur.getJoueurCourant())) {
				((JComboBox<Joueur>) voleurPanel.get(0)).addItem(jeuView.getPlateau().getJeu().TousLesJoueursAutourDeTuile(loc).get(i));
			}
		}

		((JComboBox<Joueur>) voleurPanel.get(0)).setAction(action);

		if (((JComboBox<Joueur>) voleurPanel.get(0)).getItemCount() <= 0) {
			erreurPanel("Il y a aucun joueur à voler");
		}
		else {
			setPanel(voleurPanel);
		}
	}
	private void setPanel(ArrayList<JComponent> panel) {
		this.removeAll();
		if(Lanceur.getNombreJoueurs()==index && index!=0) {
			setJoueurCourant(Lanceur.getJoueurCourant());
			this.add(joueurCourant);
		}
		for(int i=0 ; i<panel.size(); i++) {
			this.add(panel.get(i));
		}
		
		repaint();
		validate();
	}
	public void finPanel() {
		this.removeAll();
		JLabel fin = new JLabel(Lanceur.getGagnant().getNom()+"a gagné !");
		fin.setBounds(280,220,150,20);
		add(fin);
	}
	public void placerColonieRouteTour12(Boolean secondTour) {
		Random rand = new Random(); 
		int i = rand.nextInt(5) + 1;
		int j = rand.nextInt(5) + 1;
		Jeu jeu=jeuView.getPlateau().getJeu();
		Joueur joueur = Lanceur.getJoueurCourant();
		while(!jeu.getPlateau().ajouterConstructionSansRoute(new Location(i,j), joueur)) {
			i = rand.nextInt(5) + 1;
			j = rand.nextInt(5) + 1;
		}
		if(secondTour) {
			ArrayList<Tuile> tuile = jeu.getPlateau().getAdjacenteTuiles(new Location(i,j));
			for(Tuile t : tuile) {
				if(t != null) {
					joueur.RecevoirRessources(t.nomRessouce(),1);
				}
			}
		}
		int  x = rand.nextInt(2) ;
		int y;
		if(x==1) {
			y=0;
		}
		else {
			y = rand.nextInt(2);
		}
		int o;
		if(x==0 && y==0) {
		  o = rand.nextInt(2) ;
		}
		else {
			if(x==1) {
				o=0;
			}
			else {
				o=1;
			}
		}
		while(!jeu.getPlateau().ajouterRoute(new AreteLocation(i-x,j-y,0), joueur)) {
			x = rand.nextInt(2) ;
			if(x==1) {
				y=0;
			}
			else {
				y = rand.nextInt(2);
			}
			if(x==0 && y==0) {
			  o = rand.nextInt(2) ;
			}
			else {
				if(x==1) {
					o=0;
				}
				else {
					o=1;
				}
			}
		}
		
	}
	public void jouerTour() {
		Jeu jeu=jeuView.getPlateau().getJeu();
		Joueur joueur = Lanceur.getJoueurCourant();
		//Lancement du dée
		int dee = jeu.lanceeDes();
		if(dee!=7) {
			Random rand = new Random(); 
			int n = rand.nextInt(3) ;
			/*Si n=0 le joueur va construire soit une route ou bine une colonie ou bien une ville
			 *Si n=1 le joueur va faire soit le commerce inetrne ou bien externe
			 *Si n=2 le joueur va acheter ou jouer une carte de developpement 
			 * */
			switch(n) {
			case 0:construire(jeu,joueur);break;
			case 1://ConstruireVille(j,sc);break;
			case 2://ConstruireRoute(j,sc,true);break;
			}
		}
	}
	public void construire(Jeu jeu , Joueur j) {
		Random rand = new Random(); 
		int n = rand.nextInt(3) ;
		/*n=0 pour construire une route
		 *n=1 pour construire une colonie
		 *n=2 pour construire une ville
		 * */
		switch(n) {
		case 0:construireRoute(jeu,j,true);break;
		case 1:construireColonie(jeu,j);break;
		case 2:construireVille(jeu,j);break;
		}
	}
	public void construireRoute(Jeu jeu,Joueur j,Boolean enAchetant) {
		Random rand = new Random(); 
		int x = rand.nextInt(5)+1 ;
		int y = rand.nextInt(5)+1 ;
		int o = rand.nextInt(2) ;
		AreteLocation loc = new AreteLocation(x,y,o);
		try{
			if(!jeu.construireRoute(j, loc,enAchetant)) {
				j.annulerAcheterRoute();
			}
		}catch(InsufficientResourcesException e) {
			System.out.println("Vous n'avez pas assez de ressources !!!");
		}
		catch(CapacityReachedException e) {
			System.out.println("Vous avez déja 15 routes !!!");
		}
	}
	public void construireColonie(Jeu jeu,Joueur j) {
		Random rand = new Random(); 
		int x = rand.nextInt(5)+1 ;
		int y = rand.nextInt(5)+1 ;
		Location loc = new Location(x,y);
		try{
			if(!jeu.construireColonie(j, loc)) {
				j.annulerAcheterColonie();
			}
		}catch(InsufficientResourcesException e) {
			System.out.println("Vous n'avez pas assez de ressources !!!");
		}
		catch(CapacityReachedException e) {
			System.out.println("Vous avez déja 5 colonies !!!");
		}
	}
	public void construireVille(Jeu jeu,Joueur j) {
		Random rand = new Random(); 
		int x = rand.nextInt(5)+1 ;
		int y = rand.nextInt(5)+1 ;
		Location loc = new Location(x,y);
		try{
			if(!jeu.construireVille(j, loc)) {
				j.annulerAcheterVille();
			}
		}catch(InsufficientResourcesException e) {
			System.out.println("Vous n'avez pas assez de ressources !!!");
		}
		catch(CapacityReachedException e) {
			System.out.println("Vous avez déja 4 villes !!!");
		}
	}
}