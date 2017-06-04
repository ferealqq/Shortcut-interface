package ahk.pkginterface.database;
public class Actions {
    private String path;
    private String action;
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
