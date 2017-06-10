package ahk.pkginterface.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

public class Keys {

    public ArrayList<Key> firstrow = new ArrayList<>();
    public ArrayList<Key> secondrow = new ArrayList<>();
    public ArrayList<Key> thirdrow = new ArrayList<>();
    public ArrayList<Key> fourthrow = new ArrayList<>();
    public ArrayList<Key> fifthrow = new ArrayList<>();
    public ArrayList<Key> sixthrow = new ArrayList<>();

    public final ArrayList<ArrayList> rows = new ArrayList<>();

    public Keys() {
    }
    /*
    * addRowsToArrayListRows have to be runned when you want the arraylist rows to work.
    * The rows will be null if this method wouldnt be runned.
    * When made I tried to add it to constructor but wasnt able to, because when you run the readtojson from KeyData it doesn't create Keys class. It only reads json file to get the data and saves it in a keys. But dosn't call the constructor.
     */
    public void addRowsToArrayListRows(){
        rows.add(firstrow);
        rows.add(secondrow);
        rows.add(thirdrow);
        rows.add(fourthrow);
        rows.add(fifthrow);
        rows.add(sixthrow);
    }

    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        final Type type = new TypeToken<Collection<Keys>>() {
        }.getType();
        JsonReader jsonReader = new JsonReader(new FileReader("SQL lauseet/test-keyboard-layout.json"));
        Keys jotain = gson.fromJson(jsonReader,Keys.class);
    }
}