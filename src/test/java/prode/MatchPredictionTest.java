package prode;

import prode.MatchPrediction;
import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MatchPredictionTest {
  @Before
  public void before(){
    Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode_test?nullNamePatternMatchesAll=true&useSSL=false", "root", "root");
    System.out.println("MatchPredictionTest setup");
    Base.openTransaction();
  }

  @After
  public void after(){
      System.out.println("MatchPredictionTest tearDown");
      Base.rollbackTransaction();
      Base.close();
  }

  @Test
  public void validatePrecenseOfPrediction(){
      MatchPrediction matchPrediction = new MatchPrediction();
      matchPrediction.set("prediction", "");
      assertEquals(matchPrediction.isValid(), false);
  }

  @Test
  public void validatePrecenseOfValidPrediction(){
      MatchPrediction matchPrediction = new MatchPrediction();
      matchPrediction.set("prediction", null);
      assertEquals(matchPrediction.isValid(), false);
  }

  @Test
  public void validateEnum(){
    MatchPrediction matchTest = new MatchPrediction();
    matchTest.set("prediction", "suspended");
    assertEquals(matchTest.isValid(), false);
  }
}