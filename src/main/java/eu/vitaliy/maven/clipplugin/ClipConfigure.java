package eu.vitaliy.maven.clipplugin;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import eu.vitaliy.maven.clipplugin.domain.IDE;
import eu.vitaliy.maven.clipplugin.domain.Module;
import eu.vitaliy.maven.clipplugin.domain.Project;
import eu.vitaliy.maven.clipplugin.domain.VersionConfigureWay;
import eu.vitaliy.maven.clipplugin.exception.PomNotFoundException;
import eu.vitaliy.maven.clipplugin.parser.ide.IdeaModuleExtractor;
import eu.vitaliy.maven.clipplugin.parser.ide.ProjectInfo;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * example:
 * mvn eu.vitaliy:maven-clip-plugin:0.0.1-SNAPSHOT:clip -Dmodules=module2,module3,module4
 */
@Mojo( name = "clip", defaultPhase = LifecyclePhase.PROCESS_SOURCES )
public class ClipConfigure extends AbstractMojo{

    Log log = getLog();

    File baseDir = new File(".");

    @Parameter(property = "modules", required = false )
    String modules;

    @Parameter(property = "ide", required = false )
    String ide;

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
        log.info("modules: " + modules);
        log.info("versionConfigureWay: " + versionConfigureWay);
        log.info("ide: " + versionConfigureWay);
        ProjectInfo projectInfo = createProjectInfo();
        Project project = getProject(projectInfo);
        Collection<Module> modules = createModules(projectInfo.getModulePomFiles());
        project.configure(modules);
        log.info("Project configured");
    }

    private ProjectInfo createProjectInfo() {
        if (ide == null) {
            ProjectInfo projectInfo = new ProjectInfo();
            projectInfo.setProjectPomFile(new File(baseDir, "pom.xml"));

            List<String> moduleNames = createModuleNames();
            List<File> projectFiles = createProjectFiles(moduleNames);
            projectInfo.setModulePomFiles(projectFiles);
            return projectInfo;
        }

        if (ide.equalsIgnoreCase(IDE.IDEA.name())) {
            return new IdeaModuleExtractor().extract(baseDir);
        }

        throw new IllegalStateException("Invalid ide option. Ide values: <IDEA> ");
    }

    private Project getProject(ProjectInfo projectInfo) {
        Project project = new Project(projectInfo.getProjectPomFile());
        if (versionConfigureWay != null) {
            project.setVersionConfigureWay(VersionConfigureWay.valueOf(versionConfigureWay));
        }
        project.parseFromPom();
        return project;
    }

    private Collection<Module> createModules(Collection<File> projectFiles) {
        Collection<Module> modules = new ArrayList<Module>(projectFiles.size());
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

    public List<String> createModuleNames() {
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
