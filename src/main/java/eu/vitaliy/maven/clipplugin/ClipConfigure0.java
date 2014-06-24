package eu.vitaliy.maven.clipplugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.joox.Match;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;

import static org.joox.JOOX.$;

@Mojo( name = "configure", defaultPhase = LifecyclePhase.PROCESS_SOURCES )
public class ClipConfigure0
    extends AbstractMojo
{
    /**
     * Location of the file.
     */
    @Parameter(property = "modules", required = true )
    private String modules;

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
        System.out.println(modules);
        String[] moduleList = modules.split(",");
        Document document = null;
        Document rootDokument = $(new File("pom.xml")).document();
        Match rootDokumentWithNamespace = $(rootDokument).namespace("p", "http://maven.apache.org/POM/4.0.0");
        for (String module : moduleList) {
            document = $(new File("../"+module+"/pom.xml")).document();

            Match documentWithNamespace = $(document).namespace("p", "http://maven.apache.org/POM/4.0.0");
            Dependency artefact = readArtefact(documentWithNamespace);

            System.out.println(artefact);

            Document documentToWrite = $(new File("pom.xml")).document();
            setModuleDependenciesVersion(rootDokumentWithNamespace, artefact);
        }
        $(rootDokumentWithNamespace).write(new File("out.xml"));

    }

    private void setModuleDependenciesVersion(Match rootDokument, Dependency artefact) {
        Match dependencies = rootDokument.xpath("/p:project/p:dependencies/p:dependency");
        if(dependencies.get().isEmpty()) {
           $(rootDokument).append($("dependencies"));
        }

        for(Element dependency : dependencies.get()) {
            Match groupId = $(dependency).find("groupId");

        }
    }

    private Dependency readArtefact(Match documentWithNamespace) {
        String groupId = documentWithNamespace.xpath("/p:project/p:groupId").text();
        String artifactId = documentWithNamespace.xpath("/p:project/p:artifactId").text();
        String version = documentWithNamespace.xpath("/p:project/p:version").text();
        return new Dependency(groupId, artifactId, version);
    }


    class Dependency {
        String groupId;
        String artifactId;
        String version;

        Dependency(String groupId, String artifactId, String version) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
        }

        @Override
        public String toString() {
            return "Dependency{" +
                    "groupId='" + groupId + '\'' +
                    ", artifactId='" + artifactId + '\'' +
                    ", version='" + version + '\'' +
                    '}';
        }
    }


}
