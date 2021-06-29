package it.polito.tdp.imdb.model;

public class Adiacente implements Comparable<Adiacente>{

	private Director d;
	private int peso;
	
	public Adiacente(Director d, double peso) {
		super();
		this.d = d;
		this.peso = (int) peso;
	}
	
	public Director getD() {
		return d;
	}
	
	public void setD(Director d) {
		this.d = d;
	}
	
	public int getPeso() {
		return peso;
	}
	
	public void setPeso(int peso) {
		this.peso = peso;
	}

	@Override
	public int compareTo(Adiacente other) {
		return -(this.peso-other.peso);
	}
	
}
