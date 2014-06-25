package eu.vitaliy.maven.clipplugin.domain;

import org.joox.Match;
import org.w3c.dom.Document;
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
    private List<Module> modules;
    Match documentWithNamespace;

    void parseFromPom(File pom) {
        try {
            parseFromPomImpl(pom);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void parseFromPomImpl(File pom) throws IOException, SAXException {
        Document document = $(pom).document();
        documentWithNamespace = $(document).namespace("p", "http://maven.apache.org/POM/4.0.0");
        readArtefact(documentWithNamespace);
    }

    private void readArtefact(Match documentWithNamespace) {
        groupId = documentWithNamespace.xpath("/p:project/p:groupId").text();
        artifactId = documentWithNamespace.xpath("/p:project/p:artifactId").text();
        version = documentWithNamespace.xpath("/p:project/p:version").text();
    }
}
