package depsolver;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.JSON;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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

      makeGraph(repo);
  }

  //check if package must be installed.
  public static boolean mustNotInstall(String pack){
      if(pack.charAt(0) == '-'){
          return true;
      } else{
          return false;
      }
  }

  static String readFile(String filename) throws IOException {
      BufferedReader br = new BufferedReader(new FileReader(filename));
      StringBuilder sb = new StringBuilder();
      br.lines().forEach(line -> sb.append(line));
      return sb.toString();
  }


  public static void makeGraph(List<Package> repo){
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


  }

  public static void search(List<Package> x, List<Package> repo){
      HashSet<List<Package>> seen = new HashSet<>();
      if(!valid(x)){
          return;
      }
      if(seen.contains(x)){
          return;
      } else{
          seen.add(x);
      }
      if(/*x is final*/){
          //solution found!
      }

      Package tmp = repo.stream()
              .filter(p -> !x.contains(p) && !hasConflicts(p))
              .findFirst()
              .get();
      if(hasDepends(tmp)){
          tmp.getDepends().stream().filter(deps -> deps.size() > 1).forEach(dis -> dis.stream().map(p -> getPackage(p, repo)));
      }

  }

  public static Package getPackage(String pack, List<Package> repo){
      if(pack.charAt(0) == '+' || pack.charAt(0) == '-'){
          pack = pack.substring(1);
      }
      String[] packBits = pack.split("=");
      Package res = repo.stream()
              .filter(p -> p.getName().equals(packBits[0]) && p.getVersion().equals(packBits[2]))
              .findFirst()
              .get();
      return res;
  }

  public static boolean hasConflicts(Package p){
      if(p.getConflicts().size() != 0){
          return true;
      } else{
          return false;
      }
  }

  public static boolean hasDepends(Package p){
      if(p.getDepends().size() != 0){
          return true;
      } else{
          return false;
      }
  }
  public static boolean valid(List<Package> x){

      return true;
  }

  public static boolean isFinal(List<Package> x){

      return true;
  }

  public static List<Package> subset(List<Package> set, int start, int end){
      //end is exclusive.
      assert end < set.size() && start < end && start >= 0 : "start and end must be within the length of the array argument";

      List<Package> result = new ArrayList<>();
      for(;start < end; start++){
          result.add(set.get(start));
      }
      return result;
  }

  //FIXME: This doesn't work, try different approach.
  public static void subset(List<Package> set, Package[] sub, int m, int index){
      if(sub[m-1] != null){
          List<Package[]> list = new ArrayList<Package[]>();
          list.add(sub);
          for(Package[] subset : list){
              for(Package e : subset){
                  System.out.print(e.getName() + "=" + e.getVersion() + " ");
              }
              System.out.println();
          }
      } else {
          int n = set.size();
          int k = (n - m) + 1;
          for (int i = index; i < k; i++) {
              sub[index] = set.get(index);
              subset(set, sub, m - 1, index++);
          }
      }
  }


}
