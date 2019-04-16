import com.flixango.models.User;
import com.flixango.models.Movie;
import com.flixango.models.ReviewInclude;

import java.sql.*;

public class Review {

    public int RID;
    public int UserID;
    public int UMID;
    public String Review;
    public double Upvote;
    public Timestamp created_at;
    public User user;
    public Movie movie;
    Connection con;

    public Review() {

    }

    public Review(Connection con) {
        this.con = con;
    }

    public String toString() {
        String str = String.format("User: %s\n Movie:%s\n Review:%s, Upvote:%f, Created: %s", this.user, this.movie, this.Review, this.Upvote, this.created_at);
        return str;
    }

    public Review findByID(Connection con, int id, ReviewInclude include) {
        Review r = null;
        try {
            String query = "SELECT RID, UserID, UMID, Review, Upvote, created_at FROM reviews WHERE RID = ?";
            PreparedStatement stmnt = con.prepareStatement(query);
            stmnt.setInt(1, id);
            ResultSet rs = stmnt.executeQuery();
            rs.next();
            r = new Review(con);
            r.RID = rs.getInt(1);
            r.UserID = rs.getInt(2);
            r.UMID = rs.getInt(3);
            r.Review = rs.getString(4);
            r.Upvote = rs.getDouble(5);
            r.created_at = rs.getTimestamp(6);

            switch (include) {
                case MOVIE:
                    r.loadMovie();
                    break;
                case USER:
                    r.loadUser();
                    break;
                case USER_MOVIE:
                    r.loadUser();
                    r.loadMovie();
                    break;
            }
        } catch (Exception e) {
            System.out.println("Exception Finding review by id:" + e);
        }
        return r;
    }

    public boolean loadUser() {
        boolean status = false;
        try {
            this.user = User.findOneByID(this.con, this.UserID);
            if (user != null) {
                status = true;
            }
        } catch (Exception e) {
            System.out.println("Exception Loading user into review:" + e);
        }
        return status;
    }

    public boolean loadMovie() {
        boolean status = false;
        try {
            this.movie = Movie.findOneByID(this.con, this.UMID);
            if (movie != null) {
                status = true;
            }
        } catch (Exception e) {
            System.out.println("Exceptino Loading movie into review:" + e);
        }
        return status;
    }

    public static Review create(Connection con, String review, double upvote, User user, Movie movie) {
        Review r = null;
        try {
            String query = "INSERT INTO Reviews (UserID, UMID, Review, Upvote) VALUES(?, ?, ?, ?)";
            String[] keys = {"RID"};
            PreparedStatement stmnt = con.prepareStatement(query, keys);
            stmnt.setInt(1, user.ID);
            stmnt.setInt(2, movie.UMID);
            stmnt.setString(3, review);
            stmnt.setDouble(4, upvote);
            int num = stmnt.executeUpdate();
            if (num > 0) {
                ResultSet rs = stmnt.getGeneratedKeys();
                rs.next();
                r = Review.findByID(con, rs.getInt(1), ReviewInclude.USER_MOVIE);
            }

        } catch (Exception e) {
            System.out.println("Exception Creating review:" + e);
        }
        return r;
    }

    public boolean save() {
        boolean status = false;
        try {
            String query = "UPDATE Reviews SET Review=?, upvote=? WHERE RID = ?";
            PreparedStatement stmnt = this.con.prepareStatement(query);
            stmnt.setString(1, this.Review);
            stmnt.setDouble(2, this.Upvote);
            stmnt.setInt(3, this.RID);
            int num = stmnt.executeUpdate();
            if (num > 0) {
                status = true;
            }
        } catch (Exception e) {
            System.out.println("Exception Saving reviews: " + e);
        }
        return status;
    }
}