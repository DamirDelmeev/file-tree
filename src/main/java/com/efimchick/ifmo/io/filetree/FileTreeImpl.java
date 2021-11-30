package com.efimchick.ifmo.io.filetree;

import java.io.File;
import java.nio.file.Path;

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

}

