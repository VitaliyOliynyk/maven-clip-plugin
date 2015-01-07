package eu.vitaliy.maven.clipplugin.domain;

import org.apache.commons.io.FileUtils;
import org.joox.JOOX;
import org.joox.Match;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.joox.JOOX.$;

/**
 * User: xaoc
 * Date: 26.06.14
 * Time: 00:14
 */
public class Module extends Dependency{
    public static final String ELEMENT_VERSION = "version";
    public static final String LATEST_DEPENDENCY_VERSION = "LATEST";
    private List<Module> modules;
    private Match documentWithNamespace;
    protected File pomFile;
    protected Document document;

    public Module(File pomFile) {
        this.pomFile = pomFile;
    }

    public void parseFromPom() {
        try {
            parseFromPomImpl(pomFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void parseFromPomImpl(File pom) throws IOException, SAXException {
        document = $(pom).document();
        documentWithNamespace = $(document).namespace("p", "http://maven.apache.org/POM/4.0.0");
        readArtefact(documentWithNamespace);
    }

    private void readArtefact(Match documentWithNamespace) {
        groupId = documentWithNamespace.xpath("/p:project/p:groupId").text();
        artifactId = documentWithNamespace.xpath("/p:project/p:artifactId").text();
        version = documentWithNamespace.xpath("/p:project/p:version").text();
    }

    public void configure(List<Module> modules) {
        try {
            configureImpl(modules);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void configureImpl(List<Module> modules) throws Exception {
        Match dependencies = documentWithNamespace.xpath("/p:project/p:dependencies/p:dependency");
        List<Element> dependencyList = dependencies.get();
        boolean pomIsChanged = false;
        for (Element dependencyElement : dependencyList) {
            String dependencyArtefactId = $(dependencyElement).child("artifactId").text();
            for (Module module : modules) {
                if (module.getArtifactId().equals(dependencyArtefactId)) {
                    Match dependencyVersionElement = $(dependencyElement).child(ELEMENT_VERSION);

                    if (dependencyVersionElement.size() == 0) {
                        $(dependencyElement).append($(ELEMENT_VERSION).text(LATEST_DEPENDENCY_VERSION));
                    } else {
                        dependencyVersionElement.text(LATEST_DEPENDENCY_VERSION);
                    }
                    pomIsChanged = true;
                    System.out.println(String.format("Match artefact for:[%s:%s:%s] ->%s ", groupId, artifactId, version, module.getArtifactId()));
                }
            }
            if (pomIsChanged) {
                FileUtils.copyFile(pomFile, new File(pomFile.getAbsolutePath()+".backup"));
                $(document).write(pomFile);
            }
        }

        System.out.println("Module "+artifactId + " configure\n----------------------");
    }
}
