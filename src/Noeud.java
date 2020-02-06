import java.util.ArrayList;

public class Noeud implements Comparable<Noeud> {
	protected int id;
	protected int nbVoisins;
	// c'est plus simple d'ajouter et d'enlever des elements avec une liste qu'avec un array
	protected ArrayList<Noeud>  successeurs;
	protected ArrayList<Integer> arcs; //arcs sortant 
	
	// cree un noeud isole
	public Noeud(int i) {
		this.id=i;
		this.successeurs=new ArrayList<Noeud>();
		this.arcs=new ArrayList<Integer>();
	}
	
	// cree un noeud a partir d'une liste de voisins
	// les arcs sont de poids 1
	public Noeud(int i, Noeud[] noeuds) {
		this.id=i;
		if(noeuds.length!=0) {
			for (Noeud node : noeuds) {
				this.nbVoisins++;
				//if(node.estVoisin(this)==-1) { //Donc node est un successeur, car node ne connait pas this comme successeur
					this.successeurs.add(node);
					this.arcs.add(1);
				//}
			}
		}else {
			System.out.println("Tableau de noeuds vide, noeud initialise par default");
			//this = new Noeud(i);
			this.successeurs=new ArrayList<Noeud>();
			this.arcs=new ArrayList<Integer>();
		}
	}
	
	// renvoie le nombre de voisins
	public int nbVoisins() {
    	return this.nbVoisins;
	}
	
	// renvoie le degre sortant, qui vaut la somme des elements de arcs
	public int degreSortant() {
		int deg=0;
		for(int i : this.arcs) {
			deg=deg+i;
		}
    	return deg;
    	
	}
	
	// renvoie l'indice de v dans voisins si v est voisin
	// renvoie -1 si v n'est pas voisin
	public int estVoisin(Noeud v) {
		int ret = -1;
		for (Noeud node : this.successeurs) {
			if(v.compareTo(node)==0) {
				ret= this.successeurs.indexOf(v);
			}
		}
		return ret;
	}
	
	// renvoie le poids de l'arc correspondant si v est voisin, 0 sinon
	public int nbArcs(Noeud v) {
		if(this.estVoisin(v)!=-1) return this.arcs.get(this.successeurs.indexOf(v));
		else return 0;
	}
	
	
	// si v est deja voisin, modifie la poids de l'arc correspondant
	// sinon, ajoute v comme un nouveau voisin avec un arc de poids d
	// (il faut incrementer nbArcs si c'est un nouveau voisin)
	// revoie le nouveau nombre de voisins
	public int ajouteVoisin(Noeud v, int d) {
		if(this.estVoisin(v)!=-1) {this.arcs.set(this.successeurs.indexOf(v), this.arcs.get(this.successeurs.indexOf(v)) + d);}
		else{
			this.successeurs.add(v);
			this.arcs.add(d);
			this.nbVoisins++;
		}
		return this.nbVoisins;
	}
	
	// si v n'est pas voisin, ne fait rien
	// sinon enleve v de la liste de voisins
	// renvoie le nouveau nombre de voisins
	public int enleveVoisin(Noeud v) {
		int index = this.estVoisin(v);
		if(index != -1) {
			this.successeurs.remove(index);
			this.arcs.remove(index);
			this.nbVoisins--;
		}
		return this.nbVoisins;
	}
	
	@Override
	public int compareTo(Noeud o) {
		if ( id > o.id ) {
			return 1;
		}
		else if ( id < o.id ) {
			return -1;
		}
		else {
			return 0;
		}
	}
	

}
