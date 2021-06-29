package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private SimpleWeightedGraph<Director, DefaultWeightedEdge> grafo;
	private Map<Integer,Director> idMap;
	
	private List<Director> parziale;
	private List<Director> percorsoMigliore;
	private int pesoMax;
	private int maxNumRegistiConcatenati;
	private int pesoMigliore;
	
	public Model() {
		dao=new ImdbDAO();
		idMap=new HashMap<>();
		dao.listAllDirectors(idMap);
	}
	
	
	public void creaGrafo(int anno) {
		grafo=new SimpleWeightedGraph<Director, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//vertici
		Graphs.addAllVertices(grafo, dao.getVertici(anno, idMap));
		
		//archi
		for(Arco a:dao.getArchi(anno, idMap)) {
			Graphs.addEdge(grafo, a.getD1(), a.getD2(), a.getPeso());
		}
		
		System.out.println("Vertici: "+grafo.vertexSet().size());
		System.out.println("Archi: "+grafo.edgeSet().size());

	}


	public Graph<Director, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	

	public List<Adiacente> getRegistiAdiacenti(Director d){
		List<Adiacente> adiacenti=new ArrayList<>();
		
		for(DefaultWeightedEdge e: grafo.edgesOf(d)) {
			adiacenti.add(new Adiacente(Graphs.getOppositeVertex(grafo, e, d), grafo.getEdgeWeight(e)));
		}
		
		Collections.sort(adiacenti);
		
		return adiacenti;
	}
	
	public List<Director> getRegistiAffini(Director partenza, int pesoMax){
		percorsoMigliore=new ArrayList<>();
		parziale=new ArrayList<>();
		pesoMigliore=0;
		
		//nell'ogg adiacente metterò il nodo su cui sonon arrivato e il peso dell'arco attraversato per raggiungerlo
		//il nodo da cui parto ha 'peso' nullo
		parziale.add(partenza);
		this.pesoMax=pesoMax;
		maxNumRegistiConcatenati=0;
		
		cerca(parziale, 0); 
		
		return percorsoMigliore;
	}


	private void cerca(List<Director> parziale, int pesoParziale) {
		//caso terminale
		if(pesoParziale>pesoMax) {
			return;
		}
		if(parziale.size()>maxNumRegistiConcatenati) {
			maxNumRegistiConcatenati=parziale.size();
			pesoMigliore=pesoParziale;
			percorsoMigliore=new ArrayList<>(parziale);
		}
		
		//ricorsione
		//guardo tutti gli archi collegati all'ultimo nodo aggiunto
		Director partenza=parziale.get(parziale.size()-1); //ultimo el aggiunto a parziale
		for(DefaultWeightedEdge e: grafo.edgesOf(partenza)) {
			
			//controllo se sono già passato da quel vertice
			if(!parziale.contains(Graphs.getOppositeVertex(grafo, e, partenza))) {
				
					parziale.add(Graphs.getOppositeVertex(grafo, e, partenza));
					pesoParziale+=grafo.getEdgeWeight(e);
					cerca(parziale, pesoParziale);
					parziale.remove(parziale.size()-1);
					pesoParziale-=grafo.getEdgeWeight(e);
					
			}
		} //fine for su archi adiacenti
		
	}

	public int getPesoPercorso() {
		if(percorsoMigliore!=null) {
			return pesoMigliore;
		} else 
			return -1;
	}
}