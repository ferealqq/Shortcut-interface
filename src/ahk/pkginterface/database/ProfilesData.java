package ahk.pkginterface.database;

import ahk.pkginterface.JBcrypt.BCrypt;

import java.sql.*;

public class ProfilesData {

    private Connection connection = null;
    private ResultSet resultSet = null;
    private Statement st = null;
    private PreparedStatement prepStatement = null;
    private String[] connectionStrings = {"","",""};

    public ProfilesData() {
    }

    private String setConnectionStrings(){
        return "jdbc:postgresql://localhost:5432/ahk-interface?user=postgres&password=pekka";
    }

    public boolean createUser(String username, String email, String password){
        if(!isValidEmailAddress(email)) return false;
        password = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("?");
        try{
            connection = DriverManager.getConnection(setConnectionStrings());
            String sqlquery = "Insert into profile (username,password,email) values (?,?,?)";
            prepStatement = connection.prepareStatement(sqlquery);
            prepStatement.setString(1,username);
            prepStatement.setString(2,password);
            prepStatement.setString(3,email);
            prepStatement.executeUpdate();
            System.out.println("?");
            prepStatement.close();
            return true;
        }catch (SQLException ex){
            System.out.println("Error in createUser : "+ ex);
            return false;
        }finally {
            closeConnection(connection);
        }
    }
    public boolean checkUsername(String username){
        // gets data from database to check if the username is taken or not. If the username is taken returns false; if not return true;
        try{
            connection = DriverManager.getConnection(setConnectionStrings());
            String sqlquery = "select username from profile where username = ?";
            prepStatement = connection.prepareStatement(sqlquery);
            prepStatement.setString(1, username);
            resultSet = prepStatement.executeQuery();
            if(resultSet.next()) return false;
            prepStatement.close();
            return true;
        }catch (Exception ex){
            System.out.println("Error in checkUsername : "+ ex);
            return false;
        }finally {
            closeConnection(connection);
        }
    }
    public boolean checkEmail(String email) {
        // gets data from database to check if the email is taken or not. If the email is taken returns false; if not return true;
        try {
            connection = DriverManager.getConnection(setConnectionStrings());
            String sqlquery = "select email from profile where email = ?";
            prepStatement = connection.prepareStatement(sqlquery);
            prepStatement.setString(1, email);
            resultSet = prepStatement.executeQuery();
            if (resultSet.next()) return false;
            prepStatement.close();
            return true;
        } catch (Exception ex) {
            System.out.println("Error in checkEmail : " + ex);
            return false;
        } finally {
            closeConnection(connection);
        }
    }
    public boolean checkPassword(String password,String username){
        try{
            connection=DriverManager.getConnection(setConnectionStrings());
            String sqlquery = "select password from profile where username=?";
            prepStatement = connection.prepareStatement(sqlquery);
            prepStatement.setString(1,username);
            resultSet = prepStatement.executeQuery();
            String hashed ="";
            if(resultSet.next()) hashed = resultSet.getString("password");
            if(!BCrypt.checkpw(password, hashed)) return false;
            prepStatement.close();
            return true;
        }catch (Exception ex){
            System.out.println("Error in checkPassword"+ex);
            return false;
        }finally {
            closeConnection(connection);
        }
    }
    private boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    public int getProileIdByUsername(String username){
        try{
            connection = DriverManager.getConnection(setConnectionStrings());
            String sqlquery = "select profile_id from profile where username = ?";
            prepStatement = connection.prepareStatement(sqlquery);
            prepStatement.setString(1,username);
            resultSet = prepStatement.executeQuery();
            if(resultSet.next()) return resultSet.getInt("profile_id");
            prepStatement.close();
        }catch(Exception ex){
            System.out.println("Error in getProfileIdByUsername : "+ex);
        }finally {
            closeConnection(connection);
        }
        return 0;
    }
    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
            }
        }
    }
    public static void main(String[] args) {
        System.out.println(new ProfilesData().createUser("peke","peke@peke.fi","peke"));
    }
}
