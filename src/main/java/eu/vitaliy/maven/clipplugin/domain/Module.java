package eu.vitaliy.maven.clipplugin.domain;

import org.apache.commons.io.FileUtils;
import org.joox.JOOX;
import org.joox.Match;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
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
    protected VersionConfigureWay versionConfigureWay = VersionConfigureWay.VERSION;
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

    public void configure(Collection<Module> modules) {
        try {
            configureImpl(modules);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void configureImpl(Collection<Module> modules) throws Exception {
        Match dependencies = documentWithNamespace.xpath("/p:project/p:dependencies/p:dependency");
        List<Element> dependencyList = dependencies.get();
        boolean pomIsChanged = false;
        for (Element dependencyElement : dependencyList) {
            String dependencyArtefactId = $(dependencyElement).child("artifactId").text();
            for (Module module : modules) {
                if (module.getArtifactId().equals(dependencyArtefactId)) {
                    Match dependencyVersionElement = $(dependencyElement).child(ELEMENT_VERSION);
                    String moduleVersion = (versionConfigureWay == VersionConfigureWay.VERSION ? module.getVersion() : LATEST_DEPENDENCY_VERSION);
                    System.out.println("versionConfigureWay=" + versionConfigureWay + " for module " + module.getArtifactId());
                    if (dependencyVersionElement.size() == 0) {
                        $(dependencyElement).append($(ELEMENT_VERSION).text(moduleVersion));
                    } else {
                        dependencyVersionElement.text(moduleVersion);
                    }
                    pomIsChanged = true;
                    System.out.println(String.format("Match artefact for:[%s:%s:%s] ->[%s,%s,%s] ", groupId, artifactId, version, module.getGroupId(), module.getArtifactId(), module.getVersion()));
                }
            }
            writePomFile(pomIsChanged);
        }

        System.out.println("Module " + artifactId + " configure\n----------------------");
    }

    private void writePomFile(boolean pomIsChanged) throws IOException {
        if (pomIsChanged) {
            backupOriginalPomFile();
            $(document).write(pomFile);
        }
    }

    private void backupOriginalPomFile() throws IOException {
        File backupPomFile = new File(pomFile.getAbsolutePath() + ".backup");
        if (!backupPomFile.exists()) {
            FileUtils.copyFile(pomFile, backupPomFile);
        }
    }

    public void setVersionConfigureWay(VersionConfigureWay versionConfigureWay) {
        this.versionConfigureWay = versionConfigureWay;
    }
}
