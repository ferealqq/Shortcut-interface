package ahk.pkginterface.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.Collection;

public class KeyData {
    private Connection connection = null;
    private ResultSet resultSet = null;
    private Statement st = null;
    private PreparedStatement prepStatement = null;

    public KeyData(){
    }

    public Keys readJsonToKeys(){
        Gson gson = new Gson();
        final Type type = new TypeToken<Collection<Keys>>() {
        }.getType();
        JsonReader jsonReader = null;
        try {
            jsonReader = new JsonReader(new FileReader("SQL lauseet/test-keyboard-layout.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Keys keys = gson.fromJson(jsonReader,Keys.class);
        return keys;
    }

    private String setConnectionString(){
        return "jdbc:postgresql://localhost:5432/ahk-interface?user=postgres&password=pekka";
    }
    public void writeFirstrow() {
        try {
            connection = DriverManager.getConnection(setConnectionString());
            Keys ke = readJsonToKeys();
            for (int i = 0; i < ke.rows.size(); i++) {
                String sql = "insert into rowsUS (firstrow) values (?);";
                prepStatement = connection.prepareStatement(sql);
                prepStatement.setString(1,ke.firstrow.get(i).toString());
                prepStatement.executeQuery();
                prepStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection(connection);
        }

    }
    public static void main(String[] args) {
        new KeyData().writeFirstrow();
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
