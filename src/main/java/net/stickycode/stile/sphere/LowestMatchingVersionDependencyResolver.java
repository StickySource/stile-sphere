/**
 * Copyright (c) 2011 RedEngine Ltd, http://www.redengine.co.nz. All rights reserved.
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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import net.stickycode.stile.artifact.Artifact;
import net.stickycode.stile.artifact.Dependency;
import net.stickycode.stile.version.Bound;
import net.stickycode.stile.version.Version;

public class LowestMatchingVersionDependencyResolver
    implements DependencyResolver {

  @Inject
  ArtifactRepository repository;

  @Override
  public List<Artifact> resolve(List<Dependency> dependency) {
    if (dependency.isEmpty())
      return Collections.emptyList();
    
    List<Artifact> artifacts = new LinkedList<Artifact>();
    for (Dependency d : dependency) {
      Artifact resolved = resolve(d);
      artifacts.add(resolved);
      artifacts.addAll(resolve(resolved.getDependencies(Spheres.Main)));
    }
    return artifacts;
  }

  private Artifact resolve(Dependency dependency) {
    Bound lowerBound = dependency.getVersion().getLowerBound();
    if (lowerBound.isExclusive())
      throw new RuntimeException("Dependency lower bound cannot be exclusive in order to provide stability");

    Version version = findVersion(dependency, lowerBound);

    return repository.load(dependency.getId(), version);
  }

  private Version findVersion(Dependency dependency, Bound lowerBound) {
    for (Version version : repository.lookupVersions(dependency.getId())) {
      if (version.equals(lowerBound.getVersion())) {
        return version;
      }
    }

    throw new RuntimeException("Cant find " + dependency.getId() + "-" + lowerBound.getVersion());
  }

}
