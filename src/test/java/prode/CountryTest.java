package prode;

import prode.Country;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CountryTest {
  @Before
  public void before(){
    Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode_test?nullNamePatternMatchesAll=true&useSSL=false", "root", "root");
    System.out.println("CountryTest setup");
    Base.openTransaction();
  }

  @After
  public void after(){
      System.out.println("CountryTest tearDown");
      Base.rollbackTransaction();
      Base.close();
  }

  @Test
  public void validatePrecenseOfName(){
      Country countryTest = new Country();
      countryTest.set("name", "");
      assertEquals(countryTest.isValid(), false);
  }
  
  @Test
  public void validatePrecenseOfSomethingInName(){
      Country countryTest = new Country();
      countryTest.set("name", null);
      assertEquals(countryTest.isValid(), false);
  }

  @Test
  public void validateUniqueName(){
    new Country().set("name", "China").saveIt();
    Country countryTest = new Country();
    countryTest.set("name", "China");
    assertEquals(countryTest.isValid(), false);
  }

}