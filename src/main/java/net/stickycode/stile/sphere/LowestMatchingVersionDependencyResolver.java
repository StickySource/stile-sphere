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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.stickycode.stile.artifact.Artifact;
import net.stickycode.stile.artifact.Dependency;
import net.stickycode.stile.version.Bound;
import net.stickycode.stile.version.Version;

public class LowestMatchingVersionDependencyResolver
    implements DependencyResolver {

  private Logger log = LoggerFactory.getLogger(getClass());

  @Inject
  ArtifactRepository repository;

  @Override
  public List<Artifact> resolve(String id, Version version) {
    Artifact artifact = repository.load(id, version);
    return resolve(artifact);
  }

  public List<Artifact> resolve(Artifact artifact) {
    List<Dependency> dependencies = artifact.getDependencies(Spheres.Main);
    log.debug("resolving {} with {}", artifact, dependencies);
    if (dependencies.isEmpty())
      return Collections.singletonList(artifact);

    Resolutions root = new Resolutions(artifact);
    while (root.hasUnresolvedDependencies())
      for (Resolution resolution : root.getOutstandingResolutions()) {
        resolve(resolution);
      }
//    for (Dependency d : dependencies) {
//      root.add(d);
//      Artifact resolved = resolve(d);
//      resolutions.add(resolved);
//      artifacts.addAll(resolve(resolved));
//    }
    return root.getArtifactList();
  }

  private void resolve(Resolution resolution) {
    Dependency dependency = resolution.getDependency();
    log.debug("resolving {}", dependency);
    Bound lowerBound = dependency.getRange().getLowerBound();
    if (lowerBound.isExclusive())
      throw new RuntimeException("Dependency lower bound cannot be exclusive in order to provide stability");

    Version version = findVersion(dependency, lowerBound);

    resolution.setArtifact(repository.load(dependency.getId(), version));
  }

  private Version findVersion(Dependency dependency, Bound lowerBound) {
    for (Version version : repository.lookupVersions(dependency.getId())) {
      if (version.equals(lowerBound.getVersion())) {
        return version;
      }
    }

    throw new ArtifactVersionNotFoundException(dependency.getId(), lowerBound.getVersion(), repository);
  }

}
