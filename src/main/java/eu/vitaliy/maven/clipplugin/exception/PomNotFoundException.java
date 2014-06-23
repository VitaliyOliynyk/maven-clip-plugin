package eu.vitaliy.maven.clipplugin.exception;

import java.io.File;

/**
 * User: xaoc
 * Date: 24.06.14
 * Time: 00:48
 */
public class PomNotFoundException extends RuntimeException {
    public PomNotFoundException(File pomFile) {
        super("File not found: " + pomFile.getAbsolutePath());
    }
}
