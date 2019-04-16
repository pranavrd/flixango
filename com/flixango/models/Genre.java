package com.flixango.models;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Genre {
    public int GID;
    public String Name;

    Connection con;

    public Genre() {

    }

    public Genre(Connection con, int GID, String Name) {
        this.con = con;
        this.GID = GID;
        this.Name = Name;
    }

    public Genre(Connection con, String Name) {
        this.con = con;
        this.Name = Name;
    }

    public String toString() {
        String str = String.format("Name: %s", this.Name);
        return str;
    }

    public static Genre create(Connection con, String name) {
        Genre g = null;
        try {
            String query = "INSERT INTO GENRE (Name) VALUES (?)";
            String keys[] = {"GID"};
            PreparedStatement stmnt = con.prepareStatement(query, keys);
            stmnt.setString(1, name);
            int num = stmnt.executeUpdate();
            if (num > 0) {
                ResultSet rs = stmnt.getGeneratedKeys();
                rs.next();
                g = Genre.findOneByID(con, rs.getInt(1));
            }
        } catch (Exception e) {
            System.out.println("Exception creating genre:" + e);
        }
        return g;
    }

    public boolean save() {
        boolean status = false;
        try {
            String query = "UPDATE Genre SET Name=? WHERE GID=?";
            PreparedStatement stmnt = this.con.prepareStatement(query);
            stmnt.setString(1, this.Name);
            stmnt.setInt(2, this.GID);
            int num = stmnt.executeUpdate();
            if (num > 0) {
                status = true;
            }
        } catch (Exception e) {
            System.out.println("Exception saving genre:" + e);
        }
        return status;
    }

    public static Genre findOneByID(Connection con, int GID) {
        Genre g = null;
        try {
            String query = "SELECT GID, Name FROM Genre WHERE GID=?";
            PreparedStatement stmnt = con.prepareStatement(query);
            stmnt.setInt(1, GID);
            ResultSet rs = stmnt.executeQuery();
            rs.next();
            g = new Genre(con, rs.getInt(1), rs.getString(2));
        } catch (Exception e) {
            System.out.println("Exception finding genre by id:" + e);
        }
        return g;
    }
}