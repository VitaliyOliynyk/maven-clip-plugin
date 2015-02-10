package eu.vitaliy.maven.clipplugin.parser.ide;


import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

public class ProjectInfo {
    File projectPomFile;
    Collection<File> modulePomFiles = new LinkedList<File>();

    public File getProjectPomFile() {
        return projectPomFile;
    }

    public void setProjectPomFile(File projectPomFile) {
        this.projectPomFile = projectPomFile;
    }

    public Collection<File> getModulePomFiles() {
        return modulePomFiles;
    }


    public void setModulePomFiles(Collection<File> modulePomFiles) {
        this.modulePomFiles = modulePomFiles;
    }

    public void addModulePomFile(File modulePomFile) {
        modulePomFiles.add(modulePomFile);
    }
}
