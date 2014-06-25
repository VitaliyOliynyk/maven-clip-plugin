package eu.vitaliy.maven.clipplugin;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.sun.istack.internal.Nullable;
import eu.vitaliy.maven.clipplugin.exception.PomNotFoundException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Mojo( name = "configure", defaultPhase = LifecyclePhase.PROCESS_SOURCES )
public class ClipConfigure
    extends AbstractMojo
{
    File baseDir = new File(".");

    /**
     * Location of the file.
     */
    @Parameter(property = "modules", required = true )
    String modules;

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
        List<String> moduleNames = createModuleNames(modules);
        List<File> projectFiles = createProjectFiles(moduleNames);
    }

    public List<String> createModuleNames(String modules) {
        return Arrays.asList(modules.split(","));
    }

    public List<File> createProjectFiles(List<String> moduleNames) {
        return Lists.newArrayList(Lists.transform(moduleNames, new Function<String, File>() {
            @Override
            public File apply(@Nullable java.lang.String moduleName) {
                File pomFile = new File(baseDir, "/../" + moduleName + "/pom.xml");
                if(!pomFile.exists()) {
                    throw new PomNotFoundException(pomFile);
                }
                return pomFile;
            }
        }));
    }
}
