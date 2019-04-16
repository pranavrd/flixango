package com.flixango.models;
import java.sql.*;
import java.util.ArrayList;

public class Member {
    public int MID;
    public String Name;
    public String Bio;
    public Date BDate;
    Connection con;

    public Member() {

    }

    public Member(Connection con, String name, String bio, Date bdate) {
        this.Name = name;
        this.Bio = bio;
        this.BDate = bdate;
        this.con = con;
    }

    public Member(Connection con, int mid, String name, String bio, Date bdate) {
        this.MID = mid;
        this.Name = name;
        this.Bio = bio;
        this.BDate = bdate;
        this.con = con;
    }

    public String toString() {
        String str = String.format("Name: %s, Bio: %s, BDate: %s", this.Name, this.Bio, this.BDate);
        return str;
    }

    public static Member create(Connection con, String name, String bio, Date bdate) {
        Member m = null;
        try {
            String query = "INSERT INTO Members (Name, Bio, BDate) VALUES (?, ?, ?)";
            String[] keys = {"MID"};
            PreparedStatement stmnt = con.prepareStatement(query, keys);
            stmnt.setString(1, name);
            stmnt.setString(2, bio);
            stmnt.setDate(3, bdate);
            int num = stmnt.executeUpdate();
            if (num > 0) {
                ResultSet rs = stmnt.getGeneratedKeys();
                rs.next();
                m = Member.findOneByID(con, rs.getInt(1));
            }
        } catch (Exception e) {
            System.out.println("Exception creating Member:" + e);
        }
        return m;
    }

    public static Member findOneByID(Connection con, int mid) {
        Member m = null;
        try {
            String query = "SELECT MID, Name, Bio, Bdate FROM Members WHERE MID = ?";
            PreparedStatement stmnt = con.prepareStatement(query);
            stmnt.setInt(1, mid);
            ResultSet rs = stmnt.executeQuery();
            rs.next();
            m = new Member(con, rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4));
        } catch (Exception e) {
            System.out.println("Exception finding Members by ID:" + e);
        }
        return m;
    }

    public static ArrayList<Member> findByName(Connection con, String Name) {
        ArrayList<Member> members = new ArrayList<>();
        try {
            String query = "SELECT MID, Name, Bio, BDate from Members WHERE Name LIKE ?";
            PreparedStatement stmnt = con.prepareStatement(query);
            stmnt.setString(1, "%" + Name + "%");
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                Member m = new Member(con, rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4));
                members.add(m);
            }
        } catch (Exception e) {
            System.out.println("Exception finding Members by name:" + e);
        }
        return members;
    }

    public boolean save() {
        boolean status = false;
        try {
            String query = "UPDATE Members SET Name=?, Bio=?, BDate=? WHERE MID=?";
            PreparedStatement stmnt = this.con.prepareStatement(query);
            stmnt.setString(1, this.Name);
            stmnt.setString(2, this.Bio);
            stmnt.setDate(3, this.BDate);
            stmnt.setInt(4, this.MID);
            int num = stmnt.executeUpdate();
            if (num > 0) {
                status = true;
            }
        } catch (Exception e) {
            System.out.println("Exception in saving members:" + e);
        }
        return status;
    }

}