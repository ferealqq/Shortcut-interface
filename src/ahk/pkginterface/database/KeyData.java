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
      /*  Gson gson = new Gson();
        final Type type = new TypeToken<Collection<Keys>>() {
        }.getType();
        JsonReader jsonReader = new JsonReader(new FileReader("SQL lauseet/US-keyboard-layout.json"));
        Keys keys = gson.fromJson(jsonReader, Keys.class);
        */
        return new Keys();
    }

    public Keys getRowsUsToKeys() {
        Keys keys = new Keys();
        getFirstRow(keys);
        getSecondRow(keys);
        getThirdrow(keys);
        getFourthRow(keys);
        getFifthRow(keys);
        getSixthRow(keys);
        keys.addRowsToArrayListRows();
        return keys;
    }

    public void getFirstRow(Keys keys) {
        try {
            connection = DriverManager.getConnection(setConnectionString());
            String sql = "select firstrow from rowsus;";
            prepStatement = connection.prepareStatement(sql);
            resultSet = prepStatement.executeQuery();
            while (resultSet.next()) {
                String[] key = resultSet.getString("firstrow").split(" ");
                if(key.length == 0) return;
                keys.firstrow.add(new Key(key[0],key[1]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void getSecondRow(Keys keys) {
        try {
            connection = DriverManager.getConnection(setConnectionString());
            String sql = "select secondrow from rowsus;";
            prepStatement = connection.prepareStatement(sql);
            resultSet = prepStatement.executeQuery();
            while (resultSet.next()) {
                String[] key = resultSet.getString("secondrow").split(" ");
                if(key.length == 0) return;
                keys.secondrow.add(new Key(key[0],key[1]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void getThirdrow(Keys keys) {
        try {
            connection = DriverManager.getConnection(setConnectionString());
            String sql = "select thirdrow from rowsus;";
            prepStatement = connection.prepareStatement(sql);
            resultSet = prepStatement.executeQuery();
            while (resultSet.next()) {
                String[] key = resultSet.getString("thirdrow").split(" ");
                if(key.length == 0) return;
                keys.thirdrow.add(new Key(key[0],key[1]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void getFourthRow(Keys keys) {
        try {
            connection = DriverManager.getConnection(setConnectionString());
            String sql = "select fourthrow from rowsus;";
            prepStatement = connection.prepareStatement(sql);
            resultSet = prepStatement.executeQuery();
            while (resultSet.next()) {
                String[] key = resultSet.getString("fourthrow").split(" ");
                if(key.length == 0) return;
                keys.fourthrow.add(new Key(key[0],key[1]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void getFifthRow(Keys keys) {
        try {
            connection = DriverManager.getConnection(setConnectionString());
            String sql = "select fifthrow from rowsus;";
            prepStatement = connection.prepareStatement(sql);
            resultSet = prepStatement.executeQuery();
            while (resultSet.next()) {
                String[] key = resultSet.getString("fifthrow").split(" ");
                if(key.length == 0) return;
                keys.fifthrow.add(new Key(key[0],key[1]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void getSixthRow(Keys keys) {
        try {
            connection = DriverManager.getConnection(setConnectionString());
            String sql = "select sixthrow from rowsus;";
            prepStatement = connection.prepareStatement(sql);
            resultSet = prepStatement.executeQuery();
            while (resultSet.next()) {
                String[] key = resultSet.getString("sixthrow").split(" ");
                if(key.length == 0) return;
                keys.sixthrow.add(new Key(key[0],key[1]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String setConnectionString() {
        return new secretConnection().setConnectionStrings();
    }

    /*
    * writes the rows to the database.
    * remember to delete this method before the release of this program
    * this function can be exploited to ruin the databse which the code is connected to
     */
    public void writeRowsUS() throws SQLException, FileNotFoundException {
        if (checkIfDataExistsAlready()) {
            Keys keys = readKeyboardLayoutUSToKeys();
            keys.addRowsToArrayListRows();
            ArrayList<ArrayList> rows = keys.rows;
            int longestRow = sortRows(); //sortRows gets the longestRow and returns it.
            connection = DriverManager.getConnection(setConnectionString());
            for (int keyCount = 0; keyCount <= longestRow; keyCount++) {
                String sql = "insert into rowsus (firstrow,secondrow,thirdrow,fourthrow,fifthrow,sixthrow) values (?,?,?,?,?,?)";
                prepStatement = connection.prepareStatement(sql);
                setFirstrow(prepStatement, keys, keyCount);
                setSecondrow(prepStatement, keys, keyCount);
                setThirdrow(prepStatement, keys, keyCount);
                setFourthrow(prepStatement, keys, keyCount);
                setFifthrow(prepStatement, keys, keyCount);
                setSixthrow(prepStatement, keys, keyCount);
                prepStatement.execute();
                prepStatement.close();
            }
            closeConnection(connection);
        }
    }

    public ArrayList<ArrayList<Key>> getKeysInArrayLists() {
        ArrayList<ArrayList<Key>> theRealList = new ArrayList<>();
        try {
            Keys keys = new KeyData().readKeyboardLayoutUSToKeys();
            keys.addRowsToArrayListRows();
            ArrayList<ArrayList> k = keys.rows;
            for (ArrayList<Key> keek : k) {
                theRealList.add(keek);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            return theRealList;
        }
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
        if (resultSet.next()) {
            if (resultSet.getInt("count") == 0) return true;
        }
        prepStatement.close();
        resultSet.close();
        closeConnection(connection);
        return false;
    }

    /*
    * sortRows gets the longest row and returns the size of the row.
     */
    public int sortRows() throws FileNotFoundException, SQLException {
        Keys keys = readKeyboardLayoutUSToKeys();
        keys.addRowsToArrayListRows();
        ArrayList<ArrayList> arrayLists = keys.rows;
        int arrayListIndex = arrayLists.size() - 1;
        int longest = 0;
        for (ArrayList<Key> arrayList : arrayLists) {
            if (arrayList.size() > arrayLists.get(arrayListIndex).size()) longest = arrayList.size();
            arrayListIndex--;
        }
        return longest;
    }

    private void setFirstrow(PreparedStatement prepst, Keys keys, int i) throws SQLException {
        if (keys.firstrow.size() <= i) {
            prepst.setString(1, " ");
            return;
        }
        prepst.setString(1, keys.firstrow.get(i).toString());
    }

    private void setSecondrow(PreparedStatement prepst, Keys keys, int i) throws SQLException {
        if (keys.secondrow.size() <= i) {
            prepst.setString(2, " ");
            return;
        }
        prepst.setString(2, keys.secondrow.get(i).toString());
    }

    private void setThirdrow(PreparedStatement prepst, Keys keys, int i) throws SQLException {
        if (keys.thirdrow.size() <= i) {
            prepst.setString(3, " ");
            return;
        }
        prepst.setString(3, keys.thirdrow.get(i).toString());
    }

    private void setFourthrow(PreparedStatement prepst, Keys keys, int i) throws SQLException {
        if (keys.fourthrow.size() <= i) {
            prepst.setString(4, " ");
            return;
        }
        prepst.setString(4, keys.fourthrow.get(i).toString());
    }

    private void setFifthrow(PreparedStatement prepst, Keys keys, int i) throws SQLException {
        if (keys.fifthrow.size() <= i) {
            prepst.setString(5, " ");
            return;
        }
        prepst.setString(5, keys.fifthrow.get(i).toString());
    }

    private void setSixthrow(PreparedStatement prepst, Keys keys, int i) throws SQLException {
        if (keys.sixthrow.size() <= i) {
            prepst.setString(6, " ");
            return;
        }
        prepst.setString(6, keys.sixthrow.get(i).toString());
    }

    public static void main(String[] args) throws FileNotFoundException, SQLException {
        KeyData keydata = new KeyData();
        keydata.getRowsUsToKeys();
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
