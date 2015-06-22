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


import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

public class ProjectInfo {
    File projectPomFile;
    Collection<File> modulePomFiles = new LinkedList<File>();

    public File getProjectPomFile() {
        return projectPomFile;
    }

    public void setProjectPomFile(File projectPomFile) {
        this.projectPomFile = projectPomFile;
    }

    public Collection<File> getModulePomFiles() {
        return modulePomFiles;
    }


    public void setModulePomFiles(Collection<File> modulePomFiles) {
        this.modulePomFiles = modulePomFiles;
    }

    public void addModulePomFile(File modulePomFile) {
        modulePomFiles.add(modulePomFile);
    }
}
