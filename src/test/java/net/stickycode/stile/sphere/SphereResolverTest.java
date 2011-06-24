package net.stickycode.stile.sphere;

import net.stickycode.stile.artifact.Artifact;
import net.stickycode.stile.version.component.ComponentVersion;

import org.junit.Test;


public class SphereResolverTest {

  @Test
  public void resolve() {
    Artifact artifact = new Artifact("a", new ComponentVersion());
    Classpath classpath = new SphereResolver().resolveClasspath(artifact);
  }

}
