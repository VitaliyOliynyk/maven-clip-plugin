package eu.vitaliy.maven.clipplugin.parser.ide;

import java.io.File;

public abstract class IDEModuleExtractor {
    public ProjectInfo extract() {
        return extract(".");
    }

    public ProjectInfo extract(String currentDirectory) {
        return extract(new File(currentDirectory));
    }

    public abstract ProjectInfo extract(File baseDir);
}
