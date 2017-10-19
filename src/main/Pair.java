package main;
public class Pair<T,P>{
	public T el1;
	public P el2;
	public Pair(T el1,P el2){
		this.el1=el1;
		this.el2=el2;
	}
	
	public String toString(){
		return el1.toString()+" , "+el2.toString();
	}
}
