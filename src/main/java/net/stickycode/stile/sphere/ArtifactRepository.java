package net.stickycode.stile.sphere;

import java.util.List;

import net.stickycode.stile.artifact.Artifact;
import net.stickycode.stile.version.Version;


public interface ArtifactRepository {

  Artifact lookup(ArtifactReference reference);

  List<Version> lookupVersions(String id);

  Artifact load(String id, Version version);

}
