package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public void loadAllCountries(Map<Integer, Country> idMap) {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Country c= new Country(rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
				idMap.put(rs.getInt("ccode"), c);
			}
			
			conn.close();
			

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Border> getCountryPairs(int anno, Map<Integer, Country> idMap) {
		
		String sql="SELECT c.state1no as c1, c.state2no as c2, c.year as anno "
				+ "FROM contiguity c "
				+ "WHERE c.year<=? AND c.conttype=1"; //1 perchè mi interessano solo i collegmenti via terra
		
		List<Border> result= new ArrayList<Border>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				int c1Code = rs.getInt("c1");
				int c2Code = rs.getInt("c2");
				
//				Border b= new Border(rs.getInt("c1"), rs.getInt("c2"), rs.getInt("anno"));
//				result.add(b);
				
				// The identity map guarantees the uniqueness of c1 and c2 objets 
				Country c1 = idMap.get(c1Code);
				Country c2 = idMap.get(c2Code);
				
				// Just check that c1 and c2 object really exist, otherwise skip them
				if (c1 != null && c2 != null) {
					result.add(new Border(c1, c2, rs.getInt("anno") ));
				} else {
					System.out.println("Error skipping " + String.valueOf(c1Code) + " - " + String.valueOf(c2Code));
				}
				
				Border b = new Border(c1, c2, rs.getInt("anno"));
				result.add(b);
			}
			
			conn.close();
			

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
		
		return result;
	}
}
