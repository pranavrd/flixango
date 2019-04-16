package com.flixango.models;

import java.sql.*;
import java.util.ArrayList;

public class User {
    int ID;
    public String Name;
    public String EMail;
    public Long Phone;
    public String Password;
    ArrayList<WatchList> watchLists;
    Connection con;

    public User() {
        this.watchLists = new ArrayList<>();
    }

    public User(Connection con, int ID, String Name, String EMail, Long Phone, String Password) {
        this.con = con;
        this.ID = ID;
        this.Name = Name;
        this.EMail = EMail;
        this.Phone = Phone;
        this.Password = Password;
        this.watchLists = new ArrayList<>();
    }

    public User(Connection con, String Name, String Email, Long Phone, String Password) {
        this.con = con;
        this.Name = Name;
        this.EMail = Email;
        this.Phone = Phone;
        this.Password = Password;
        this.watchLists = new ArrayList<>();
    }

    @Override
    public String toString() {
        String str = String.format("Name: %s, Email: %s, Phone:%d, Password: %s", this.Name, this.EMail, this.Phone, this.Password);
        return str;
    }

    public boolean save() {
        boolean status = false;
        try {
            String query = "UPDATE Users SET Name=?, Email=?, Phone=?, Password=?";
            PreparedStatement stmnt = con.prepareStatement(query);
            stmnt.setString(1, this.Name);
            stmnt.setString(2, this.EMail);
            stmnt.setLong(3, this.Phone);
            stmnt.setString(4, this.Password);
            int num = stmnt.executeUpdate();
            if (num > 0) {
                status = true;
                System.out.println("User Saved Successfully");
            } else {
                System.out.println("User save unsuccessful");
            }
        } catch (Exception e) {
            System.out.println("Exception saving Users:" + e);
        }
        return status;
    }

    public static User create(Connection con, String Name, String Email, Long Phone, String Password) {
        User u = null;
        try {
            String query = "INSERT INTO Users (Name, Email, Phone, Password) VALUES (?, ?, ?, ?)";
            String[] keys = {"ID"};
            PreparedStatement stmnt = con.prepareStatement(query, keys);
            stmnt.setString(1, Name);
            stmnt.setString(2, Email);
            stmnt.setLong(3, Phone);
            stmnt.setString(4, Password);
            int num = stmnt.executeUpdate();
            if (num > 0) {
                ResultSet rs = stmnt.getGeneratedKeys();
                rs.next();
                u = User.findOneByID(con, rs.getInt(1));
            }
        } catch (Exception e) {
            System.out.println("Exception Creating user:" + e);
        }
        return u;
    }

    public static User findByEMail(Connection con, String email) {
        User u = null;
        try {
            String query = "SELECT ID, Name, Email, Phone, Password FROM Users WHERE EMail= ?";
            PreparedStatement stmnt = con.prepareStatement(query);
            stmnt.setString(1, email);
            ResultSet rs = stmnt.executeQuery();
            rs.next();
            u = new User(con, rs.getInt(1), rs.getString(2), rs.getString(3), rs.getLong(4), rs.getString(5));
        } catch (Exception e) {
            System.out.println("Exception Finding user by Email:" + e);
        }
        return u;
    }

    public static ArrayList<User> findByName(Connection con, String name) {
        ArrayList<User> users = new ArrayList<>();
        try {
            String query = "SELECT ID, Name, Email, Phone, Password FROM Users WHERE Name LIKE ?";
            PreparedStatement stmnt = con.prepareStatement(query);
            stmnt.setString(1, "%" + name + "%");
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                User u = new User(con, rs.getInt(1), rs.getString(2), rs.getString(3), rs.getLong(4), rs.getString(5));
                users.add(u);
            }
        } catch (Exception e) {

        }
        return users;
    }

    public static User findOneByID(Connection con, int ID) {
        User u = null;
        try {
            String query = "SELECT ID, Name, Email, Phone, Password FROM Users WHERE ID= ?";
            PreparedStatement stmnt = con.prepareStatement(query);
            stmnt.setInt(1, ID);
            ResultSet rs = stmnt.executeQuery();
            rs.next();
            u = new User(con, rs.getInt(1), rs.getString(2), rs.getString(3), rs.getLong(4), rs.getString(5));
        } catch (Exception e) {
            System.out.println("Exception Finding user by ID:" + e);
        }
        return u;
    }

    public ArrayList<WatchList> getWatchLists() {
        try {
            String query = "SELECT WID, CreatorID, Name, created_at FROM TABLE(get_watchlist_for_user(?))";
            PreparedStatement stmnt = this.con.prepareStatement(query);
            stmnt.setInt(1, this.ID);
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                WatchList w = new WatchList(this.con, rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getTimestamp(4));
                this.watchLists.add(w);
            }
        } catch (Exception e) {
            System.out.println("Exception getting watchlist for user:" + e);
        }
        return this.watchLists;
    }
}