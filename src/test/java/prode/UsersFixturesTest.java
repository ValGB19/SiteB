package prode;

import prode.UsersFixtures;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UsersFixturesTest {
  @Before
  public void before(){
    Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode_test?nullNamePatternMatchesAll=true&useSSL=false", "root", "root");
    System.out.println("UsersFixturesTest setup");
    Base.openTransaction();
  }

  @After
  public void after(){
      System.out.println("UsersFixturesTest tearDown");
      Base.rollbackTransaction();
      Base.close();
  }

  @Test
  public void validatePrecenseOfRelacion(){
      UsersFixtures usersFixturesTest = new UsersFixtures();
      usersFixturesTest.set("user_id","","fixture_id","");
      assertEquals(usersFixturesTest.isValid(), false);
  }

  @Test
  public void validatePrecenseOfRelacion2(){
      UsersFixtures usersFixturesTest = new UsersFixtures();
      usersFixturesTest.set("user_id","","fixture_id",5);
      assertEquals(usersFixturesTest.isValid(), false);
  }

  @Test
  public void validatePrecenseOfRelacion3(){
      UsersFixtures usersFixturesTest = new UsersFixtures();
      usersFixturesTest.set("user_id", 5,"fixture_id","");
      assertEquals(usersFixturesTest.isValid(), false);
  }
}
