import java.util.ArrayList;
import java.util.Arrays;

public class Repository {
    ArrayList<File> files = new ArrayList<>();
    ArrayList<Commit> commits = new ArrayList<>();
    String name;

    public Repository(String name) {
        this.name = name;
    }

    void createFile(String fileName, String content) {
        files.add(new File(fileName, content));
    }

    boolean hasFileName(String name) {

        for (File f : files) {
            if (f.name.equals(name))
                return true;
        }
        return false;
    }
    boolean hasCommit(String name) {
        for (Commit c : commits) {
            if (c.title.equals(name))
                return true;
        }
        return false;
    }
    boolean isFile(){
        for (File f : files) {
            if (f.staged)
                return true;
        }
        return false;
    }
    void changeContent(String fileName, String newContent) {
        for (File f : files) {
            if (f.name.equals(fileName))
                f.content = newContent;
        }
    }
    void bazyabi(Commit commit) {
//        for (int i = 0 ; i < files.size(); i++) {
//
//            int z = -1;
//            for (int j = 0 ; j < commit.filess.size(); j++) {
//                int chek = 0;
//                z = j;
//                if (files.get(i).name.equals(commit.filess.get(j).name)){
//                    files.get(i).content = commit.getCon(j);
//                    chek = 1;
//                    break;
//                }
//                if (chek==0 && z!=-1){
//                    File f = new File(commit.filess.get(z).name,commit.getCon(z));
//                    files.add(f);
//                }
//            }
//
//        }
        for (int i =0 ; i< commit.filess.size();i++){
            int chek = -1;
            for (int j=0;j<files.size();j++){
                if (files.get(j).name.equals(commit.filess.get(i).name)){
                    files.get(j).content = commit.getCon(i);
                    chek =1;
                    break;
                }
            }
            if (chek==-1){
                File f = new File(commit.filess.get(i).name,commit.getCon(i));
                files.add(f);

            }
        }
    }
    void bazyabi(Commit commit, String fileName) {
        int chek = -1;
        int z = -1;
        for (int i = 0 ; i < files.size(); i++) {
            for (int j = 0 ; j < commit.filess.size(); j++) {
                if (files.get(i).name.equals(commit.filess.get(j).name)&&commit.filess.get(j).name.equals(fileName)){
                    files.get(i).content = commit.getCon(j);
                    chek = 0;
                }
                if (commit.filess.get(j).name.equals(fileName)){
                    z = j;
                }
            }
        }
        if (z!=-1 && chek == -1){
            File f = new File(commit.filess.get(z).name,commit.getCon(z));
            files.add(f);
        }
    }

    Commit getCommit(String title){
        for (Commit c: commits) {
            if (c.title.equals(title))
                return c;
        }
        return null;
    }
    boolean isFileInCommit(Commit commit, String fileName){
        for (File f: commit.filess) {
            if (f.name.equals(fileName))
                return true;
        }
        return false;
    }
    void addAll(){
        for (File f: files) {
            f.staged = true;
        }
    }
    void addFile(String name){
        for (File f: files) {
            if (f.name.equals(name)){
                f.staged = true;
            }
        }
    }
    void unStage(){
        for (File f: files)
            f.staged = false;
    }
    void removeFile(String name){
        File ff = null;
        for (File f: files) {
            if (f.name.equals(name)){
                ff = f;
                break;
            }
        }
        files.remove(ff);
    }
    void createCommit(String title){
        Commit c = new Commit();
        c.title = title;
        for (File f : files) {
            if (f.staged) {
                c.filess.add(f);
                c.cons.add(f.content);
                f.staged = false;
            }
        }
        commits.add(c);
    }
    File getFile (String name){
        for (File f: files) {
            if (f.name.equals(name))
                return f;
        }
        return null;
    }
    void status(){
        if (files.size()>0){
            for (File f: files) {
                if (f.staged) {
                    System.out.println(f.name+ " staged");
                } else {
                    System.out.println(f.name+" not staged");
                }
            }
        }else System.out.println("There are no files in the repository");
    }
    void log(){
        if (commits.size()>0) {
            int i = 1;
            for (Commit c : commits) {
                System.out.println(i + ". " + c.title);
                i++;
            }
        } else System.out.println("No commits have been done yet ");
    }
    void diff(String fileName, Commit commitOne, Commit commitTwo) {
        String con1 = commitOne.getCon(fileName);
        String con2 = commitTwo.getCon(fileName);
        String[] con11 = con1.split(" ");
        String[] con22 = con2.split(" ");
        for (String s: con11) {
            int chek = 0;
            for (String ss: con22) {
                if (s.equals(ss)){
                    System.out.print(s+" ");
                    chek = 1;
                    break;
                }
            }
            if (chek==0){
                System.out.print("*"+s+"* ");
            }
        }
        System.out.println();
        System.out.println();
        for (String s: con22) {
            int chek = 0;
            for (String ss: con11) {
                if (s.equals(ss)){
                    System.out.print(s+" ");
                    chek=1;
                    break;
                }
            }
            if (chek==0){
                System.out.print("*"+s+"* ");
            }
        }
        System.out.println();
    }

}
