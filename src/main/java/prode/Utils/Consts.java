package prode.Utils;

public final class Consts{

	public static final String ATTRIBUTEADMIN = "admin";
	public static final String ATTRIBUTELOGED = "logueado";
	public static final String ATTRIBUTEUSERNAME = "username";
	public static final String ATTRIBUTESCHEDULE = "schedule";
	public static final String ATTRIBUTELASTFIXTURE = "lastFixture";
	

  /**
   The caller references the constants using <tt>Consts.EMPTY_STRING</tt>, 
   and so on. Thus, the caller should be prevented from constructing objects of 
   this class, by declaring this private constructor. 
  */
  private Consts(){
    //this prevents even the native class from 
    //calling this ctor as well :
    throw new AssertionError();
  }
}
