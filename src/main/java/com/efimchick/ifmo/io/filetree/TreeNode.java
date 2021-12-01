package com.efimchick.ifmo.io.filetree;


import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class TreeNode<T> implements Iterable<TreeNode<T>> {
    private final List<TreeNode<T>> elementsIndex;
    public T data;
    public TreeNode<T> parent;
    public List<TreeNode<T>> children;

    public TreeNode(T data) {
        this.data = data;
        this.children = new LinkedList<TreeNode<T>>();
        this.elementsIndex = new LinkedList<TreeNode<T>>();
        this.elementsIndex.add(this);
    }

    public static TreeNode<File> createDirTree(File folder) {
        TreeNode<File> DirRoot = new TreeNode<File>(folder);
        if (!folder.isDirectory()) {
            return DirRoot;
        } else {
            for (File file : getLocationFiles(folder.listFiles())) {
                if (file.isDirectory()) {
                    appendDirTree(file, DirRoot);
                } else {
                    appendFile(file, DirRoot);
                }
            }
        }
        return DirRoot;
    }

    private static File[] getLocationFiles(File[] files) {
        List<File> LocationList = new ArrayList<>();
        List<File> LocationEndList = new ArrayList<>();

        for (File fileForLocation : files) {
            if (fileForLocation.isDirectory()) {
                LocationList.add(fileForLocation);
            } else {
                LocationEndList.add(fileForLocation);
            }
        }
        LocationList.addAll(LocationEndList);
        files = LocationList.toArray(new File[0]);
        return files;
    }

    public static void appendDirTree(File folder, TreeNode<File> DirRoot) {
        DirRoot.addChild(folder);
        for (File file : getLocationFiles(folder.listFiles())) {
            if (file.isDirectory()) {
                appendDirTree(file,
                        DirRoot.children.get(DirRoot.children.size() - 1));
            } else {
                appendFile(file,
                        DirRoot.children.get(DirRoot.children.size() - 1));
            }
        }
    }

    public static void appendFile(File file, TreeNode<File> filenode) {
        filenode.addChild(file);
    }

    public static String renderDirectoryTree(TreeNode<File> tree) {
        List<StringBuilder> lines = renderDirectoryTreeLines(tree);
        List<StringBuilder>tempList=
                lines.stream().map(stringBuilder -> stringBuilder.append("\n")).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder(lines.size() * 20);
        for (StringBuilder line:tempList){
            sb.append(line);
        }
        byte[] bytes = new byte[0];

        return sb.toString();
    }

    public static List<StringBuilder>
    renderDirectoryTreeLines(TreeNode<File>
                                     tree) {
        List<StringBuilder> result = new LinkedList<>();
        result.add(new StringBuilder().append(tree.data.getName()).append(" ").append(getLengthInner(tree.data)).append(" bytes"));
        Iterator<TreeNode<File>> iterator = tree.children.iterator();
        while (iterator.hasNext()) {
            List<StringBuilder> subtree =
                    renderDirectoryTreeLines(iterator.next());
            if (iterator.hasNext()) {
                addSubtree(result, subtree);
            } else {
                addLastSubtree(result, subtree);
            }
        }
        return result;
    }

    private static long getLengthInner(File file) {

        long result = 0;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File fileInnerForLength : files) {
                if (!fileInnerForLength.isDirectory()) {
                    result = result + fileInnerForLength.length();
                } else {
                    result = result + getLengthInner(fileInnerForLength);
                }
            }
            return result;
        } else {
            return file.length();
        }
    }

    private static void addSubtree(List<StringBuilder> result,
                                   List<StringBuilder> subtree) {
        Iterator<StringBuilder> iterator = subtree.iterator();
        result.add(iterator.next().insert(0, Pref.BRANCH.getValue()));
        while (iterator.hasNext()) {
            result.add(iterator.next().insert(0, Pref.STICK.getValue()));
        }
    }

    private static void addLastSubtree(List<StringBuilder> result,
                                       List<StringBuilder> subtree) {
        Iterator<StringBuilder> iterator = subtree.iterator();
        result.add(iterator.next().insert(0, Pref.LAST.getValue()));
        while (iterator.hasNext()) {
            result.add(iterator.next().insert(0, "   "));
        }
    }

    public TreeNode<T> addChild(T child) {
        TreeNode<T> childNode = new TreeNode<T>(child);
        childNode.parent = this;
        this.children.add(childNode);
        this.registerChildForSearch(childNode);
        return childNode;
    }

    private void registerChildForSearch(TreeNode<T> node) {
        elementsIndex.add(node);
        if (parent != null)
            parent.registerChildForSearch(node);
    }

    @Override
    public String toString() {
        return data != null ? data.toString() : "[data null]";
    }

    @Override
    public Iterator<TreeNode<T>> iterator() {
        TreeNodeIter<T> iter = new TreeNodeIter<T>(this);
        return iter;
    }
}