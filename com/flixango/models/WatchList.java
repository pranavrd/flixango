package com.flixango.models;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

public class WatchList {
    public int WID;
    public int CreatorID;
    public String Name;
    public Timestamp created_at;
    public ArrayList<Movie> movies;

    Connection con;

    public WatchList() {
        this.movies = new ArrayList<>();
    }

    public WatchList(Connection con, int WID, int CreatorID, String name, Timestamp created_at) {
        this.WID = WID;
        this.CreatorID = CreatorID;
        this.Name = name;
        this.created_at = created_at;
        this.movies = new ArrayList<>();

        this.con = con;
    }

    public static WatchList findOneByID(Connection con, int ID) {
        WatchList w = null;
        try {
            String query = "SELECT WID, CreatorID, Name, created_at FROM Watchlist WHERE WID = ?";
            PreparedStatement stmnt = con.prepareStatement(query);
            stmnt.setInt(1, ID);
            ResultSet rs = stmnt.executeQuery();
            rs.next();
            w = new WatchList(con, rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getTimestamp(4));
        } catch (Exception e) {
            System.out.println("Exception finding watchlist by ID:" + e);
        }
        return w;
    }

    public static ArrayList<WatchList> findByName(Connection con, String name) {
        ArrayList<WatchList> lists = new ArrayList<>();
        try {
            String query = "SELECT WIL, CreatorID, Name, created_at FROM Watchlist WHERE Name LIKE ?";
            PreparedStatement stmnt = con.prepareStatement(query);
            stmnt.setString(1, "%" + name + "%");
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                WatchList w = new WatchList(con, rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getTimestamp(4));
                lists.add(w);
            }
        } catch (Exception e) {
            System.out.println("Exception in finding watchlist by name:" + e);
        }
        return lists;
    }

    public static WatchList create(Connection con, String name, User u) {
        WatchList w = null;
        try {
            String query = "INSERT INTO Watchlist (CreatorID, Name) VALUES(?, ?)";
            String[] keys = {"WID"};
            PreparedStatement stmnt = con.prepareStatement(query, keys);
            stmnt.setInt(1, u.ID);
            stmnt.setString(2, name);
            int num = stmnt.executeUpdate();
            if (num > 0) {
                ResultSet rs = stmnt.getGeneratedKeys();
                rs.next();
                w = WatchList.findOneByID(con, rs.getInt(1));
            }
        } catch (Exception e) {
            System.out.println("Exception creating Watchlist:" + e);
        }
        return w;
    }

    public boolean addMovie(Movie m) {
        boolean status = false;
        try {
            String query = "INSERT INTO contains (UMID, WID) VALUES(?,?)";
            PreparedStatement stmnt = this.con.prepareStatement(query);
            stmnt.setInt(1, m.UMID);
            stmnt.setInt(2, this.WID);
            int num = stmnt.executeUpdate();
            if (num > 0) {
                status = true;
                this.movies.add(m);
            }
        } catch (Exception e) {
            System.out.println("Exception Adding movie to Watchlist:" + e);
        }
        return status;
    }

    public ArrayList<Movie> getMovies() {
        try {
            String query = "SELECT UMID, Name, Language, Duration, Rating, CCode FROM TABLE(get_movies_for_watchlist(?))";
            PreparedStatement stmnt = this.con.prepareStatement(query);
            stmnt.setInt(1, this.WID);
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                Movie m = new Movie(this.con, rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getInt(6));
                this.movies.add(m);
            }
        } catch (Exception e) {
            System.out.println("Exception return movies for watchlist:" + e);
        }
        return this.movies;
    }

}