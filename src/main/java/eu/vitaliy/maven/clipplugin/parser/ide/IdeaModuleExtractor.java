package eu.vitaliy.maven.clipplugin.parser.ide;

import org.joox.Match;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileNotFoundException;

import static org.joox.JOOX.$;

public class IdeaModuleExtractor extends IDEModuleExtractor {

    @Override
    public ProjectInfo extract(File currentDirectory) {
        try {
            return extractImpl(currentDirectory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ProjectInfo extractImpl(File currentDirectory) throws Exception {
        Document ideaModulesXmlDocument = $(new File(currentDirectory, ".idea/modules.xml")).document();
        Match moduleMatches = $(ideaModulesXmlDocument).find("module");
        ProjectInfo projectInfo = new ProjectInfo();
        for (Element element : moduleMatches.get()) {
            String filePath = $(element).attr("filepath");
            filePath = filePath.replace("$PROJECT_DIR$/", "");
            filePath = filePath.replaceAll("[^/]*?\\.iml", "");

            File pomFile;
            if (filePath.isEmpty()) {
                pomFile = new File(currentDirectory, "pom.xml");
                projectInfo.setProjectPomFile(pomFile);
                continue;
            } else {
                pomFile = new File(currentDirectory, filePath + "/pom.xml");
                projectInfo.addModulePomFile(pomFile);
            }
            if (!pomFile.exists()) {
                throw new FileNotFoundException(pomFile.getCanonicalPath());
            }

            System.out.println(pomFile.getCanonicalPath());
        }

        return projectInfo;
    }
}
