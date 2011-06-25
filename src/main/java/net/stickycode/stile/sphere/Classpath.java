package net.stickycode.stile.sphere;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.stickycode.stile.artifact.Artifact;


public class Classpath
  implements Iterable<Artifact> {

  private List<Artifact> artifacts = new LinkedList<Artifact>();

  @Override
  public Iterator<Artifact> iterator() {
    return artifacts.iterator();
  }

  public void add(Artifact artifact) {
    artifacts.add(artifact);
  }

}
