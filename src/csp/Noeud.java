package csp;

import java.util.ArrayList;

public class Noeud {
	
	private int idNoeud;
	private int valNoeud;
	public ArrayList<Integer> domaines;
	
	public Noeud(int idNoeud, ArrayList<Integer> domaines) {
		this.domaines = new ArrayList<>();
		this.idNoeud = idNoeud;
		this.valNoeud = -1;
		this.setDomaines(domaines);
	}

	public int getIdNoeud() {
		return idNoeud;
	}

	public void setIdNoeud(int idNoeud) {
		this.idNoeud = idNoeud;
	}

	public int getValNoeud() {
		return valNoeud;
	}

	public void setValNoeud(int valNoeud) {
		this.valNoeud = valNoeud;
	}

	public ArrayList<Integer> getDomaine() {
		return domaines;
	}

	public void setDomaines(ArrayList<Integer> domaine) {
		this.domaines.addAll(domaine);
	}

	@Override
	public String toString() {
		return "Noeud [idNoeud=" + idNoeud + "]";
	}

	public void afficherDomaines(){
		for (Integer domaine : domaines) {
			System.out.print(domaine + ", ");
		}
		System.out.println();
	}

}
