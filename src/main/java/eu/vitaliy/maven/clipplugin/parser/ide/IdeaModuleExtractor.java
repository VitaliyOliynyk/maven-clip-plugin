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
            e.printStackTrace();
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
            String path;
            if (filePath.isEmpty()) {
                path = "pom.xml";
                pomFile = createFile(currentDirectory, path);
                if (pomFile.exists()) {
                    projectInfo.setProjectPomFile(pomFile);
                }
            } else {
                path = filePath + "pom.xml";
                pomFile = createFile(currentDirectory, path);
                if (pomFile.exists()) {
                    projectInfo.addModulePomFile(pomFile);
                }
            }

            System.out.println("module path: " + path);
            if (pomFile != null) {
                System.out.println("module absolute path:" + pomFile.getAbsolutePath());
                System.out.println("module canonical path:" + pomFile.getCanonicalPath());
            }

        }

        return projectInfo;
    }

    private File createFile(File currentDirectory, String path) {
       char[] pathCharArray = path.toCharArray();
        if ((pathCharArray.length > 0 && pathCharArray[0] == '/') || (pathCharArray.length > 1 && pathCharArray[1] == ':')) {
            return new File(path);
        }
        return new File(currentDirectory, path);
    }
}
