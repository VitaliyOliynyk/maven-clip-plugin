package eu.vitaliy.maven.clipplugin.domain;

/**
 * User: xaoc
 * Date: 23.06.14
 * Time: 22:09
 */
public class Dependency {
    private String groupId;
    private String artifactId;
    private String version;

    private Dependency(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }


    public Dependency of(String groupId, String artifactId, String version) {
        return new Dependency(groupId, artifactId, version);
    }

    @Override
    public String toString() {
        return "Dependency{" +
                "groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
