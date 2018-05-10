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
    Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode_test?nullNamePatternMatchesAll=true", "root", "root");
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
    user.set("nick","","password","123","dni",5,"email","email@example.com");
    assertEquals(user.isValid(), false);
  }

  @Test
  public void validatePrecenseOfValidUsernames(){
    User user = new User();
    user.set("nick",null,"password","123","dni",5,"email","email@example.com");
    assertEquals(user.isValid(), false);
  }

  @Test
  public void validateUniqueUsername(){
    new User().set("nick","asd","password","123","dni",7,"email","email@example.com").saveIt();;
    User userTest = new User();
    userTest.set("nick","asd","password","123","dni",5,"email","email2@example.com");
    assertEquals(userTest.isValid(), false);
  }


  @Test
  public void validatePrecenseOfPasswords(){
    User user = new User();
    user.set("nick","asd","password","","dni",5,"email","email@example.com");
    assertEquals(user.isValid(), false);
  }

  @Test
  public void validatePrecenseOfValidPasswords(){
    User user = new User();
    user.set("nick","asd","password",null,"dni",5,"email","email@example.com");
    assertEquals(user.isValid(), false);
  }

  @Test
  public void validatePrecenseOfDni(){
    User user = new User();
    user.set("nick","asd","password","","dni",null,"email","email@example.com");
    assertEquals(user.isValid(), false);
  }

  @Test
  public void validatePrecenseOfValidDni(){
    User user = new User();
    user.set("nick","asd","password","","dni",-1,"email","email@example.com");
    assertEquals(user.isValid(), false);
  }  

  @Test
  public void validateUniqueDni(){
    new User().set("nick","nacho","password","123","dni",5,"email","email@example.com").saveIt();;
    User userTest = new User();
    userTest.set("nick","daniela","password","123","dni",5,"email","email2@example.com");
    assertEquals(userTest.isValid(), false);
  }
}
