import java.util.ArrayList;

public class Commit {
    String title;
    ArrayList<File> filess = new ArrayList<>();
    ArrayList<String> cons = new ArrayList<>();
    void addFile(File file){
        filess.add(file);
    }
    boolean hasFile(String name){
        for (File f: filess) {
            if (f.name.equals(name))
                return true;
        }
        return false;
    }
    String getCon(String fileName){
        for (int i =0;i<filess.size();i++){
            if (filess.get(i).name.equals(fileName)){
                return cons.get(i);
            }
        }
        return null;
    }
    String getCon(int i){
        return cons.get(i);
    }

}
