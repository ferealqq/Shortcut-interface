package ahk.pkginterface.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;

public class KeyData {
    private Connection connection = null;
    private ResultSet resultSet = null;
    private Statement st = null;
    private PreparedStatement prepStatement = null;

    public KeyData() {
    }

    public Keys readKeyboardLayoutUSToKeys() throws FileNotFoundException {
        Gson gson = new Gson();
        final Type type = new TypeToken<Collection<Keys>>() {
        }.getType();
        JsonReader jsonReader = new JsonReader(new FileReader("SQL lauseet/test-keyboard-layout.json"));
        Keys keys = gson.fromJson(jsonReader, Keys.class);
        return keys;
    }

    private String setConnectionString() {
        return "jdbc:postgresql://localhost:5432/ahk-interface?user=postgres&password=pekka";
    }

    /*
    * writes the rows to the database.
    * remember to delete this method before the release of the program
    * this can be exploited to ruin the databse the program is running on.
     */
    public void writeRowsUS() throws SQLException, FileNotFoundException {
        Keys keys = readKeyboardLayoutUSToKeys();
        keys.addRowsToArrayListRows();
        ArrayList<ArrayList> rows = keys.rows;
        int longestRow = sortRows(); //sortRows gets the longestRow and returns it.
        connection = DriverManager.getConnection(setConnectionString());
        for (int keyCount = 0; keyCount <= longestRow; keyCount++) {
            String sql = "insert into rowsus (firstrow,secondrow,thirdrow,fourthrow,fifthrow,sixthrow) values (?,?,?,?,?,?)";
            prepStatement = connection.prepareStatement(sql);
            writeFirstrow(prepStatement,keys,keyCount);
            writeSecondrow(prepStatement,keys,keyCount);
            writeThridrow(prepStatement,keys,keyCount);
            writeFourthrow(prepStatement,keys,keyCount);
            writeFifthrow(prepStatement,keys,keyCount);
            writeSixthrow(prepStatement,keys,keyCount);
            prepStatement.execute();
            prepStatement.close();
        }
        closeConnection(connection);
    }

    /*
    * check if the data has been written to the databse already.
    * returns true if data already exists if not returns false
     */
    public boolean checkIfDataExistsAlready() throws SQLException {
        connection = DriverManager.getConnection(setConnectionString());
        String sql = "select COUNT(*) from rowsus";
        prepStatement = connection.prepareStatement(sql);
        resultSet = prepStatement.executeQuery();
        if(resultSet.next()){
            if(resultSet.getInt("count")==0) return false;
        }
        prepStatement.close();
        resultSet.close();
        closeConnection(connection);
        return true;
    }
    /*
    * sortRows gets the longest row and returns the size of the row.
     */
    public int sortRows() throws FileNotFoundException {
        Keys keys = readKeyboardLayoutUSToKeys();
        keys.addRowsToArrayListRows();
        ArrayList<ArrayList>arrayLists = keys.rows;
        int arrayListIndex = arrayLists.size() - 1;
        int longest = 0;
        for (ArrayList<Key> arrayList : arrayLists) {
            if(arrayList.size()>arrayLists.get(arrayListIndex).size()) longest = arrayList.size();
            arrayListIndex--;
        }
        return longest;
    }

    public void writeFirstrow(PreparedStatement prepst, Keys keys, int i) throws SQLException {
        if(keys.firstrow.size() <= i) {
            prepst.setString(1," ");
            return;
        }
        prepst.setString(1, keys.firstrow.get(i).toString());
    }
    public void writeSecondrow(PreparedStatement prepst,Keys keys,int i) throws SQLException {
        if(keys.secondrow.size() <= i) {
            prepst.setString(2," ");
            return;
        }
        prepst.setString(2, keys.secondrow.get(i).toString());
    }

    public void writeThridrow(PreparedStatement prepst, Keys keys, int i) throws SQLException {
        if(keys.thirdrow.size() <= i) {
            prepst.setString(3," ");
            return;
        }
        prepst.setString(3, keys.thirdrow.get(i).toString());
    }
    public void writeFourthrow(PreparedStatement prepst, Keys keys, int i) throws SQLException{
        if(keys.fourthrow.size() <= i) {
            prepst.setString(4," ");
            return;
        }
        prepst.setString(4,keys.fourthrow.get(i).toString());
    }
    public void writeFifthrow(PreparedStatement prepst, Keys keys, int i) throws  SQLException{
        if(keys.fifthrow.size() <= i) {
            prepst.setString(5," ");
            return;
        }
        prepst.setString(5,keys.fifthrow.get(i).toString());
    }
    public void writeSixthrow(PreparedStatement prepst, Keys keys, int i) throws SQLException{
        if(keys.sixthrow.size() <= i) {
            prepst.setString(6," ");
            return;
        }
        prepst.setString(6,keys.sixthrow.get(i).toString());
    }

    public static void main(String[] args) throws FileNotFoundException, SQLException {
        KeyData keydata =new KeyData();
        Keys k = new KeyData().readKeyboardLayoutUSToKeys();
        k.addRowsToArrayListRows();
        System.out.println(k.rows.get(1).get(1).getClass());
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
