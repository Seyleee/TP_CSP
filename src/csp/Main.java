package csp;

import java.util.Scanner;

public class Main {

	static GenerateurCSP csp = new GenerateurCSP();
	
	public static void main(String[] args) {
		int nbrNoeud;
		int tailleDomaine;
		double densite;
		double durete;
		
		System.out.println(" Entrer le nombre de noeuds souhaité en entier ! ");
		Scanner sc = new Scanner(System.in);
		nbrNoeud = sc.nextInt();
		
		System.out.println(" Donner la taille du domaine en entier ! ");
		tailleDomaine = sc.nextInt();
		
		System.out.println("Entrer la densité entre 0 et 1 ! ");
		densite = sc.nextDouble();
		
		System.out.println("Dureté entre 0 et 1 ! ");
		durete = sc.nextDouble();
		sc.close();
	
		System.out.println(" Votre CSP est : --> ");
		csp.genererCSP(nbrNoeud, tailleDomaine, densite, durete);
		csp.AfficherCSP();

		if(csp.checkCSP()){
			long dureeBT = csp.backtracking();
			double backtracking = (double) dureeBT / 1000000.0;
			long dureeBJ = csp.backjumping();
			double backjumping = (double) dureeBJ / 1000000.0;
			//long dureeFC = csp.forwardChecking();
			//double forwardcheking = (double) dureeFC / 1000000.0;
			
			System.out.println("Durée d'exécution du backtracking : " + backtracking + " ms");
			//System.out.println("Durée d'exécution du backjumping : " + forwardcheking + " ms");
			System.out.println("Durée d'exécution du backjumping : " + backjumping + " ms");
		} else
			System.out.println("UNSAT");
		}

}
