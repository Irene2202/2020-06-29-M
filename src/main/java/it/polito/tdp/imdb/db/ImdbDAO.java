package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Arco;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public void listAllDirectors(Map<Integer, Director> map){
		String sql = "SELECT * FROM directors";
		//List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!map.containsKey(res.getInt("id"))) {
					Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
					map.put(director.getId(), director);
				}
				
				//result.add(director);
			}
			conn.close();
			//return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException ("ERRORE DB", e);
		}
	}

	public List<Director> getVertici(int anno, Map<Integer, Director> map) {
		String sql = "SELECT distinct md.director_id AS id "
				+ "FROM movies_directors md, movies m "
				+ "WHERE m.id=md.movie_id AND m.year=?";
		List<Director> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(map.containsKey(res.getInt("id"))) {
					result.add(map.get(res.getInt("id")));
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException ("ERRORE DB", e);
		}
	}

	public List<Arco> getArchi(int anno, Map<Integer, Director> map) {
		String sql = "SELECT md1.director_id AS d1, md2.director_id AS d2, COUNT(*) AS peso "
				+ "FROM movies m1, roles r1, movies_directors md1, movies m2, roles r2, movies_directors md2 "
				+ "WHERE m1.year=? AND m1.id=md1.movie_id AND r1.movie_id=m1.id AND m2.year=? AND m2.id=md2.movie_id AND r2.movie_id=m2.id AND r1.actor_id=r2.actor_id AND md1.director_id>md2.director_id "
				+ "GROUP BY md1.director_id,md2.director_id";
		List<Arco> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(map.containsKey(res.getInt("d1")) && map.containsKey(res.getInt("d2"))) {
					Arco a=new Arco(map.get(res.getInt("d1")), map.get(res.getInt("d2")), res.getInt("peso"));
					result.add(a);
				}
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException ("ERRORE DB", e);
		}
	}
	
	
	
	
	
	
}
