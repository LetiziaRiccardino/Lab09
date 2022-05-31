package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private Graph<Country, DefaultEdge> grafo;
	private Map<Integer, Country> idMap;
	private BordersDAO dao;

	public Model() {
		
		dao= new BordersDAO();
		idMap= new HashMap <>();
		dao.loadAllCountries(idMap); //riempio la mappa con i vertici;
		
	}
	
	public void creaGrafo(int anno) {
		this.grafo= new SimpleGraph<Country, DefaultEdge>(DefaultEdge.class);
		
		//aggiungo i vertici
		List<Country> vertici= new ArrayList<Country> (idMap.values());
		Graphs.addAllVertices(this.grafo, vertici);
		
		//aggiungo gli archi non orientati e non pesati
		for(Border a: dao.getCountryPairs(anno)) {
			if(a.getCcode1()>a.getCcode2())
				Graphs.addEdgeWithVertices(this.grafo, idMap.get(a.getCcode1()), idMap.get(a.getCcode2()));
		}
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public Map<Country, Integer> statiConfinanti(){ //potrebbe anche tornare una Stringa
		//Stampare l’elenco degli stati, indicando per ciascuno il numero di stati confinanti (grado del vertice)
		Map<Country, Integer> stati= new HashMap<Country, Integer>();
		
		for(Country c: this.grafo.vertexSet()) {
			for(DefaultEdge e: this.grafo.edgeSet()) {
				if(this.grafo.getEdgeSource(e).getCcode()==c.getCcode() ||
						this.grafo.getEdgeTarget(e).getCcode()==c.getCcode()) {
					if(stati.containsKey(c)) {
						Integer somma= stati.get(c)+1;
						stati.put(c, somma);
					}else {
						stati.put(c, 1);
					}
				}
			}
		}
		return stati;
	}
	
	//stampare il numero di componenti connesse nel grafo
	public int getComponenteConnessa() {//posso decidere se fare visita in ampiezza o in profondita
		Set<Country> visitati= new HashSet<>();
		DepthFirstIterator<Country, DefaultEdge> it= new DepthFirstIterator<>(this.grafo);
		while(it.hasNext())
			visitati.add(it.next());
		return visitati.size();
	}

}
