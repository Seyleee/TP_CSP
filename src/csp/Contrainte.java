package csp;

public class Contrainte {
	
	private int contrainte1;
	private int contrainte2;

	public Contrainte(int contrainte1, int contrainte2) {
		this.contrainte1 = contrainte1;
		this.contrainte2 = contrainte2;
	}

	public int getContrainte1() {
		return contrainte1;
	}

	public void setContrainte1(int contrainte1) {
		this.contrainte1 = contrainte1;
	}

	public int getContrainte2() {
		return contrainte2;
	}

	public void setContrainte2(int contrainte2) {
		this.contrainte2 = contrainte2;
	}

	@Override
	public String toString() {
		return "Contrainte [contrainte1=" + contrainte1 + ", contrainte2=" + contrainte2 + "]";
	}

}
