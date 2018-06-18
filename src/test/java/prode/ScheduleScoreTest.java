package prode;

import prode.ScheduleScore;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScheduleScoreTest {
  @Before
  public void before(){
    Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode_test?nullNamePatternMatchesAll=true&useSSL=false", "root", "root");
    System.out.println("ScheduleScoreTest setup");
    Base.openTransaction();
  }

  @After
  public void after(){
      System.out.println("ScheduleScoreTest tearDown");
      Base.rollbackTransaction();
      Base.close();
  }

  @Test
  public void validatePrecenseOfValidScore(){
      ScheduleScore scheduleScore = new ScheduleScore();
      scheduleScore.set("score",-3);
      assertEquals(scheduleScore.isValid(), false);
  }

  @Test
  public void validatePrecenseOfUserAsoc(){
      ScheduleScore scheduleScore = new ScheduleScore();
      scheduleScore.set("user_id", null);
      assertEquals(scheduleScore.isValid(), false);
  }
}
