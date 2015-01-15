package eu.vitaliy.maven.clipplugin;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import eu.vitaliy.maven.clipplugin.domain.Module;
import eu.vitaliy.maven.clipplugin.domain.Project;
import eu.vitaliy.maven.clipplugin.domain.VersionConfigureWay;
import eu.vitaliy.maven.clipplugin.exception.PomNotFoundException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * example:
 * mvn eu.vitaliy:maven-clip-plugin:0.0.1-SNAPSHOT:clip -Dmodules=module2,module3,module4
 */
@Mojo( name = "clip", defaultPhase = LifecyclePhase.PROCESS_SOURCES )
public class ClipConfigure
    extends AbstractMojo
{
    File baseDir = new File(".");

    @Parameter(property = "modules", required = true )
    String modules;

    @Parameter(property = "versionConfigureWay", required = false, defaultValue = "VERSION")
    String versionConfigureWay = VersionConfigureWay.VERSION.name();

    public void execute()
        throws MojoExecutionException
    {
        try {
            executeImpl();
        } catch (Exception e) {
           throw new MojoExecutionException("Error!", e);
        }
    }

    public void executeImpl() throws Exception {
        System.out.println("modules: " + modules);
        System.out.println("versionConfigureWay: " + versionConfigureWay);
        Project project = getProject();
        List<String> moduleNames = createModuleNames(modules);
        List<File> projectFiles = createProjectFiles(moduleNames);
        List<Module> modules = createModules(projectFiles);
        project.configure(modules);
        System.out.println("Project configured");
    }

    private Project getProject() {
        Project project = new Project(new File(baseDir, "pom.xml"));
        if (versionConfigureWay != null) {
            project.setVersionConfigureWay(VersionConfigureWay.valueOf(versionConfigureWay));
        }
        project.parseFromPom();
        return project;
    }

    private List<Module> createModules(List<File> projectFiles) {
        List<Module> modules = new ArrayList<Module>(projectFiles.size());
        for (File projectFile : projectFiles) {
            Module module = new Module(projectFile);
            if (versionConfigureWay != null) {
                module.setVersionConfigureWay(VersionConfigureWay.valueOf(versionConfigureWay));
            }
            module.parseFromPom();
            modules.add(module);
        }

        return modules;
    }

    public List<String> createModuleNames(String modules) {
        return Arrays.asList(modules.split(","));
    }

    public List<File> createProjectFiles(List<String> moduleNames) {
        return Lists.newArrayList(Lists.transform(moduleNames, new Function<String, File>() {
            @Override
            public File apply(java.lang.String moduleName) {
                File pomFile = new File(baseDir, "/../" + moduleName + "/pom.xml");
                if(!pomFile.exists()) {
                    throw new PomNotFoundException(pomFile);
                }
                return pomFile;
            }
        }));
    }
}
