/**
 * Copyright 2014 Vitaliy Oliynyk  http://vitaliy.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.vitaliy.maven.clipplugin;

import eu.vitaliy.maven.clipplugin.exception.PomNotFoundException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.List;

import static java.util.Arrays.asList;
import static junitparams.JUnitParamsRunner.$;
import static org.fest.assertions.api.Assertions.*;

/**
 * User: xaoc
 * Date: 23.06.14
 * Time: 22:12
 */
@RunWith(JUnitParamsRunner.class)
public class ClipConfigureTest {

    public static final String TEST_FILES = "target/it";
    private static final String MODULES = "module2,module3,module4";
    private static final String MODULES_ONE = "module2";
    private static final String MODULES_WITH_ERRORS = "module2,module3,module_error";
    ClipConfigure clipConfigure;

    @Before
    public void before() throws Exception {
        clipConfigure = new ClipConfigure();
        clipConfigure.modules = MODULES;
        FileUtils.deleteDirectory(new File(TEST_FILES));
        FileUtils.copyDirectory(new File("src/it"), new File(TEST_FILES));
        clipConfigure.baseDir = new File(TEST_FILES + "/module1");
        File projectPom = new File(clipConfigure.baseDir, "pom.xml");
        assertThat(projectPom).exists();
        assertThat(FileUtils.readFileToString(projectPom)).contains("module1");
    }

    @Test
    @Parameters
    public void createModuleNamesTest(String modulesStr, List<String> expectedModules) {
        clipConfigure.modules = modulesStr;
        List<String> moduleNames = clipConfigure.createModuleNames();
        assertThat(moduleNames).isEqualTo(expectedModules);
    }

    public Object[] parametersForCreateModuleNamesTest() {
        return $(
                $(MODULES, asList("module2", "module3", "module4")),
                $(MODULES_ONE, asList("module2"))
        );
    }

    @Test
    public void createProjectFilesTest() throws Exception {
        //given
        List<File> projectFiles = clipConfigure.createProjectFiles(asList("module2", "module3", "module4"));
        assertThat(projectFiles).hasSize(3);
        int cnt=2;
        for (File projectFile : projectFiles) {
            assertThat(projectFile).exists();
            assertThat(FileUtils.readFileToString(projectFile)).contains("module" + cnt++);
        }
    }

    @Test(expected = PomNotFoundException.class)
    public void createProjectFilesWithErrorTest(){
        List<File> files = clipConfigure.createProjectFiles(asList("module2", "module3", "module_error"));
        fail("Error!");
    }

    @Test
    public void integrateTest() throws Exception {
        clipConfigure.modules = MODULES;
        clipConfigure.execute();
    }



}
