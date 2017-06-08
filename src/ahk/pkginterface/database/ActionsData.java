package ahk.pkginterface.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;

public class ActionsData {
    private Connection connection = null;
    private ResultSet resultSet = null;
    private Statement st = null;
    private PreparedStatement prepStatement = null;
    private String[] connectionStrings = {"","",""};

    public ActionsData(){
        connectionStrings[0] = "jdbc:postgresql://localhost:5432/ahk-interface";
        connectionStrings[1] = "postgres";
        connectionStrings[2] = "pekka";
    }
    private String setConnectionStrings(){
        return connectionStrings[0]+connectionStrings[1]+connectionStrings[2];
    }
    public ArrayList<Actions> getActions() {
        ArrayList<Actions> Actions = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(setConnectionStrings());
            String sql = "select Action,Path from Actions;";
            prepStatement = connection.prepareStatement(sql);
            resultSet = prepStatement.executeQuery();
            while (resultSet.next()) {
                String path = resultSet.getString("Path");
                String action = resultSet.getString("Action");
                Actions acc = new Actions(path,action);
                Actions.add(acc);
            }
            prepStatement.close();
            return Actions;
        } catch (Exception ex) {
            System.out.println("Error in getActionsInMap" + ex);
            return null;
        } finally {
            closeConnection(connection);
        }
    }
    public ArrayList<Actions> searchAction(String search){
        ArrayList<Actions> actions = getActions();
        ArrayList<Actions> results = new ArrayList<>();
        for (Actions action: actions) {
            if(action.getAction().toLowerCase().contains(search.toLowerCase())) results.add(action);
        }
        return results;
    }

    public static void main(String[] args) {
        FileReader fr = null;
        BufferedReader br = null;
        ActionsData k = new ActionsData();
        try{
            for (Actions actions:k.getActions()) {
                fr = new FileReader(actions.getPath());
                br = new BufferedReader(fr);
                String sCurrentLine;

                br = new BufferedReader(new FileReader(actions.getPath()));

                while ((sCurrentLine = br.readLine()) != null) {
                    System.out.println(sCurrentLine);
                }
            }
        }catch (Exception ex){
        }
    }
    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
            }
        }
    }
}