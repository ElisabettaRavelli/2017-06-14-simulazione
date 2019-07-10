package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.alg.KosarajuStrongConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private ArtsmiaDAO dao;
	private SimpleDirectedGraph<Integer, DefaultEdge> grafo;
	private Map<Integer, ArtObject> idMap;
	
	public Model() {
		this.dao = new ArtsmiaDAO();
		this.idMap = new HashMap<>();
		this.dao.listObject(idMap);
	}
	
	public List<Integer> getAllAnni(){
		return this.dao.getAllAnni();
	}
	
	public void creaGrafo(Integer anno) {
		this.grafo = new SimpleDirectedGraph<>(DefaultEdge.class);
		for(Arco a: this.dao.getConnessioni(anno)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getId1(), a.getId2());
			System.out.println("Arco aggiunto: "+a.getId1()+ " -> "+a.getId2());
		}
	}
	public int getVertici() {
		return this.grafo.vertexSet().size();
	}
	public int getArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public boolean calcolaCC() {
		KosarajuStrongConnectivityInspector<Integer, DefaultEdge> ci = new KosarajuStrongConnectivityInspector<Integer, DefaultEdge>(grafo);
		return ci.isStronglyConnected();
	}
	
	public ArtObject mostraMaxOpere() {
		int numMaxOpere = 0;
		Integer mostra = null;
		for(Integer i: this.grafo.vertexSet()) {
			int numOpere = this.dao.numeroOpere(i);
			if(numOpere > numMaxOpere) {
				numMaxOpere = numOpere;
				mostra = i;
			}
		}
		return idMap.get(mostra);
	}
}
