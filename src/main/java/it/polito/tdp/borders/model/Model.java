package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private Graph<Country, DefaultEdge> grafo;
	private Map<Integer, Country> idMap;
	private BordersDAO dao;
	private List<Country> countries;

	public Model() {
		
		dao= new BordersDAO();
		
		
	}
	
	public void creaGrafo(int anno) {
		
		this.grafo= new SimpleGraph<Country, DefaultEdge>(DefaultEdge.class);
		
		//aggiungo i vertici
//		List<Country> vertici= new ArrayList<Country> (idMap.values());
//		Graphs.addAllVertices(this.grafo, vertici);
		
		
		//aggiungo gli archi non orientati e non pesati
//		for(Border a: dao.getCountryPairs(anno, idMap)) {
//			if(a.getC1().getCcode()>a.getC2().getCcode())
//				Graphs.addEdgeWithVertices(this.grafo, a.getC1(), a.getC2());
//		}
		
		List<Border> confini = dao.getCountryPairs(anno, this.idMap);

		if (confini.isEmpty()) {
			throw new RuntimeException("No country pairs for specified year");
		}

		this.grafo = new SimpleGraph<>(DefaultEdge.class);

		for (Border b : confini) {
			grafo.addVertex(b.getC1());
			grafo.addVertex(b.getC2());
			grafo.addEdge(b.getC1(), b.getC2());
		}
		
		// Sort the countries
				countries = new ArrayList<>(grafo.vertexSet());  //gli stati, e dunque i vertici del grafo, cambiano per ogni anno selezionato: non ci sono tutti i Country inizialmente
				Collections.sort(countries); //devo importare Collections
				                             //devo aggiungere a Country: implements Comparable<Country>

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
//			for(DefaultEdge e: this.grafo.edgeSet()) {
//				if(this.grafo.getEdgeSource(e).getCcode()==c.getCcode() ||
//						this.grafo.getEdgeTarget(e).getCcode()==c.getCcode()) {
//					if(stati.containsKey(c)) {
//						Integer somma= stati.get(c)+1;
//						stati.put(c, somma);
//					}else {
//						stati.put(c, 1);
//					}
//				}
//			}
			stati.put(c, this.grafo.degreeOf(c));
		}
		return stati;
	}
	
	//stampare il numero di componenti connesse nel grafo
	public int getComponenteConnessa() {//posso decidere se fare visita in ampiezza o in profondita
//		Set<Country> visitati= new HashSet<>();
//		DepthFirstIterator<Country, DefaultEdge> it= new DepthFirstIterator<>(this.grafo);
//		while(it.hasNext())
//			visitati.add(it.next());
//		return visitati.size();
		
		if (this.grafo == null) {
			throw new RuntimeException("Grafo non esistente");
		}

		ConnectivityInspector<Country, DefaultEdge> ci = new ConnectivityInspector<Country, DefaultEdge>(this.grafo);
		return ci.connectedSets().size();
	}

}
