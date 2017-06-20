package ahk.pkginterface.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class ActionsData {
    private Connection connection = null;
    private ResultSet resultSet = null;
    private Statement st = null;
    private PreparedStatement prepStatement = null;

    public ActionsData() {
    }

    private String setConnectionStrings() {
        return "jdbc:postgresql://localhost:5432/ahk-interface?user=postgres&password=pekka";
    }

    public ArrayList<Actions> getActions() {
        ArrayList<Actions> Actions = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(setConnectionStrings());
            String sql = "select Action,Path,keyWords from Actions;";
            prepStatement = connection.prepareStatement(sql);
            resultSet = prepStatement.executeQuery();
            while (resultSet.next()) {
                String path = resultSet.getString("Path");
                String action = resultSet.getString("Action");
                if (Objects.nonNull(resultSet.getArray("keyWords"))) {
                    Array keyWords = resultSet.getArray("keyWords");
                    if (!keyWords.toString().isEmpty()) {
                        Actions acc = new Actions(path, action, (String[]) keyWords.getArray());
                        Actions.add(acc);
                    } else {
                        Actions acc = new Actions(path, action, null);
                        Actions.add(acc);
                    }
                } else {
                    Actions acc = new Actions(path, action, null);
                    Actions.add(acc);
                }
            }
            prepStatement.close();
            return Actions;
        } catch (Exception ex) {
            System.out.println("Error in getActions " + ex);
            return null;
        } finally {
            closeConnection(connection);
        }
    }

    public ArrayList<Actions> searchAction(String search) {
        ArrayList<Actions> actions = getActions();
        ArrayList<Actions> results = new ArrayList<>();
        for (Actions action : actions) {
            if (action.getAction().toLowerCase().contains(search.toLowerCase())) {
                results.add(action);
            } else if(Objects.nonNull(action.keyWords)) {
                for (String keyWord : action.keyWords) {
                    if (keyWord.toLowerCase().contains(search.toLowerCase())) {
                        results.add(action);
                        break;
                    }
                }
            }
        }
        return results;
    }

    public static void main(String[] args) {
        ActionsData k = new ActionsData();
        System.out.println(k.getActions());
        System.out.println(k.searchAction("stop"));
        /*FileReader fr = null;
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
    */
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