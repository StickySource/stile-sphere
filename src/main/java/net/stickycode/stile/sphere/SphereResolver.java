/**
 * Copyright (c) 2011 RedEngine Ltd, http://www.RedEngine.co.nz. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
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
//    Artifact artifact = repository.load(id, version);
//    classpath.add(artifact);
    for (Artifact a : resolver.resolve(id, version))
      classpath.add(a);
//    for (Dependency dependency : artifact.getDependencies(main)) {
//      ArtifactReference ref = resolver.resolve(dependency);
//      classpath.add(repository.lookup(ref));
//    }
    return classpath;
  }

}
