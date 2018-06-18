package prode;

import prode.Fixture;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FixtureTest {
  @Before
  public void before(){
    Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode_test?nullNamePatternMatchesAll=true&useSSL=false", "root", "root");
    System.out.println("FixtureTest setup");
    Base.openTransaction();
  }

  @After
  public void after(){
    System.out.println("FixtureTest tearDown");
    Base.rollbackTransaction();
    Base.close();
  }

  @Test
  public void validatePrecenseOfLeague(){
    Fixture fixtureTest = new Fixture();
    fixtureTest.set("league", "");
    assertEquals(fixtureTest.isValid(), false);
  }
  
  @Test
  public void validatePrecenseOfSomethingInLeague(){
    Fixture fixtureTest = new Fixture();
    fixtureTest.set("league", null);
    assertEquals(fixtureTest.isValid(), false);
  }

  @Test
  public void validateUniqueLeague(){
    new Fixture().set("league", "la").saveIt();
    Fixture fixtureTest = new Fixture();
    fixtureTest.set("league", "la");
    assertEquals(fixtureTest.isValid(), false);
  }
}