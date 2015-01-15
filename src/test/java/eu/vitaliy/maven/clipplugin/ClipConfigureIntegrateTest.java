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
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

/**
 * User: xaoc
 * Date: 23.06.14
 * Time: 22:12
 */
//@RunWith(JUnitParamsRunner.class)
public class ClipConfigureIntegrateTest {
    private static final String MODULES = "module2,module3,module4";
    ClipConfigure clipConfigure;

    @Before
    public void before() throws Exception {
        clipConfigure = new ClipConfigure();
    }

    @Test
    public void integrateTest() throws Exception {
        clipConfigure.modules = MODULES;
        clipConfigure.versionConfigureWay="LATEST";
        clipConfigure.baseDir = new File("./src/it/module1");
        clipConfigure.execute();
    }



}
