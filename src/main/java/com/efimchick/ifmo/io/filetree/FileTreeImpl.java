package com.efimchick.ifmo.io.filetree;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.efimchick.ifmo.io.filetree.TreeNode.createDirTree;
import static com.efimchick.ifmo.io.filetree.TreeNode.renderDirectoryTree;

public class FileTreeImpl implements FileTree {

    @Override
    public Optional<String> tree(Path path) {
        File file = new File(String.valueOf(path));
        String result;
        if (file.exists()) {
            TreeNode<File> DirTree = createDirTree(file);
            result = renderDirectoryTree(DirTree);
            if (!file.isDirectory()) {
                return Optional.of(result);
            } else {
                return Optional.of(result);
            }
        }
        return Optional.empty();
    }

    private String getResult(File file, String prefix, String spaces) {
        StringBuilder stringBuilder = new StringBuilder();
        File[] files = getLocationFiles(file.listFiles());
        for (File fileInner : files) {
            if (fileInner.equals(files[files.length - 1])) {
                prefix = prefix + Pref.LAST.getValue();
            } else {
                prefix = prefix + Pref.BRANCH.getValue();
            }
            if (fileInner.isDirectory()) {

                stringBuilder.append(prefix).append(fileInner.getName()).append(" ").append(getLengthInner(fileInner)).append(" bytes\n");

                stringBuilder.append(getResult(fileInner, Pref.STICK.getValue() + spaces + prefix, spaces));
            } else {
                stringBuilder.append(prefix).append(fileInner.getName()).append(" ").append(fileInner.length()).append(" bytes\n");
            }
        }
        return stringBuilder.toString();
    }

    private File[] getLocationFiles(File[] files) {
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

    private long getLengthInner(File file) {
        long result = 0;
        File[] files = file.listFiles();
        for (File fileInnerForLength : files) {
            if (!fileInnerForLength.isDirectory()) {
                result = result + fileInnerForLength.length();
            } else {
                result = result + getLengthInner(fileInnerForLength);
            }
        }
        return result;
    }
}

