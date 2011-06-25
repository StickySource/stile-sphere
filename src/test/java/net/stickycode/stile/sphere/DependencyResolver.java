package net.stickycode.stile.sphere;

import java.util.List;

import net.stickycode.stile.artifact.Artifact;
import net.stickycode.stile.artifact.Dependency;


public interface DependencyResolver {

  List<Artifact> resolve(List<Dependency> dependency);

}
