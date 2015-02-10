package eu.vitaliy.maven.clipplugin.parser.ide;

import org.junit.Test;

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

}
