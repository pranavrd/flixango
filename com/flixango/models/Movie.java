package com.flixango.models;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Movie {
    public int UMID;
    public String Name;
    public String Language;
    public String Duration;
    public double Rating;
    public int CCode;
    public ArrayList<Genre> genres;
    public ArrayList<Review> reviews;
    public ArrayList<Member> members;
    public HashMap<String, Member> cast;
    Connection con;

    public Movie() {
        this.genres = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.members = new ArrayList<>();
        this.cast = new HashMap<>();
    }

    public Movie(Connection con, String Name, String Language, String Duration, double Rating, int CCode) {
        this.con = con;
        this.Name = Name;
        this.Language = Language;
        this.Duration = Duration;
        this.Rating = Rating;
        this.CCode = CCode;
        this.genres = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.members = new ArrayList<>();
        this.cast = new HashMap<>();
    }


    public Movie(Connection con, int UMID, String Name, String Language, String Duration, double Rating, int CCode) {
        this.con = con;
        this.UMID = UMID;
        this.Name = Name;
        this.Language = Language;
        this.Duration = Duration;
        this.Rating = Rating;
        this.CCode = CCode;
        this.genres = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.members = new ArrayList<>();
        this.cast = new HashMap<>();
    }

    public String toString() {
        String str = String.format("Name: %s, Language: %s, Duration: %s, Rating:%f, CCode: %d", this.Name, this.Language, this.Duration, this.Rating, this.CCode);
        return str;
    }

    public boolean save() {
        boolean stat = false;
        try {
            String query = "UPDATE Movies SET Name=?, Language=?, Duration=?, Rating=?, CCode=? WHERE UMID=?";
            PreparedStatement stmnt = this.con.prepareStatement(query);
            stmnt.setString(1, this.Name);
            stmnt.setString(2, this.Language);
            stmnt.setString(3, this.Duration);
            stmnt.setDouble(4, this.Rating);
            stmnt.setInt(5, this.CCode);
            stmnt.setInt(6, this.UMID);
            int status = stmnt.executeUpdate();
            if (status == 1) {
                System.out.println("Movie Saved successfully");
                stat = true;
            } else {
                System.out.println("Movie Save error");
                stat = false;
            }

        } catch (Exception e) {
            System.out.println("Error saving Movie:" + e);
            stat = false;
        }
        return stat;
    }

    public static Movie create(Connection con, String name, String language, String duration, double rating, int ccode) {
        Movie m = null;
        try {
            String query = "INSERT INTO Movies (Name, Language, Duration, Rating, CCode) VALUES (?, ?, ?, ?, ?)";
            String keys[] = {"UMID"};
            PreparedStatement stmnt = con.prepareStatement(query, keys);
            stmnt.setString(1, name);
            stmnt.setString(2, language);
            stmnt.setString(3, duration);
            stmnt.setDouble(4, rating);
            stmnt.setInt(5, ccode);

            int num = stmnt.executeUpdate();
            if (num > 0) {
                ResultSet rs = stmnt.getGeneratedKeys();
                rs.next();
                m = Movie.findOneByID(con, rs.getInt(1));
            }
        } catch (Exception e) {
            System.out.println("Error Creating Movies:" + e);
        }
        return m;
    }

    public static Movie findOneByID(Connection con, int UMID) {
        Movie m = null;
        try {
            String query = "SELECT UMID, Name, Language, Duration, Rating, CCode FROM Movies WHERE UMID=?";
            PreparedStatement stmnt = con.prepareStatement(query);
            stmnt.setInt(1, UMID);
            ResultSet rs = stmnt.executeQuery();
            rs.next();
            m = new Movie(con, rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDouble(5), rs.getInt(6));
        } catch (Exception e) {
            System.out.println("Exception Finding Movies:" + e);
        }
        return m;
    }

    public static ArrayList<Movie> findByName(Connection con, String name) {
        ArrayList<Movie> m = new ArrayList<>();
        try {
            String query = "SELECT UMID, Name, Language, Duration, Rating, CCode FROM Movies WHERE Name LIKE ?";
            PreparedStatement stmnt = con.prepareStatement(query);
            stmnt.setString(1, "%" + name + "%");
            ResultSet rs = stmnt.executeQuery();

            while (rs.next()) {
                Movie movObj = new Movie();
                movObj.UMID = rs.getInt(1);
                movObj.Name = rs.getString(2);
                movObj.Language = rs.getString(3);
                movObj.Duration = rs.getString(4);
                movObj.Rating = rs.getDouble(5);
                movObj.CCode = rs.getInt(6);
                m.add(movObj);
            }
        } catch (Exception e) {
            System.out.println("Exception Finding movies by name:" + e);
        }
        return m;
    }

    public ArrayList<Genre> getGenres() {
        try {
            String query = "SELECT GID, Name FROM TABLE(get_genre_for_movie(?))";
            PreparedStatement stmnt = this.con.prepareStatement(query);
            stmnt.setInt(1, this.UMID);
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                Genre g = new Genre(con, rs.getInt(1), rs.getString(2));
                this.genres.add(g);
            }
        } catch (Exception e) {
            System.out.println("Exception getting genres for movie:" + e);
        }
        return this.genres;
    }

    public ArrayList<Review> getReviews() {
        try {
            String query = "SELECT RID, USERID, UMID, Review, Upvote, created_at FROM TABLE(get_reviews_for_movie(?))";
            PreparedStatement stmnt = this.con.prepareStatement(query);
            stmnt.setInt(1, this.UMID);
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                Review r = new Review(this.con, rs.getInt(1), rs.getInt(2), rs.getInt(3),
                        rs.getString(4), rs.getDouble(5), rs.getTimestamp(6));
                r.movie = this;
                this.reviews.add(r);
            }
        } catch (Exception e) {
            System.out.println("Exception retrieving reviews for movie: " + e);
        }
        return this.reviews;
    }

    public boolean addGenre(Genre g) {
        boolean status = false;
        try {
            String query = "INSERT INTO genre_type (GID, UMID) VALUES(?, ?)";
            PreparedStatement stmnt = this.con.prepareStatement(query);
            stmnt.setInt(1, g.GID);
            stmnt.setInt(2, this.UMID);
            int num = stmnt.executeUpdate();
            if (num > 0) {
                status = true;
                this.genres.add(g);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Genre already exists for this movie");
        } catch (Exception e) {
            System.out.println("Exception adding genre to movie: " + e);
        }
        return status;
    }

    public HashMap<String, Member> getCast() {
        try {
            String query = "SELECT MID, Name, Bio, BDate, Role FROM TABLE(get_cast_for_movie(?))";
            PreparedStatement stmnt = this.con.prepareStatement(query);
            stmnt.setInt(1, this.UMID);
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                Member m = new Member(this.con, rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4));
                this.members.add(m);
                this.cast.put(rs.getString(5), m);
            }
        } catch (Exception e) {
            System.out.println("Exception getting cast for movie:" + e);
        }
        return this.cast;
    }

    public boolean addCast(Member m, Role r) {
        boolean status = false;
        try {
            String query = "INSERT INTO cast (UMID, MID, roleID) VALUES(?, ?, ?)";
            PreparedStatement stmnt = this.con.prepareStatement(query);
            stmnt.setInt(1, this.UMID);
            stmnt.setInt(2, m.MID);
            stmnt.setInt(3, r.ID);
            int num = stmnt.executeUpdate();
            if (num > 0) {
                status = true;
                this.members.add(m);
                this.cast.put(r.Name, m);
            }
        } catch (Exception e) {
            System.out.println("Exception adding cast member" + e);
        }
        return status;
    }
}