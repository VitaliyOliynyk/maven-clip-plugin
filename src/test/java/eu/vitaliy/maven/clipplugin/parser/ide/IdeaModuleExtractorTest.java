package eu.vitaliy.maven.clipplugin.parser.ide;

import org.fest.assertions.core.Condition;
import org.junit.Test;

import java.io.File;

import static org.fest.assertions.api.Assertions.*;

public class IdeaModuleExtractorTest {

    @Test
         public void extractTest() throws Exception {
        //given
        String projectDir = "src/test/resources/ide/idea/project1";
        IDEModuleExtractor ideaModuleExtractor = new IdeaModuleExtractor();

        //when
        ProjectInfo projectInfo = ideaModuleExtractor.extract(projectDir);

        //then
        assertThat(projectInfo.getModulePomFiles()).hasSize(1);
        assertThat(projectInfo.getProjectPomFile()).exists();
    }

    @Test
    public void extractWorkspaceTest() throws Exception {
        //given
        String projectDir = "src/test/resources/ide/idea/workspace1";
        IDEModuleExtractor ideaModuleExtractor = new IdeaModuleExtractor();

        //when
        ProjectInfo projectInfo = ideaModuleExtractor.extract(projectDir);

        //then
        assertThat(projectInfo.getModulePomFiles()).hasSize(2);
        assertThat(projectInfo.getModulePomFiles()).are(new Condition<File>() {
            @Override
            public boolean matches(File file) {
                return file.exists();
            }
        });
        assertThat(projectInfo.getProjectPomFile()).isNull();
    }

}
