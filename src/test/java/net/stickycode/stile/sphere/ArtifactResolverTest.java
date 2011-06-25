package net.stickycode.stile.sphere;

import java.util.Collections;

import net.stickycode.mockwire.UnderTest;
import net.stickycode.mockwire.junit4.MockwireRunner;
import net.stickycode.stile.artifact.Dependency;
import net.stickycode.stile.version.range.ComponentVersionRangeParser;

import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(MockwireRunner.class)
public class ArtifactResolverTest {
  
  @UnderTest
  LowestMatchingVersionArtifactResolver resolver;
  
  @UnderTest
  ComponentVersionRangeParser parser;
  
  @Test(expected=RuntimeException.class)
  public void notFound() {
    resolver.resolve(Collections.singletonList(new Dependency("a", parser.parseVersionRange("[1,2)"))));
  }
}
