public class File {
    String name;
    String content;
    boolean staged = false;
    public File(String name, String content){
        this.name = name;
        this.content = content;
    }
    String getContent(){
        return content;
    }

}
