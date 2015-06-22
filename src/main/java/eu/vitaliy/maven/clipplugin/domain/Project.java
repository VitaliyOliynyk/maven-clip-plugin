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

package eu.vitaliy.maven.clipplugin.domain;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * User: xaoc
 * Date: 26.06.14
 * Time: 00:20
 */
public class Project extends Module{
    public List<Module> modules;

    public Project(File pomFile) {
        super(pomFile);
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    @Override
    public void configure(Collection<Module> modules) {
        super.configure(modules);
        for (Module module : modules) {
            module.configure(modules);
        }
    }
}
