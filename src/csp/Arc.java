package csp;

import java.util.ArrayList;

public class Arc {
	
	private Noeud n1;
	private Noeud n2;
	public ArrayList<Contrainte> listeContraintes;

	public Arc(Noeud n1, Noeud n2) {
		this.listeContraintes = new ArrayList<>();
		this.n1 = n1;
		this.n2 = n2;
	}

	@Override
	public String toString() {
		return "Arc [n1=" + n1.toString() + ", n2=" + n2.toString() + "]";
	}


	public Noeud getN1() {
		return n1;
	}


	public void setN1(Noeud n1) {
		this.n1 = n1;
	}


	public Noeud getN2() {
		return n2;
	}


	public void setN2(Noeud n2) {
		this.n2 = n2;
	}


	public ArrayList<Contrainte> getListeContraintes() {
		return listeContraintes;
	}

	public void setListeContraintes(ArrayList<Contrainte> listeContraintes) {
		this.listeContraintes = listeContraintes;
	}

	public void afficherListeContraintes() {

		for (Contrainte listeContrainte : listeContraintes) {
			System.out.println(listeContrainte.toString() + ", ");
		}
	}
	public void genererListeContraintes() {
		for(int i = 0; i < n1.getDomaine().size(); i++) {
			for(int j = 0; j < n2.getDomaine().size(); j++) {
				Contrainte contrainte = new Contrainte(n1.getDomaine().get(i), n2.getDomaine().get(j));
				listeContraintes.add(contrainte);
			}
		}
	}
	

}
