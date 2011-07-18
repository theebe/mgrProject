package pl.mgrProject.test.model;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Configuration;
import org.testng.annotations.Test;


/**
 * Test komponentu encyjnego Linia
 * UWAGA: Do testów wymagana jest JAVA w wersji 5
 * @author bat
 *
 */
public class LiniaTest{

	
	 @Test
	 public void getAllTest() throws Exception  {
		
		 EntityManager em = getEntityManagerFactory().createEntityManager();
		
		
	 }
	 
	 private EntityManagerFactory emf;
	    
	    public EntityManagerFactory getEntityManagerFactory()
	    {
	        return emf;
	    }
	    
	    @BeforeClass
	    public void init() 
	    {
	        emf = Persistence.createEntityManagerFactory("mgrProject");
	    }
	    
	    @AfterClass
	    public void destroy()
	    {
	        emf.close();
	    }
	 
//	 @Test
//	 public void getPoPrzystankuTest(){	 
//	 }
	 
	 
	 
}
