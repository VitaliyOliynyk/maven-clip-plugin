package eu.vitaliy.maven.clipplugin.domain;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: xaoc
 * Date: 26.06.14
 * Time: 01:03
 */
public class ModuleTest {
    private File baseDir;
    public static final String TEST_FILES = "target/it";

    @Before
    public void before() throws Exception {
        FileUtils.deleteDirectory(new File(TEST_FILES));
        FileUtils.copyDirectory(new File("src/it"), new File(TEST_FILES));
        baseDir = new File(TEST_FILES + "/module1");
    }

    @Test
    public void parseFromXmlTest() throws Exception {
        File projectPom = new File(baseDir, "pom.xml");
        Module module = new Module(projectPom);
        module.parseFromPom();
        assertThat(module.getGroupId()).isEqualTo("eu.vitaliy.maven.clipplugin");
        assertThat(module.getArtifactId()).isEqualTo("module1");
    }
}
