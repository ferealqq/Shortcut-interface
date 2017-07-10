package ahk.pkginterface.database;

import java.util.ArrayList;

public class Actions {
    /*
    * @param action the action thats in the folder of the path.
    * @param path a path that the action code is in.
     */
    public final String path;
    public final String action;
    //public final ArrayList<String> keyWords = new ArrayList<>();
    public String[] keyWords;
    public Actions(String p,String a,String[] keywords){
        keyWords = keywords;
        path = p;
        action = a;
    }
    public String getAction(){
        return action;
    }
    public String getPath(){
        return path;
    }
    public String toString(){
        return "" + path +" "+ action;
    }
}
