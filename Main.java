import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static ArrayList<String> repNames = new ArrayList<>();
    static ArrayList<Repository> reps = new ArrayList<>();

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String command;
        Matcher matcher;
        String R_rep = "\\s*new\\s+repository\\s+(?<repName>[\\s\\S]+)\\s*";
        String R_open = "\\s*open\\s+repository\\s+(?<repName>[\\s\\S]+)\\s*";
        String F_create = "\\s*new\\s+file\\s+\"(?<fileName>[\\s\\S]*)\"\\s+\"(?<fileContent>[\\s\\S]*)\"\\s*";
        String ChContent = "\\s*edit\\s+file\\s+\"(?<fileName>[\\s\\S]*)\"\\s+\"(?<fileContent>[\\s\\S]*)\"\\s*";
        String dif1 = "\\s*diff\\s+\"(?<fileName>[\\s\\S]*)\"\\s+\"(?<commitOne>[\\s\\S]+)\"\\s+\"(?<commitTwo>[\\s\\S]+)\"\\s*";
        String dif2 = "\\s*diff\\s+\"(?<fileName>[\\s\\S]*)\"\\s+(?<commitOne>\\d+)\\s+\"(?<commitTwo>[\\s\\S]+)\"\\s*";
        String dif3 = "\\s*diff\\s+\"(?<fileName>[\\s\\S]*)\"\\s+\"(?<commitOne>[\\s\\S]+)\"\\s+(?<commitTwo>\\d+)\\s*";
        String dif4 = "\\s*diff\\s+\"(?<fileName>[\\s\\S]*)\"\\s+(?<commitOne>\\d+)\\s+(?<commitTwo>\\d+)\\s*";
        command = scanner.nextLine();
        Repository openRep = null;
        while (!command.matches("\\s*end\\s*")){
            if (command.matches(R_rep)){
                matcher = getMatcher(command, R_rep);
                String repName = matcher.group("repName");
                repName = repName.trim();

                if (!hasRepName(repName)){
                    System.out.println("Repository created successfully! ");
                    Repository e = new Repository(repName);
                    reps.add(e);
                    repNames.add(repName);

                } else System.out.println("Repository with this name already exists ");
            } else if (command.matches(R_open)){
                matcher = getMatcher(command, R_open);
                String repName = matcher.group("repName");
                repName = repName.trim();
                if (hasRepName(repName)){
                    System.out.println("Repository "+ repName +" opened successfully! ");
                    openRep = findRep(repName);
                }else System.out.println("No repository exists with this name ");
            } else if (command.matches(F_create)){
                matcher = getMatcher(command, F_create);
                String fileName = matcher.group("fileName");
                String content = matcher.group("fileContent");
                fileName = fileName.trim();
                content = content.trim();
                if (openRep != null){
                    if (openRep.hasFileName(fileName)){
                        System.out.println("File with this name already exists in this Repository");
                    } else{
                        openRep.createFile(fileName, content);
                        System.out.println("File created successfully! ");
                    }
                }
            } else if (command.matches(ChContent)){
                matcher = getMatcher(command, ChContent);
                String fileName = matcher.group("fileName");
                String content = matcher.group("fileContent");
                fileName = fileName.trim();
                content = content.trim();
                if (openRep != null){
                    if (openRep.hasFileName(fileName)){
                        System.out.println("Edited successfully! ");
                        openRep.changeContent(fileName, content);
                    } else{
                        System.out.println("No file exists with the given name");
                    }
                }
            } else if (command.matches("\\s*add\\s+-all\\s*")){
                openRep.addAll();
                System.out.println("Added");
            } else if (command.matches("\\s*add\\s+\"(?<fileName>[\\s\\S]*)\"\\s*")){
                matcher = getMatcher(command, "\\s*add\\s+\"(?<fileName>[\\s\\S]*)\"\\s*");
                String fileName = matcher.group("fileName");
                fileName = fileName.trim();
                if (openRep.hasFileName(fileName)){
                    openRep.addFile(fileName);
                    System.out.println("Added");
                } else System.out.println("No file exists with the given name");
            } else if (command.matches("\\s*commit\\s+\"(?<commitTitle>[\\s\\S]*)\"\\s*")){

                matcher = getMatcher(command, "\\s*commit\\s+\"(?<commitTitle>[\\s\\S]*)\"\\s*");
                String title = matcher.group("commitTitle");
                title = title.trim();
                if (openRep.isFile()){
                    openRep.createCommit(title);

                    System.out.println("Committed ");
                } else System.out.println("No files are added");
            } else if (command.matches("\\s*un-stage\\s+-all\\s*")){
                for (Repository r: reps) {
                    r.unStage();
                }
                System.out.println("Removed from stage");
            } else if (command.matches("\\s*un-stage\\s+\"(?<fileName>[\\s\\S]*)\"\\s*")){
                matcher = getMatcher(command, "\\s*un-stage\\s+\"(?<fileName>[\\s\\S]*)\"\\s*");
                String fileName = matcher.group("fileName");
                fileName = fileName.trim();
                int i =0;
                for (Repository r: reps) {
                    for (File f: r.files) {
                        if (f.name.equals(fileName))
                            if (f.staged){
                                System.out.println("Removed from stage ");
                                f.staged = false;
                                i=1;
                            }
                    }
                }
                if (i==0)
                     System.out.println("No file exists with the given name");
            } else if (command.matches("\\s*remove\\s+\"(?<fileName>[\\s\\S]*)\"\\s*")){
                matcher = getMatcher(command, "\\s*remove\\s+\"(?<fileName>[\\s\\S]*)\"\\s*");
                String fileName = matcher.group("fileName");
                fileName = fileName.trim();
                if(openRep.hasFileName(fileName)){
                    System.out.println("Removed");
                    openRep.removeFile(fileName);
                } else System.out.println("No file exists with the given name");
            } else if (command.matches("\\s*restore\\s+\"(?<fileName>[\\s\\S]+)\"\\s+\"(?<commitTitle>[\\s\\S]*)\"\\s*")){
                matcher = getMatcher(command, "\\s*restore\\s+\"(?<fileName>[\\s\\S]*)\"\\s+\"(?<commitTitle>[\\s\\S]*)\"\\s*");
                String fileName = matcher.group("fileName");
                String commitTitle = matcher.group("commitTitle");
                fileName = fileName.trim();
                commitTitle = commitTitle.trim();
                if (openRep.hasCommit(commitTitle)){
                    Commit c = openRep.getCommit(commitTitle);
                    if(openRep.isFileInCommit(c,fileName)) {
                        openRep.bazyabi(openRep.getCommit(commitTitle),fileName);
                        System.out.println("Restored");
                    }else System.out.println("No file exists in the given commit with the given name");
                }else System.out.println("No commit exists with the given name");
            } else if (command.matches("\\s*restore\\s+(?<commitNum>\\d+)\\s*")){
                matcher = getMatcher(command, "\\s*restore\\s+(?<commitNum>\\d+)\\s*");
                String commitTitle = matcher.group("commitNum");
                commitTitle = commitTitle.trim();
                int commitNum = Integer.parseInt(commitTitle);
                if (openRep.commits.size() >= commitNum){
                    openRep.bazyabi(openRep.commits.get(commitNum-1));
                    System.out.println("Restored");
                }else System.out.println("No commit exists with the given number");
            } else if (command.matches("\\s*restore\\s+\"(?<commitTitle>[\\s\\S]*)\"\\s*")){
                matcher = getMatcher(command, "\\s*restore\\s+\"(?<commitTitle>[\\s\\S]*)\"\\s*");

                String commitTitle = matcher.group("commitTitle");
                commitTitle = commitTitle.trim();
                if (openRep.hasCommit(commitTitle)){
                    openRep.bazyabi(openRep.getCommit(commitTitle));
                    System.out.println("Restored");
                }else System.out.println("No commit exists with the given name");
            } else if (command.matches("\\s*restore\\s+\"(?<fileName>[\\s\\S]*)\"\\s+(?<commitNum>\\d+)\\s*")){
                matcher = getMatcher(command, "\\s*restore\\s+\"(?<fileName>[\\s\\S]*)\"\\s+(?<commitNum>\\d+)\\s*");
                String fileName = matcher.group("fileName");
                String commitTitle = matcher.group("commitNum");
                fileName = fileName.trim();
                commitTitle = commitTitle.trim();
                int commitNum = Integer.parseInt(commitTitle);
                if (openRep.commits.size() >= commitNum){
                    Commit c = openRep.commits.get(commitNum-1);
                    if(openRep.isFileInCommit(c,fileName)) {
                        openRep.bazyabi(c,fileName);
                        System.out.println("Restored");
                    }else System.out.println("No file exists in the given commit with the given name");
                }else System.out.println("No commit exists with the given number");
            } else if (command.matches("\\s*show\\s+\"(?<fileName>[\\s\\S]*)\"\\s*")){
                matcher = getMatcher(command, "\\s*show\\s+\"(?<fileName>[\\s\\S]*)\"\\s*");
                String fileName = matcher.group("fileName");
                fileName = fileName.trim();
                if (openRep.hasFileName(fileName)){
                    String out = openRep.getFile(fileName).getContent();
                    System.out.println(out);
                } else System.out.println("No file exists with the given name ");
            } else if (command.matches("\\s*status\\s*")){
                openRep.status();
            } else if (command.matches("\\s*log\\s*")){
                openRep.log();
            } else if (command.matches(dif1)){
                matcher = getMatcher(command, dif1);
                String fileName = matcher.group("fileName");
                String commitOne = matcher.group("commitOne");
                String commitTwo = matcher.group("commitTwo");
                fileName = fileName.trim();
                commitOne = commitOne.trim();
                commitTwo = commitTwo.trim();
                boolean hasCommit = openRep.hasCommit(commitOne);
                boolean hasCommitt = openRep.hasCommit(commitTwo);
                if (hasCommitt && hasCommit){
                    boolean hasFile = openRep.getCommit(commitOne).hasFile(fileName);
                    boolean hasFile2 = openRep.getCommit(commitTwo).hasFile(fileName);
                    if (hasFile2 && hasFile&& openRep.hasFileName(fileName)){
                        Commit c1 = openRep.getCommit(commitOne);
                        Commit c2 = openRep.getCommit(commitTwo);
                        openRep.diff(fileName, c1, c2);
                    } else System.out.println("The given file doesn't exist in one/both of the given commits or its removed");
                } else System.out.println("One of the commits doesn't exist");
            } else if (command.matches(dif2)){
                matcher = getMatcher(command, dif2);
                String fileName = matcher.group("fileName");
                String commitOne = matcher.group("commitOne");
                String commitTwo = matcher.group("commitTwo");
                fileName = fileName.trim();
                commitOne = commitOne.trim();
                commitTwo = commitTwo.trim();
                int commitO = Integer.parseInt(commitOne);

                boolean hasCommit = false;
                if (openRep.commits.size()>= commitO){
                     hasCommit = true;
                }
                boolean hasCommitt = openRep.hasCommit(commitTwo);
                if (hasCommitt && hasCommit){
                    boolean hasFile = openRep.commits.get(commitO-1).hasFile(fileName);
                    boolean hasFile2 = openRep.getCommit(commitTwo).hasFile(fileName);
                    if (hasFile2 && hasFile&& openRep.hasFileName(fileName)){
                        Commit c1 = openRep.commits.get(commitO-1);
                        Commit c2 = openRep.getCommit(commitTwo);
                        openRep.diff(fileName,c1,c2);
                    } else System.out.println("The given file doesn't exist in one/both of the given commits or its removed");
                } else System.out.println("One of the commits doesn't exist");

            } else if (command.matches(dif3)){
                matcher = getMatcher(command, dif3);
                String fileName = matcher.group("fileName");
                String commitOne = matcher.group("commitOne");
                String commitTwo = matcher.group("commitTwo");
                fileName = fileName.trim();
                commitOne = commitOne.trim();
                commitTwo = commitTwo.trim();
                int commitT = Integer.parseInt(commitTwo);
                boolean hasCommit = false;
                if (openRep.commits.size()>= commitT){
                    hasCommit = true;
                }
                boolean hasCommitt = openRep.hasCommit(commitOne);
                if (hasCommitt && hasCommit){
                    boolean hasFile = openRep.commits.get(commitT-1).hasFile(fileName);
                    boolean hasFile2 = openRep.getCommit(commitOne).hasFile(fileName);
                    if (hasFile2 && hasFile&& openRep.hasFileName(fileName)){
                        Commit c2 = openRep.commits.get(commitT-1);
                        Commit c1 = openRep.getCommit(commitOne);
                        openRep.diff(fileName,c1,c2);
                    } else System.out.println("The given file doesn't exist in one/both of the given commits or its removed");
                } else System.out.println("One of the commits doesn't exist");

            } else if (command.matches(dif4)){
                matcher = getMatcher(command, dif4);
                String fileName = matcher.group("fileName");
                String commitOne = matcher.group("commitOne");
                String commitTwo = matcher.group("commitTwo");
                fileName = fileName.trim();
                commitOne = commitOne.trim();
                commitTwo = commitTwo.trim();
                int commitO = Integer.parseInt(commitOne);

                int commitT = Integer.parseInt(commitTwo);
                boolean hasCommit = false;
                if (openRep.commits.size()>= commitO){
                    hasCommit = true;
                }
                boolean hasCommitt = false;
                if (openRep.commits.size()>= commitT){
                    hasCommitt = true;
                }
                if (hasCommitt && hasCommit){
                    boolean hasFile = openRep.commits.get(commitO-1).hasFile(fileName);
                    boolean hasFile2 = openRep.commits.get(commitT-1).hasFile(fileName);
                    if (hasFile2 && hasFile && openRep.hasFileName(fileName)){
                        Commit c1 = openRep.commits.get(commitO-1);
                        Commit c2 = openRep.commits.get(commitT-1);
                        openRep.diff(fileName,c1,c2);
                    } else System.out.println("The given file doesn't exist in one/both of the given commits or its removed");
                } else System.out.println("One of the commits doesn't exist");

            }

            command = scanner.nextLine();
        }
    }
    public static Matcher getMatcher(String command, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(command);
        return matcher.matches() ? matcher : null;
    }
    static boolean hasRepName(String name){
        for (String repName: repNames) {
            if (repName.equals(name)){
                return true;
            }
        }
        return false;
    }
    static Repository findRep(String name){
        for (int i = 0; i < repNames.size(); i++){
            if (repNames.get(i).equals(name)){
                return reps.get(i);
            }
        }
        return null;
    }

}
