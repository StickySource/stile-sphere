package net.stickycode.stile.sphere;

import javax.inject.Inject;

import net.stickycode.stile.artifact.Artifact;
import net.stickycode.stile.version.Version;


public class SphereResolver {

  @Inject
  private ArtifactRepository repository;
  
  @Inject
  private DependencyResolver resolver;
  
  public Classpath resolveClasspath(Spheres main, String id, Version version) {
    Classpath classpath = new Classpath();
    Artifact artifact = repository.load(id, version);
    classpath.add(artifact);
    for (Artifact a : resolver.resolve(artifact.getDependencies(main)))
      classpath.add(a);
//    for (Dependency dependency : artifact.getDependencies(main)) {
//      ArtifactReference ref = resolver.resolve(dependency);
//      classpath.add(repository.lookup(ref));
//    }
    return classpath;
  }

}
