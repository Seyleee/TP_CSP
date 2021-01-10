package csp;

import java.util.ArrayList;
import java.util.Random;

public class GenerateurCSP {
	
	public ArrayList<Noeud> listeNoeuds; 
	public ArrayList<Arc> listeArcs;
	public ArrayList<ArrayList<Arc>> listeAdjacenteEnEntree;
	public ArrayList<ArrayList<Arc>> listeAdjacenteEnSortie;
	public ArrayList<ArrayList<Integer>> listeDomaines;
	
	public GenerateurCSP () {
		listeNoeuds = new ArrayList<>();
		listeArcs = new ArrayList<>();
		listeAdjacenteEnEntree = new ArrayList<>();
		listeAdjacenteEnSortie = new ArrayList<>();
		listeDomaines = new ArrayList<>();
	}
	
	public void genererCSP(int nbrNoeud, int tailleDomaine, double densite, double durete) {
		
		int nbrArc;
		int nbrMaximumArc;
		int nbrContraintes;
		int nbrMaximumContraintes;
		double densiteActuelle;
		double dureteActuelle;
		ArrayList<Integer> domaines = new ArrayList<>();
		
		// Génération des noeuds et des domaines aléatoirement
		for(int i = 0; i < nbrNoeud; i++) {
			for(int j = 0; j < tailleDomaine; j++) {
				domaines.add(new Random().nextInt(100));
			}
			this.listeNoeuds.add(new Noeud(i, domaines));
			domaines.clear();
		}
		
		// Génération de l'ensemble des arcs
		for(int i = 0; i < this.listeNoeuds.size(); i++) {
			Noeud noeud = this.listeNoeuds.get(i);
			for (Noeud listeNoeud : this.listeNoeuds) {
				if (noeud.getIdNoeud() != listeNoeud.getIdNoeud()) {
					Arc arc = new Arc(noeud, listeNoeud);
					this.listeArcs.add(arc);
				}
			}
		}
		
		// Génération des contraintes
		for (Arc listeArc : this.listeArcs) {
			listeArc.genererListeContraintes();
		}
		
		// Atteindre la densité souhaitée
		nbrMaximumArc = this.listeArcs.size();
		nbrArc = nbrMaximumArc;
		densiteActuelle = nbrArc / nbrMaximumArc ;
		while (densiteActuelle > densite) {
			this.listeArcs.remove(new Random().nextInt(this.listeArcs.size()));
			nbrArc = this.listeArcs.size();
			densiteActuelle = nbrArc / nbrMaximumArc ;
		}
		
		// Atteindre la dureté saisie aléatoirement en réduisant le nombre de contrainte 
		nbrMaximumContraintes = this.listeArcs.get(0).getListeContraintes().size();
		for (Arc listeArc : this.listeArcs) {
			nbrContraintes = listeArc.getListeContraintes().size();
			dureteActuelle = nbrContraintes / nbrMaximumContraintes;

			while (dureteActuelle > durete) {
				listeArc.getListeContraintes().remove(new Random().nextInt(nbrContraintes));
				nbrContraintes = listeArc.getListeContraintes().size();
				dureteActuelle = nbrContraintes / nbrMaximumContraintes;
			}
		}
		RemplirListes();
	}
	
	//Remplir les listes adjacentes en entrée et en sortie avec les valeurs des noeuds contenues dans les arcs.
	public void RemplirListes() {
		
		for (int i = 0; i < this.listeNoeuds.size(); i++){
			ArrayList<Arc> arcEntree = new ArrayList<>();
			ArrayList<Arc> arcSortie = new ArrayList<>();

			for (Arc listeArc : listeArcs) {
				if (listeArc.getN1().getIdNoeud() == i)
					arcSortie.add(listeArc);
				else if (listeArc.getN2().getIdNoeud() == i)
					arcEntree.add(listeArc);
			}
			this.listeAdjacenteEnEntree.add(i, arcEntree);
			this.listeAdjacenteEnSortie.add(i, arcSortie);
		}
	}

	public void AfficherCSP () {
		System.out.println("\n Liste des noeuds générés avec leurs domaines --> \n");
		for (Noeud listeNoeud : this.listeNoeuds) {
			System.out.print(listeNoeud.toString() + " : ");
			listeNoeud.afficherDomaines();
		}

		System.out.println("\n Liste des arcs avec la densité souhaitée --> \n");
		for (Arc listeArc : this.listeArcs) {
			System.out.println(listeArc.toString() + " , ");
		}

		System.out.println("\n Liste des contraintes avec la dureté souhaitée --> \n");
		for (Arc listeArc : this.listeArcs) {
			listeArc.afficherListeContraintes();
		}
	}

	public boolean checkCSP () {
		boolean check = true;
		RemplirListeDomaine();
		for (int i = 0; i < this.listeDomaines.size(); i++)
			if (this.listeDomaines.get(i).isEmpty()){
				if (!this.listeAdjacenteEnEntree.get(i).isEmpty() || !this.listeAdjacenteEnSortie.get(i).isEmpty())
					return false;
				else {
					this.listeNoeuds.get(i).setValNoeud(this.listeNoeuds.get(i).getDomaine().get(new Random().nextInt(this.listeNoeuds.get(i).getDomaine().size())));
					this.listeDomaines.get(i).add(this.listeNoeuds.get(i).getValNoeud());
				}
			}
		FilterContraintes();
		return check;
	}

	//Cette méthode permet de filtrer les contraintes afin d’améliorer les capacités de résolution du CSP généré
	private void FilterContraintes() {
		int contrainte1, contrainte2, noeud;
		for (int i = 0; i < this.listeNoeuds.size(); i++) {
			for (int j = 0; j < this.listeAdjacenteEnSortie.get(i).size(); j++) {
				for (int k = 0 ; k < this.listeAdjacenteEnSortie.get(i).get(j).getListeContraintes().size(); k++) {
					contrainte1 = this.listeAdjacenteEnSortie.get(i).get(j).getListeContraintes().get(k).getContrainte1();
					noeud 	 = this.listeAdjacenteEnSortie.get(i).get(j).getN2().getIdNoeud();
					contrainte2 = this.listeAdjacenteEnSortie.get(i).get(j).getListeContraintes().get(k).getContrainte2();
					if (!this.listeDomaines.get(i).contains(contrainte1) || !this.listeDomaines.get(noeud).contains(contrainte2)){
						this.listeAdjacenteEnSortie.get(i).get(j).getListeContraintes().remove(k);
						k--;
					}
				}
			}
		}
	}

	// Remplissage de la liste du domaine
	/** Cette méthode permet de boucler sur les noeuds existants 
	    afin de remplir la liste de domaine avec les différentes listes
	    renvoyées par la fonction FilterDomaines().
	**/
	private void RemplirListeDomaine() {
		for (int i = 0; i < this.listeNoeuds.size(); i++){
			this.listeDomaines.add(i, FilterDomaines(i));
		}
		
	}

	private ArrayList<Integer> FilterDomaines(int idNoeud) {
		
		int val;
		ArrayList<Integer> domaines;

		domaines = this.listeNoeuds.get(idNoeud).getDomaine();
				
		// Pour chaque valeur du domaine du noeud, on vérifie la consistance
		for (int i = 0; i < this.listeNoeuds.get(idNoeud).getDomaine().size(); i++){
			val = this.listeNoeuds.get(idNoeud).getDomaine().get(i);
			if(!checkConsistance(val, idNoeud))
				// On retire du domaine les valeurs qui rendent le CSP inconsistency
				domaines.remove((Integer) val);
		}

		for (int i = 0; i < domaines.size(); i++) {
			while (NbrRepetition(domaines.get(i), domaines) > 1){
				domaines.remove(domaines.get(i));
			}
		}
		return domaines;
	}

	public int NbrRepetition(int valeur, ArrayList<Integer> domaines) {
		int i = 0;
		for (Integer domaine : domaines) {
			if (domaine == valeur)
				i++;
		}
		return i;
	}
	
	// make csp arc consistency
	private boolean checkConsistance(int val, int idNoeud) {
		
		boolean check = false;
		
		if (this.listeAdjacenteEnSortie.get(idNoeud).isEmpty() && this.listeAdjacenteEnEntree.get(idNoeud).isEmpty())
			check = true;
		
		for (int i = 0; i < this.listeAdjacenteEnSortie.get(idNoeud).size(); i++){
			check = false;
			for (int j = 0; j < this.listeAdjacenteEnSortie.get(idNoeud).get(i).getListeContraintes().size(); j++){
				if (val == this.listeAdjacenteEnSortie.get(idNoeud).get(i).getListeContraintes().get(j).getContrainte1()) {
					check = true;
					break;
				}
			}
			if(!check){
				return check;
			}
		}
		
		for (int i = 0; i < this.listeAdjacenteEnEntree.get(idNoeud).size(); i++){
			check = false;
			for (int j = 0; j < this.listeAdjacenteEnEntree.get(idNoeud).get(i).getListeContraintes().size(); j++){
				if (val == this.listeAdjacenteEnEntree.get(idNoeud).get(i).getListeContraintes().get(j).getContrainte2()) {
					check = true;
					break;
				}
			}
			if(!check){
				return check;
			}
		}
		return check;
	}

	public long backtracking () {
		int i = 0, valeur = 0;
		boolean check;
		long temps = System.nanoTime();
		long tempsExecution;
		ArrayList<ArrayList<Integer>> domaines;
		domaines = DomaineListe();
		
		while (i >= 0 && i < this.listeNoeuds.size()) {
			check = false;
			while (!check && !domaines.get(i).isEmpty()){
				valeur = domaines.get(i).remove(0);
   				if (checkassignationConsistance(valeur, this.listeNoeuds.get(i).getIdNoeud())){
   					check = true;
					this.listeNoeuds.get(i).setValNoeud(valeur);			
				}
			}
			if (!check) {
				domaines.get(i).clear();
				for(int j = 0; j < this.listeDomaines.get(i).size(); j++)				
					domaines.get(i).add(this.listeDomaines.get(i).get(j));
				i--;
			} else
				i++;
		}
		if (i < 0){
			System.out.println("BackTracking : UNSAT");
			tempsExecution = System.nanoTime();			
			return tempsExecution - temps;
		} else {
			tempsExecution = System.nanoTime();
			AfficherSolution();
			return tempsExecution - temps;
		}
	}

	public ArrayList<ArrayList<Integer>> DomaineListe () {
		ArrayList<ArrayList<Integer>> domaines1 = new ArrayList<>();
		for (ArrayList<Integer> listeDomaine : this.listeDomaines) {
			ArrayList<Integer> domaines2 = new ArrayList<>();
			domaines2.addAll(listeDomaine);
			domaines1.add(domaines2);
		}
		return domaines1;
	}

	private void AfficherSolution() {
		System.out.println("\n ---------------------------- Solution du CSP --------------------------- \n");
		System.out.println(" Les noeuds avec leurs valeurs --> \n");
		for (int i = 0; i < this.listeNoeuds.size(); i++) 
			System.out.println(" La valeur du noeud " + i + " : " + this.listeNoeuds.get(i).getValNoeud() + "\n");
	}
	
	// check la cohérence de l'assignation des valeurs aux variables
	private boolean checkassignationConsistance(int val, int idNoeud) {
		
		boolean check = false;
		for (int i = 0; i < this.listeAdjacenteEnSortie.get(idNoeud).size(); i++){
			check = false;
			if(this.listeAdjacenteEnSortie.get(idNoeud).get(i).getN2().getValNoeud() == -1) 
				check = true;
			else { 
				for(int j = 0; j < this.listeAdjacenteEnSortie.get(idNoeud).get(i).getListeContraintes().size(); j++)
					if (val == this.listeAdjacenteEnSortie.get(idNoeud).get(i).getListeContraintes().get(j).getContrainte1()
							&& this.listeAdjacenteEnSortie.get(idNoeud).get(i).getN2().getValNoeud() == this.listeAdjacenteEnSortie.get(idNoeud).get(i).getListeContraintes().get(j).getContrainte2()) {
						check = true;
						break;
					}
			}
			if (!check)
				return check;
		}
		for(int i = 0; i < this.listeAdjacenteEnEntree.get(idNoeud).size(); i++){
			check = false;
			if(this.listeAdjacenteEnEntree.get(idNoeud).get(i).getN1().getValNoeud() == -1) 
				check = true;
			else { 
				for(int j = 0; j < this.listeAdjacenteEnEntree.get(idNoeud).get(i).getListeContraintes().size(); j++)
					if (val == this.listeAdjacenteEnEntree.get(idNoeud).get(i).getListeContraintes().get(j).getContrainte2()
							&& this.listeAdjacenteEnEntree.get(idNoeud).get(i).getN1().getValNoeud() == this.listeAdjacenteEnEntree.get(idNoeud).get(i).getListeContraintes().get(j).getContrainte1()) {
						check = true;
						break;
					}
			}
			if (!check)
				return check;
		}
		return check;
	}

	public long backjumping() {
		int i = 0, j = 0, valeur = 0;
		boolean check;
		long temps = System.nanoTime();
		long tempsExecution;
		boolean consistency;
		
		ArrayList<ArrayList<Integer>> domaines;
		domaines = DomaineListe();
		
		ArrayList<Integer> coupables = new ArrayList<>();
		for (int k = 0 ; k < this.listeNoeuds.size(); k++) {
			coupables.add(k, 0);
		}
			
		while (i >= 0 && i < this.listeNoeuds.size()) {
			check = false;
			while(!check && !domaines.get(i).isEmpty()){
				valeur = domaines.get(i).remove(0);
				consistency = true;
				j = 0;
				while (j > i && consistency) {
					if (j > coupables.get(i)){
						coupables.add(i, j);
					}
	   				if (checkassignationConsistance(valeur, this.listeNoeuds.get(i).getIdNoeud())){
	   					j++;
	   				} else {
	   					consistency = false;
	   				}
				}
				if (consistency) {
					check = true;
					this.listeNoeuds.get(i).setValNoeud(valeur);
				}
			}
			
			if (!check) {
				this.listeDomaines.get(i).clear();
				for (int z = 0; z < this.listeDomaines.get(i).size(); z++)				
					domaines.get(i).add(this.listeDomaines.get(i).get(z));
				i = coupables.get(i);
			} else {
				i++;
				coupables.add(i, 0);
			}
		}
		
		if (i < 0) {
			System.out.println("BackJumping : UNSAT");
			tempsExecution = System.nanoTime();			
			return tempsExecution - temps;
		} else {
			tempsExecution = System.nanoTime();
			AfficherSolution();
			return tempsExecution - temps;
		}
	}

/*
	public long forwardChecking() {
		int i = 0;
		int j = 0;
		long temps = System.currentTimeMillis();
		long tempsExecution;
		int valeur;
		Boolean OK;
		ArrayList<ArrayList<Integer>> listeDomaines = this.DomaineListe();

		while (i < this.listeNoeuds.size()) {

			OK = false;
			while(!OK && !listeDomaines.get(i).isEmpty()){
				System.out.println("index: " + i);
				valeur = listeDomaines.get(i).remove(0);
				for (int k = i; k <= this.listeNoeuds.size(); k++) {
					revise(k, i);
					checkingNextStep(valeur);
				}
			}


			if(!OK) {
				this.listeNoeuds.get(i).setDomaines(listeDomaines.get(i));
			}
			i++;

		}



		tempsExecution = System.currentTimeMillis();
		return tempsExecution - temps;
	}

	public void revise(int m, int n) {
		Arc linkToWatch = null;

		for (Arc a : this.listeArcs) {
			if(a.getN1().getIdNoeud() == n && a.getN2().getIdNoeud() == m) {
				linkToWatch = a;
			}
		}

		if(linkToWatch != null) {
			for(int i = 1; i <= this.listeArcs.size(); i++) {
				this.listeArcs.set(m -1 , linkToWatch);

				if(!checkConsistance(linkToWatch.getN1().getValNoeud(), linkToWatch.getN2().getIdNoeud()) && !checkConsistance(linkToWatch.getN1().getValNoeud(), linkToWatch.getN2().getIdNoeud())) {
					this.listeArcs.remove(m-1);
				}
			}
		}
	}

	public void checkingNextStep(int valeur){

	}*/


}
