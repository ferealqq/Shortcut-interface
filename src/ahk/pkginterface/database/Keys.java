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
        rows.add(firstrow);
        rows.add(secondrow);
        rows.add(thirdrow);
        rows.add(fourthrow);
        rows.add(fifthrow);
        rows.add(sixthrow);
    }

    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        Key k = new Key("kuk", "suk");
        System.out.println(k.getKey() + "whathtaht");
        final Type type = new TypeToken<Collection<Keys>>() {
        }.getType();
        System.out.println("asd");
        JsonReader jsonReader = new JsonReader(new FileReader("SQL lauseet/test-keyboard-layout.json"));
        System.out.println("asd");
        Keys jotain = gson.fromJson(jsonReader,Keys.class);
        System.out.println(jotain.secondrow.get(1).getKeysynonyminahk());
    }
}
