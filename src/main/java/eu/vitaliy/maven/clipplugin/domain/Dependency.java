package eu.vitaliy.maven.clipplugin.domain;

/**
 * User: xaoc
 * Date: 23.06.14
 * Time: 22:09
 */
public class Dependency {
    String groupId;
    String artifactId;
    String version;

    public Dependency() {
    }

    private Dependency(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }


    public Dependency of(String groupId, String artifactId, String version) {
        return new Dependency(groupId, artifactId, version);
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
