package depsolver;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.JSON;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashSet;

class Package {
    private String name;
    private String version;
    private Integer size;
    private List<List<String>> depends = new ArrayList<>();
    private List<String> conflicts = new ArrayList<>();

    public String getName() { return name; }
    public String getVersion() { return version; }
    public Integer getSize() { return size; }
    public List<List<String>> getDepends() { return depends; }
    public List<String> getConflicts() { return conflicts; }
    public void setName(String name) { this.name = name; }
    public void setVersion(String version) { this.version = version; }
    public void setSize(Integer size) { this.size = size; }
    public void setDepends(List<List<String>> depends) { this.depends = depends; }
    public void setConflicts(List<String> conflicts) { this.conflicts = conflicts; }
}

public class Main {
  public static void main(String[] args) throws IOException {
      //TODO: make sure to delete the following args lines
      String[] par = {"C:\\Users\\David\\Documents\\University\\Year 3\\CO663\\dependency-solver-2019-DavidFCHW\\tests\\example-0\\repository.json",
              "C:\\Users\\David\\Documents\\University\\Year 3\\CO663\\dependency-solver-2019-DavidFCHW\\tests\\example-0\\initial.json",
              "C:\\Users\\David\\Documents\\University\\Year 3\\CO663\\dependency-solver-2019-DavidFCHW\\tests\\example-0\\constraints.json"};

      TypeReference<List<Package>> repoType = new TypeReference<List<Package>>() {};
//      List<Package> repo = JSON.parseObject(readFile(args[0]), repoType);
      List<Package> repo = JSON.parseObject(readFile(par[0]), repoType);
      TypeReference<List<String>> strListType = new TypeReference<List<String>>() {};
//      List<String> initial = JSON.parseObject(readFile(args[1]), strListType);
      List<String> initial = JSON.parseObject(readFile(par[1]), strListType);
//      List<String> constraints = JSON.parseObject(readFile(args[2]), strListType);
      List<String> constraints = JSON.parseObject(readFile(par[2]), strListType);

      // CHANGE CODE BELOW:
      // using repo, initial and constraints, compute a solution and print the answer
      for (Package p : repo) {
          System.out.printf("package %s version %s\n", p.getName(), p.getVersion());
          for (List<String> clause : p.getDepends()) {
              System.out.printf("  dep:");
              for (String q : clause) {
                  System.out.printf(" %s", q);
              }
              System.out.printf("\n");
          }
      }
      var repoSize = repo.size();
      System.out.println("packages: " + repoSize);
      System.out.println("Total possible states: " + Math.pow(2, repoSize));
      makeGraph(repo);
  }

  static String readFile(String filename) throws IOException {
      BufferedReader br = new BufferedReader(new FileReader(filename));
      StringBuilder sb = new StringBuilder();
      br.lines().forEach(line -> sb.append(line));
      return sb.toString();
  }


  public static void makeGraph(List<Package> repo){
      long numberOfStates = Math.round(Math.pow(2, repo.size()));
      //use arrays to represent states.
      //TODO: a state is a "subset" of the repo. Think of sliding windows.
      //ArrayList<List<Package>> states = new ArrayList<>();
//      ArrayList<Package[]> states = new ArrayList<>();
      ArrayList<List<Package>> states = new ArrayList<>();
      //make the assumption that you always have an empty state, meaning that states.size() == numberofStates-1.
      for(int start = 0; start <= repo.size(); start++) {
          int end = 1;
          while (end <= repo.size()) {
              states.add(subset(repo, start, end));
              end++;
          }
      }

      for(List<Package> p : states){
          System.out.println();
          System.out.print("[");
          for(Package pack : p){
              System.out.print(pack.getName() + "=" + pack.getVersion() + " ");
          }
          System.out.println("]");
      }


      System.out.println("Number of states: " + states.size());
  }

  //TODO: Apply the main search algorithm.
  /**
   * The main search algorithm dfs.
   */
  public static void search(){

  }

  //TODO: Check if the state is valid or not.
  public static boolean isValid(){

      return true;
  }


  //FIXME: Check if how my algorithm actually works and sort it out. Currently it's missing two states, found out why and FAST!
  public static List<Package> subset(List<Package> set, int start, int end){
      //end is exclusive.
      assert end < set.size() && start < end && start >= 0 : "start and end must be within the length of the array argument";

      List<Package> result = new ArrayList<>();
      for(;start < end; start++){
          result.add(set.get(start));
      }
      return result;
  }

  /*public static List<Package[]> subset(List<Package> set, int elementSize){
      assert elementSize <= set.size() && elementSize >= 0 : "Element size must be a positive integer less than or equal to the set size";
      List<Package> states = new ArrayList<>();

      for(int index = 0; index < set.size(); index++){
          Package current = set.get(index);
          Package[] state = new Package[elementSize];
          state[index] = current;

      }

      return null;
  }*/




    /**
     * The following code is an adapted version of GeekforGeeks':
     * https://www.geeksforgeeks.org/print-all-possible-combinations-of-r-elements-in-a-given-array-of-size-n/ method 1.
     */
    public static List<Package[]> subsets(List<Package> set, Package[] tmpCombo, int setStart, int setEnd, int tmpIndex, int comboSize){
        List<Package[]> subset = new ArrayList<>();
        //If the current combination is completed then return it.
        if(tmpIndex == comboSize){
            subset.add(tmpCombo);
            return subset;
        } else {
            for (int i = setStart; i <= setEnd && setEnd - i + 1 >= comboSize - tmpIndex; i++) {
                tmpCombo[tmpIndex] = set.get(i);
                //recur here...
                subsets(set, tmpCombo, setStart + 1, setEnd, tmpIndex, comboSize);
            }
        }
        return subset;
    }
}
