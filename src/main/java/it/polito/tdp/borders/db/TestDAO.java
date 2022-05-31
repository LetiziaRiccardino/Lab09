package it.polito.tdp.borders.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Border;

public class TestDAO {

	public static void main(String[] args) {

		BordersDAO dao = new BordersDAO();
		Map<Integer, Country> idMap= new HashMap<Integer, Country>();

		System.out.println("Lista di tutte le nazioni:");
		dao.loadAllCountries(idMap);
//		for(Country c: idMap.values()) {
//			System.out.println(c.toString()+"\n");
//		}
		System.out.println("Numero vertici: "+ idMap.size());
		
		
		List<Border> border= new ArrayList<Border>();
		border=dao.getCountryPairs(2000);
		System.out.print("Numero archi: "+border.size());
	}
}
