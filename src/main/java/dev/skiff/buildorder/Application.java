package dev.skiff.buildorder;

import java.util.*;

public class Application {
    private static String[] getProjects(Scanner in) {
        System.out.println("Please enter list of projects separated by commas: ");
        String projList = in.nextLine();
        String[] projArr = projList.split(",");
        for (int i = 0; i < projArr.length; i++) {
            projArr[i] = projArr[i].replaceAll("\\s", "");
        }
        return projArr;
    }

    private static String[] getDependencies(Scanner in, String[] projects) {
        System.out.println("Please enter dependencies in the format '(a, d), (f, b), (b, d), (f, a), (d, c)' without apostrophes,");
        System.out.println("where the second project is dependent on the first.");
        String dependencyList = in.nextLine();
        String[] dependencyArr = dependencyList.split("\\)");
        for (int i = 0; i < dependencyArr.length; i++) {
            dependencyArr[i] = dependencyArr[i].replaceAll("\\s", "").replaceAll("[(),]", "");
        }
        return dependencyArr;
    }

    private static List<Project> createProjectList(String[] projects, String[] dependencies) {
        ArrayList<Project> projectList = new ArrayList<>();
        for (int i = 0; i < projects.length; i++) {
            Project temp = new Project();
            HashMap<String, ArrayList<String>> empty = new HashMap<String, ArrayList<String>>();
            empty.put(projects[i], new ArrayList<String>());
            temp.setProjectInfo(empty);
            projectList.add(temp);
        }
        for (int i = 0; i < dependencies.length; i++) {
            Project matching = null;
            for (int j = 0; j < projectList.size(); j++) {
                String key = String.valueOf(dependencies[i].charAt(1));
                if (projectList.get(j).getProjectInfo().containsKey(key)) {
                    matching = projectList.get(j);
                    ArrayList<String> deps = matching.getProjectInfo().get(String.valueOf(dependencies[i].charAt(1)));
                    deps.add(String.valueOf(dependencies[i].charAt(0)));
                    HashMap<String, ArrayList<String>> newProjInfo = new HashMap<>();
                    newProjInfo.put(String.valueOf(dependencies[i].charAt(1)), deps);
                    matching.setProjectInfo(newProjInfo);
                }
            }
        }
        return projectList;
    }

    private static String getBuildOrder(List<Project> projects) {
        String buildOrder = "";
        for (int i = 0; i < projects.size(); i++) {
            for (String key : projects.get(i).getProjectInfo().keySet()) {
                if (projects.get(i).getProjectInfo().get(key).isEmpty()) {
                    buildOrder += key;
                    projects.remove(projects.get(i));
                    i--;
                }
            }
        }
        int pSize = projects.size();
        for (int i = 0; i < pSize; i++) {
            for (int j = 0; j < projects.size(); j++) {
                for (String key : projects.get(j).getProjectInfo().keySet()) {
                    System.out.println(key);
                    ArrayList<String> deps = projects.get(j).getProjectInfo().get(key);
                    boolean isReady = true;
                    for (String s : deps) {
                        if (!buildOrder.contains(s)) {
                            isReady = false;
                            break;
                        }
                    }
                    if (isReady) {
                        buildOrder += key;
                        projects.remove(projects.get(j));
                        j--;
                    }
                }
            }
        }
        if (!projects.isEmpty()) {
            throw new RuntimeException("Could not find a valid build order");
        }
        return buildOrder;
    }

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            String[] projects = getProjects(in);
            String[] dependencies = getDependencies(in, projects);
            List<Project> projList = createProjectList(projects, dependencies);
            System.out.println(projList);
            System.out.println(getBuildOrder(projList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
