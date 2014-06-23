package eu.vitaliy.maven.clipplugin;

import ch.lambdaj.function.convert.ConstructorArgumentConverter;
import ch.lambdaj.function.convert.Converter;
import eu.vitaliy.maven.clipplugin.exception.PomNotFoundException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.joox.Match;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import static ch.lambdaj.Lambda.convert;
import static org.joox.JOOX.$;

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
        return convert(moduleNames, new Converter<String, java.io.File>() {
            @Override
            public File convert(String moduleName) {
                File pomFile = new File(baseDir, "/../" + moduleName + "/pom.xml");
                if(!pomFile.exists()) {
                    throw new PomNotFoundException(pomFile);
                }
                return pomFile;
            }
        });
    }
}
