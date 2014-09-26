package eu.vitaliy.maven.clipplugin.domain;

import java.io.File;
import java.util.List;

/**
 * User: xaoc
 * Date: 26.06.14
 * Time: 00:20
 */
public class Project extends Module{
    public List<Module> modules;

    public Project(File pomFile) {
        super(pomFile);
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    @Override
    public void configure(List<Module> modules) {
        super.configure(modules);
        for (Module module : modules) {
            module.configure(modules);
        }
    }
}
