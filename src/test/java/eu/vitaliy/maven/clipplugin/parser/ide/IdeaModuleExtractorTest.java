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
