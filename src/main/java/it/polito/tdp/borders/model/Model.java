package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private Graph<Country, DefaultEdge> grafo;
	private Map<Integer, Country> idMap; //salvo tutti i Country presenti nel database
	private BordersDAO dao;
	private List<Country> countries; //corrisponfìdono ai vertici del grafo, i quali cambiano per ogni anno selezionato

	public Model() {
		
		dao= new BordersDAO();
		idMap= new HashMap<Integer, Country>();
		dao.loadAllCountries(idMap);
		
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
	
	public Map<Country, Integer> gradoVertice(){ //potrebbe anche tornare una Stringa
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
	
	public Collection<Country> getAllStati(){
		return this.idMap.values();
	}
	
	
	
	//PUNTO 2 visualizzare la lista di tutti i nodi raggiungibili nel graf a partire da un vertice selezionato
	public List<Country> getReachableCountries(Country selectedCountry) {

		if (!this.grafo.vertexSet().contains(selectedCountry)) {
			throw new RuntimeException("Selected Country not in graph");
		}

		//versione iterativa
		List<Country> reachableCountries = this.displayAllNeighboursIterative(selectedCountry);
		System.out.println("Reachable countries: " + reachableCountries.size());
		//con libreria JGraphT
		reachableCountries = this.displayAllNeighboursJGraphT(selectedCountry);
		System.out.println("Reachable countries: " + reachableCountries.size());
		//versione ricorsiva
		reachableCountries = this.displayAllNeighboursRecursive(selectedCountry);
		System.out.println("Reachable countries: " + reachableCountries.size());

		return reachableCountries;
	}

	/*
	 * VERSIONE ITERATIVA
	 */
	private List<Country> displayAllNeighboursIterative(Country selectedCountry) {

		// Creo due liste: quella dei noti visitati ..
		List<Country> visited = new LinkedList<Country>();

		// .. e quella dei nodi da visitare
		List<Country> toBeVisited = new LinkedList<Country>();

		// Aggiungo alla lista dei vertici visitati il nodo di partenza.
		visited.add(selectedCountry);

		// Aggiungo ai vertici da visitare tutti i vertici collegati a quello inserito
		toBeVisited.addAll(Graphs.neighborListOf(this.grafo, selectedCountry));

		while (!toBeVisited.isEmpty()) {

			// Rimuovi il vertice in testa alla coda
			Country temp = toBeVisited.remove(0);

			// Aggiungi il nodo alla lista di quelli visitati
			visited.add(temp);

			// Ottieni tutti i vicini di un nodo
			List<Country> listaDeiVicini = Graphs.neighborListOf(this.grafo, temp);

			// Rimuovi da questa lista tutti quelli che hai già visitato..
			listaDeiVicini.removeAll(visited);

			// .. e quelli che sai già che devi visitare.
			listaDeiVicini.removeAll(toBeVisited);

			// Aggiungi i rimanenenti alla coda di quelli che devi visitare.
			toBeVisited.addAll(listaDeiVicini);
		}

		// Ritorna la lista di tutti i nodi raggiungibili
		return visited;
	}

	/*
	 * VERSIONE LIBRERIA JGRAPHT
	 */
	private List<Country> displayAllNeighboursJGraphT(Country selectedCountry) {

		List<Country> visited = new LinkedList<Country>();

		// Versione 1 : utilizzo un BreadthFirstIterator
//		GraphIterator<Country, DefaultEdge> bfv = new BreadthFirstIterator<Country, DefaultEdge>(graph,
//				selectedCountry);
//		while (bfv.hasNext()) {
//			visited.add(bfv.next());
//		}

		// Versione 2 : utilizzo un DepthFirstIterator
		GraphIterator<Country, DefaultEdge> dfv = new DepthFirstIterator<Country, DefaultEdge>(this.grafo, selectedCountry);
		while (dfv.hasNext()) {
			visited.add(dfv.next());
		}

		return visited;
	}

	/*
	 * VERSIONE RICORSIVA
	 */
	private List<Country> displayAllNeighboursRecursive(Country selectedCountry) {

		List<Country> visited = new LinkedList<Country>();
		recursiveVisit(selectedCountry, visited);
		return visited;
	}

	private void recursiveVisit(Country n, List<Country> visited) {
		// Do always
		visited.add(n);

		// cycle
		for (Country c : Graphs.neighborListOf(this.grafo, n)) {	
			// filter
			if (!visited.contains(c))
				recursiveVisit(c, visited);
				// DO NOT REMOVE!! (no backtrack)
		}
	}

}
