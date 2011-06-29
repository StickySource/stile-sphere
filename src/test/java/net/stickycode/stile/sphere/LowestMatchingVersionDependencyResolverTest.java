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

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import net.stickycode.mockwire.Uncontrolled;
import net.stickycode.mockwire.UnderTest;
import net.stickycode.mockwire.junit4.MockwireRunner;
import net.stickycode.stile.artifact.Artifact;
import net.stickycode.stile.artifact.Dependency;
import net.stickycode.stile.version.Version;
import net.stickycode.stile.version.component.ComponentVersionParser;
import net.stickycode.stile.version.range.ComponentVersionRangeParser;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(MockwireRunner.class)
public class LowestMatchingVersionDependencyResolverTest {

  @UnderTest
  LowestMatchingVersionDependencyResolver resolver;

  @UnderTest
  ComponentVersionRangeParser parser;

  @UnderTest
  ComponentVersionParser versionParser;

  @Uncontrolled
  FakeArtifactRepository repository;

  @Test(expected = ArtifactVersionNotFoundException.class)
  public void notFound() {
    resolver.resolve("noversions", v("1"));
  }

  @Test(expected = ArtifactVersionNotFoundException.class)
  public void notFoundWithOneAbove() {
    resolver.resolve("aboverange", v("1"));
  }

  @Test(expected = ArtifactVersionNotFoundException.class)
  public void notFoundWithOneBelow() {
    resolver.resolve("belowrange", v("1"));
  }

  @Test
  public void found() {
    assertThat(resolver.resolve("a", v("1"))).containsExactly(new Artifact("a", v("1")));
  }

  @Test
  public void foundPointVersion() {
    assertThat(resolver.resolve("just11", v("1.1"))).containsExactly(new Artifact("just11", v("1.1")));
  }

  @Test(expected=NonIntersectingVersionRangeException.class)
  public void conflict() {
    resolver.resolve("a", v("2"));
  }

  private List<Dependency> dep(String id, String specification) {
    return Collections.singletonList(new Dependency(id, parser.parseVersionRange(specification)));
  }

  private Version v(String version) {
    return versionParser.parse(version);
  }
}
