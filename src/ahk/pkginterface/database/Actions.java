package ahk.pkginterface.database;
public class Actions {
    /*
    * @param action the action thats in the folder of the path.
    * @param path a path that the action code is in.
     */
    public final String path;
    public final String action;

    public Actions(String p,String a){
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
