package ahk.pkginterface.database;

import ahk.pkginterface.JBcrypt.BCrypt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProfileDB {

    private Connection connection = null;
    private ResultSet resultSet = null;
    private Statement st = null;
    private PreparedStatement prepStatement = null;
    private String[] connectionStrings = {"","",""};
    
    public ProfileDB() {
        // tämä on testi
        connectionStrings[0] = "jdbc:postgresql://localhost:5432/ahk-interface";
        connectionStrings[1] = "postgres";
        connectionStrings[2] = "pekka";
    }
/**
 *  Creates the user and sends the data to the database server
 */    
    public boolean createUser(String username,String password,String email){
        if(!isValidEmailAddress(email)) return false;
        password = BCrypt.hashpw(password, BCrypt.gensalt());
        try{
            connection = DriverManager.getConnection(connectionStrings[0],connectionStrings[1],connectionStrings[2]);
            String sqlquery = "Insert into profile (username,password,email) values (?,?,?)"; 
            prepStatement = connection.prepareStatement(sqlquery);
            prepStatement.setString(1,username);
            prepStatement.setString(2,password);
            prepStatement.setString(3,email);
            prepStatement.executeUpdate();
            prepStatement.close();
            connection.close();
            return true;
        }catch (Exception ex){
            System.out.println("Error in createUser : "+ ex);
            return false;
        }
    }
    private boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    public static void main(String[] args) {
        System.out.println(new ProfileDB().createUser("pekka","pekka","pekka@pekkas.fi"));
    }
}
