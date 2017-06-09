package ahk.pkginterface.database;
public class Key {
    public final String key;
    public final String keysynonyminahk;
    public Key(String k,String ks){
        key = k;
        keysynonyminahk = ks;
    }
    public String getKey(){
        return key;
    }
    public String getKeysynonyminahk(){
        return keysynonyminahk;
    }
    public String toString(){
        return key + " " + keysynonyminahk;
    }
}
