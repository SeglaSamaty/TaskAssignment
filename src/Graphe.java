import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;

public class Graphe {
	protected int nbNoeuds;
	protected ArrayList<Noeud> noeuds;
	
	
	
	public Graphe() {
   		nbNoeuds=0;
   		noeuds=new ArrayList<Noeud>();
	}
	
	
	public Graphe(Noeud[] nds) {
		for (Noeud noeud : nds) {
			this.noeuds.add(noeud);
		}
		this.nbNoeuds = nds.length;
	}
	
	
	public Graphe(String fileName) {
		try {
			BufferedReader buf = new BufferedReader( new FileReader( fileName ));
			String ligne;
			String[] mots;

			// la premiere ligne contient le nombre de noeuds
			ligne = buf.readLine();
			int n = (new Integer(ligne)).intValue();

			// creer les noeuds vides
			Noeud[] nds = new Noeud[n];
			for ( int i = 0; i < n; i++ ) {
				nds[i] = new Noeud(i);
			}

			// ajouter arcs
			int linCompteur = 0, colCompteur = 0;
			while ( (ligne = buf.readLine()) != null ) {
				linCompteur++;
				
				// s'il y a trop de lignes, afficher un
				// message d'erreur et renvoyer un graphe vide
				if ( linCompteur > n ) {
					System.out.println("Fichier mal forme: nombre de lignes trop grand.");
					nbNoeuds = 0;
					noeuds = null;
					buf.close();
					return;
				}
				
				mots = ligne.split(" ");
				colCompteur = mots.length;
				
				// chaque ligne doit avoir exactement n mots
				// si le nombre de mot de la ligne courante est different de n, afficher un
				// message d'erreur et renvoyer un graphe vide
				if ( colCompteur != n ) {
					System.out.println("Fichier mal forme: mauvais nombre de colonnes.");
					nbNoeuds = 0;
					noeuds = null;
					buf.close();
					return;
				}
				// sinon ajouter les arcs
				else {
					for (int i = 0; i < colCompteur; i++ ) {
						int val = (new Integer(mots[i])).intValue();
						if ( val != 0 ) {
							nds[linCompteur - 1].ajouteVoisin(nds[i], 1);
						}
					}
				}
			}
			
			// si on arrive ici, tout est bien !
			nbNoeuds = n;
			noeuds = new ArrayList<Noeud>(nbNoeuds);
			for (int i = 0; i < nbNoeuds; i++) {
				noeuds.add(i, nds[i]);
			}
			
			buf.close();
		}
		catch ( Exception e ) {			
			e.printStackTrace();
		}
	}
	//******
	public void affiche() {
		System.out.println("Nombre de noeuds = " + nbNoeuds);
		for ( Noeud v : noeuds ) {
			String s = "" + v.id + ": ";
			for ( Noeud u : v.successeurs ) {
				s += u.id + " ";
			}
			System.out.println(s);
		}
		System.out.println();
	}

	public LinkedList<Noeud> cheminBFS(Noeud s, Noeud t){
		/*
		 * Cette methode retourne le chemin de s vers t
		 */
		
		boolean visited[] = new boolean[nbNoeuds]; // liste de l'etat des noeuds (visite ou non visite)
		Noeud previousNode[] = new Noeud[nbNoeuds];// liste de node qui conduise au node donne
        for(int i=0; i<nbNoeuds; ++i) { 
            visited[i]=false; //tout est a non visite pour le debut
        	previousNode[i]=null;  //aucun node ne connais a priori sont node parent dans l'arbre genere par bfs
        }
        
        LinkedList<Noeud> queue = new LinkedList<Noeud>();
        
        queue.add(s);
        visited[0] = true;
        
        while (queue.size()!=0) 
        { 
            Noeud u = queue.poll();
  
            for (Noeud node : u.successeurs) {
            	int index=getNodeIndexInNodeList(node);
            	
            	if (visited[index]==false) {
            		queue.add(node);
            		previousNode[index]=u; //On enregistre le node parent
                    visited[index] = true; //le node est donc visite
            	}
            } 
        }
        
      //le chemin qu'on va obtenir est l'inverse du chemin c'est-a-dire qu'il commence par t et fini par s
      	LinkedList<Noeud> cheminInverse = new LinkedList<Noeud>(); //Contient les nodes du chemin dans le sens inverse
      	LinkedList<Noeud> chemin = new LinkedList<Noeud>(); //Contient les nodes du chemin
      	Noeud n = t;
        cheminInverse.add(n);
        try {
        	//calcul du chemin inverse
        	 while (n.compareTo(s)!=0 && n!=null){
             	n=previousNode[getNodeIndexInNodeList(n)];
     			cheminInverse.add(n);
     		}
             //Nous allons reverse le chemin inverse
             
             int nbNodePth = cheminInverse.size();
             for (int i = 0; i < nbNodePth; i++) {
     			chemin.add(cheminInverse.get(nbNodePth-i-1));
     		}
             
            
		} catch (Exception e) {
			//Si on tombe su un exceprion en l'occurence nullpointer => le previousNode est vide donc plus de chemin 
			//possible entre s et p
			chemin = null;
		}
        
		return chemin;
	}
	
	public boolean inverseChemin(LinkedList<Noeud> chemin) {
		/*
		 * Cette methode marche bien avec un chemin qui est un vrai liste simple  car si les nodes sont 
		 * doublement chainees ou meme avec des cycles les liens residuels autre ceux implicite du chemin restent
		 * inchanges
		 */
		boolean ret = true;
		try {
			System.out.println();
			for (int i = 0; i < chemin.size(); i++) {
				//System.out.println(chemin.get(i).id+" av :"+chemin.get(i).successeurs.size());
				chemin.get(i).enleveVoisin(chemin.get(i+1));
				chemin.get(i+1).ajouteVoisin(chemin.get(i), 1);
				//System.out.println(chemin.get(i).id+" ap :"+chemin.get(i).successeurs.size());
			}
		} catch (Exception e) {
			ret = false;
		}
		
		return ret;
		
	}
	
	
	public int ff(Noeud s, Noeud t) {
		//les affectations sont redirigees vers le fichier affectation
		
		LinkedList<Noeud> shPath = this.cheminBFS(s, t);
		while (shPath!=null) {
			//int q = shPath.get(2).id-this.nbNoeuds/2 +1;
			//str = str+" "+shPath.get(1).id+" : "+q+"\n";
			this.inverseChemin(shPath);
			
			shPath = this.cheminBFS(s, t);
		}
		
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("/home/segla/eclipse-workspace/RO-Mini-projet/src/affectations"));
			writer.write("Couplage max : "+t.successeurs.size()+" \n");
			
			String str = "\n  Personnes : Taches \n";
			str = str + "  ------------------\n";
			int tmp = this.nbNoeuds-2;
			tmp = tmp/2;
			for (Noeud noeud : t.successeurs) {
				str = str+"  "+noeud.successeurs.get(0).id+"         :   "+(noeud.id-tmp)+"\n";
				//System.out.println(str);
			}
			
			
			writer.write(str);
		    writer.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	    
		return t.successeurs.size();
	}
	
	public Noeud getNodeById(int id) {
		
		Noeud ret = null;
		
		for (int i = 0; i < noeuds.size(); i++) {
			if (noeuds.get(i).id == id) {
				ret = noeuds.get(i);
			}
		} 
		
		return ret;
	}
	
	public int getNodeIndexInNodeList(Noeud node) {
		/*
		 * Cette methode retourne l'index d'un node dans la liste des node du graphe en prenant en parametre la 
		 * valeur du node
		 * retourne -1 si le node n'est pas dans le graphe
		 */
		int index = -1;
		for (int i = 0; i < noeuds.size(); i++) {
			if (noeuds.get(i).id==node.id) {
				index = i;
			}
		}
		return index;
	}

	
}
