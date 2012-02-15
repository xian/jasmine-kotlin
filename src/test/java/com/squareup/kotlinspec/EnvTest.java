package com.squareup.kotlinspec;

import com.squareup.jasmine4k.Env;
import com.squareup.jasmine4k.Spec;
import com.squareup.jasmine4k.ThingTests;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.squareup.Expect.expect;

public class EnvTest {

  private Env env;

  @Before
  public void setUp() throws Exception {
    env = new Env();
    Env.$classobj.currentEnv = env;
  }

  @After
  public void tearDown() throws Exception {
    Env.$classobj.currentEnv = null;
  }

  @Test
  public void shouldSetUpSpecsAndSuites() throws Exception {
    new ThingTests().testForTestEnvSetup();
    List<Spec> topLevelSpecs = env.getChildSpecs();
    expect(topLevelSpecs.size()).toEqual(1);
    Spec outerSuite = topLevelSpecs.get(0);
    expect(outerSuite.getName()).toEqual("MyClass");

    List<Spec> childSpecs = outerSuite.getChildSpecs();
    expect(childSpecs.size()).toEqual(2);

    expect(childSpecs.get(0).getName()).toEqual("should have behavior A");
    expect(childSpecs.get(1).getName()).toEqual("other case");
    expect(((Spec) childSpecs.get(1).getChildSpecs().get(0)).getName()).toEqual("should have behavior B");
  }

  @Test
  public void shouldRunBeforesAndAftersInTheCorrectOrder() throws Exception {
    List<String> log = new ArrayList<String>();
    new ThingTests().testBeforesAndAfters(log);
    expect(log).toBeEmpty();

    env.execute();
    expect(log).toEqual(Arrays.asList(
        "outer before",
        "in first it",
        "outer after",

        "outer before",
        "context before 1",
        "context before 2",
        "in inner it",
        "context after 1",
        "context after 2",
        "outer after",

        "outer before",
        "in third it",
        "outer after"
    ));
  }
}
