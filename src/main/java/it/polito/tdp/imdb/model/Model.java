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
}
