import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/*
 * Dans notre implementation si on a N personne alors le reseau aura 2N+2 Nodes
 * 2N+2 Nodes = N nodes pour les personnes, N nodes pour les taches, 1 node source et 1 node puits
 * 
 * la source portera l'id 0
 * les nodes personnes auront les id 1,2, ..., N
 * les nodes tache auront les id  N+1, N+2 ..., 2N
 * le puits portera l'id 2N+1
 */
public class Couplage {

	public static Graphe creeReseau(String fichier) {
		Graphe graphe = new Graphe();
		
		try {
			BufferedReader buf = new BufferedReader( new FileReader( fichier ));
			String ligne;
			String[] mots;

			// la premiere ligne contient le nombre de taches donc de travailleurs aussi
			ligne = buf.readLine();
			int n = (new Integer(ligne)).intValue();

			// creer les nodes sans les arcs
			Noeud[] nds = new Noeud[2*n];
			for ( int i = 0; i < 2*n; i++ ) {
				nds[i] = new Noeud(i+1);
				
			}

			Noeud s = new Noeud(0);
			Noeud t = new Noeud(2*n+1);
			
			for (int i = 0; i < n; i++) {
				//System.out.println("s "+nds[i].id);
				s.ajouteVoisin(nds[i], 1);
			}
			for (int i = n; i < 2*n; i++) {
				//System.out.println("p "+nds[i].id);
				nds[i].ajouteVoisin(t, 1);
			}

			// ajouter arcs
			int linCompteur = 0, colCompteur = 0;
			while ( (ligne = buf.readLine()) != null ) {
				linCompteur++;
				
				// s'il y a trop de lignes, afficher un
				// message d'erreur et renvoyer un graphe vide
				if ( linCompteur > n ) {
					System.out.println("Fichier mal forme: nombre de lignes trop grand.");
					graphe.nbNoeuds = 0;
					graphe.noeuds = null;
					buf.close();
				}
				
				mots = ligne.split(" ");
				colCompteur = mots.length;
				
				// chaque ligne doit avoir exactement n mots
				// si le nombre de mot de la ligne courante est different de n, afficher un
				// message d'erreur et renvoyer un graphe vide
				if ( colCompteur > n || colCompteur == 0) {
					System.out.println("Fichier mal forme: mauvais nombre de colonnes.");
					graphe.nbNoeuds = 0;
					graphe.noeuds = null;
					buf.close();
				}
				// on ajoute les arcs
				else {
						for (int i = 0; i < colCompteur; i++ ) {
							int val = (new Integer(mots[i])).intValue();
							//System.out.println("s > "+(val-1));
							nds[linCompteur - 1].ajouteVoisin(nds[val-1+n], 1);
						}
				}
			}
			
			// si on arrive ici, tout est bien !
			graphe.nbNoeuds = 2*n+2;
			graphe.noeuds = new ArrayList<Noeud>(graphe.nbNoeuds);
			graphe.noeuds.add(s);
			for (int i = 0; i < 2*n; i++) {
				graphe.noeuds.add(nds[i]);
				
			}
			graphe.noeuds.add(t);
			buf.close();
		}
		catch ( Exception e ) {			
			e.printStackTrace();
		}
		return graphe;
	}
	
	public static void main(String[] args) {
		String fpath = "/home/segla/eclipse-workspace/RO-Mini-projet/src/taches";
		Graphe g = creeReseau(fpath);
		g.affiche();
		System.out.println("\n\n");
		g.ff(g.noeuds.get(0), g.noeuds.get(g.noeuds.size()-1));
		
		g.affiche();
	}
}
