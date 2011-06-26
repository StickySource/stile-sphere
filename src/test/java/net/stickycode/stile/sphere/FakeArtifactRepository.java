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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.stickycode.stile.artifact.Artifact;
import net.stickycode.stile.artifact.Dependency;
import net.stickycode.stile.version.Version;
import net.stickycode.stile.version.VersionRange;
import net.stickycode.stile.version.component.ComponentVersionParser;
import net.stickycode.stile.version.range.ComponentVersionRangeParser;

public class FakeArtifactRepository
    implements ArtifactRepository {

  private Logger log = LoggerFactory.getLogger(FakeArtifactRepository.class);
  
  @Inject
  ComponentVersionRangeParser rangeParser;

  @Inject
  ComponentVersionParser versionParser;

  @Override
  public Artifact lookup(ArtifactReference reference) {
    throw new RuntimeException("Nope");
  }

  @Override
  public List<Version> lookupVersions(String id) {
    if ("just11".equals(id))
      return versions("1.1");

    if ("lessthan1".equals(id))
      return versions("0.9", "0.8", "0.1");

    if ("single1".equals(id))
      return versions("1");

    if ("b".equals(id))
      return versions("1", "2");

    if ("c".equals(id))
      return versions("1", "2");

    if ("a".equals(id))
      return versions("1", "2");

    if ("d".equals(id))
      return versions("1", "2");

    if ("e".equals(id))
      return versions("1", "2");

    throw new ArtifactVersionsNotFoundException(id, this);
  }

  @Override
  public Artifact load(String id, Version version) {
    log.debug("loading {}-{}", id, version);
    if ("single1".equals(id))
      return new Artifact(id, version);

    if ("just11".equals(id))
      return new Artifact(id, version);
    
    if ("e".equals(id))
      return new Artifact(id, version);

    if ("c".equals(id)) {
      if (v1().equals(version))
        return new Artifact(id, version);

      if (v2().equals(version))
        return withDeps("c", version, dep("d", "[1,2)"));
    }

    if ("a".equals(id)) {
      if (v1().equals(version))
        return new Artifact(id, version);

      if (v2().equals(version))
        return withDeps(id, version, dep("b", "[1,2)"), dep("c", "[1,2)"));
    }

    if ("b".equals(id))
      return withDeps(id, version, dep("c", "[2,3)"));

    if ("d".equals(id))
      return withDeps(id, version, dep("e", "[1,3)"));

    throw new ArtifactVersionNotFoundException(id, version, this);
  }

  private Version v1() {
    return versionParser.parse("1");
  }

  private Version v2() {
    return versionParser.parse("2");
  }

  private Dependency dep(String id, String range) {
    return new Dependency(id, range(range));
  }

  private Artifact withDeps(String id, Version version, Dependency... dependencies) {
    Artifact artifact = new Artifact(id, version);
    for (Dependency dependency : dependencies) {
      artifact.addDependency(Spheres.Main, dependency);
    }
    return artifact;
  }

  private VersionRange range(String string) {
    return rangeParser.parseVersionRange(string);
  }

  private List<Version> versions(String... strings) {
    List<Version> versions = new ArrayList<Version>(strings.length);
    for (String version : strings) {
      versions.add(versionParser.parse(version));
    }
    return versions;
  }
}
