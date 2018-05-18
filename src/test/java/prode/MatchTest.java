package prode;

import prode.Match;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import java.util.Date;

public class MatchTest {
  @Before
  public void before(){
    Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode_test?nullNamePatternMatchesAll=true", "root", "root");
    System.out.println("MatchTest setup");
    Base.openTransaction();
  }

  @After
  public void after(){
    System.out.println("MatchTest tearDown");
    Base.rollbackTransaction();
    Base.close();
  }

  @Test
  public void validatePrecenseOfDate(){
    Match matchTest = new Match();
    matchTest.set("day", null);
    assertEquals(matchTest.isValid(), false);
  }
    
  @Test
  public void validatePrecenseOfSchedule(){
    Match matchTest = new Match();
    matchTest.set("day", new Date(2010,4,12));
    assertEquals(matchTest.isValid(), false);
  }

  @Test
  public void validateEnum(){
    Match matchTest = new Match();
    matchTest.set("day", new Date(2010,4,12), "result", "No Gano Riber");
    assertEquals(matchTest.isValid(), false);
  }

}