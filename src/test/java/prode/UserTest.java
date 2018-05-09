package prode;

import prode.User;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {
  @Before
  public void before(){
    Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode_test?nullNamePatternMatchesAll=true", "franco", "franco");
    System.out.println("UserTest setup");
    Base.openTransaction();
  }

  @After
  public void after(){
      System.out.println("UserTest tearDown");
      Base.rollbackTransaction();
      Base.close();
  }

  @Test
  public void validatePrecenseOfUsernames(){
      User user = new User();
      user.set("nick","","password","123","dni","5");
      assertEquals(user.isValid(), false);
  }

  @Test
  public void validatePrecenseOfPasswords(){
      User user = new User();
      user.set("nick", "asd","password","");
      assertEquals(user.isValid(), false);
  }
}
