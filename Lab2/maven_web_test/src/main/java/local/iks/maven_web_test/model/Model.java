package local.iks.maven_web_test.model;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.SecureRandom;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import local.iks.maven_web_test.model.entity.Entity_Student;
import local.iks.maven_web_test.model.dto.DTO_Student;
import local.iks.maven_web_test.model.dto.DTO_User;

import java.util.List;

//JDBC (Begin)
import java.sql.*;
//JDBC (End)

//Pool (Begin)
import javax.annotation.Resource;   
import javax.sql.DataSource;
   
import javax.naming.InitialContext;   
//Pool (End)   


//JPA (Begin)
import javax.persistence.*;
import javax.transaction.*;
//JPA (End)   


public class Model implements IModel  {
        
    private static Key generateSecretKey() {
        SecureRandom sr = new SecureRandom();
        byte[] keyBytes = new byte[20];
        sr.nextBytes(keyBytes);        
        return new SecretKeySpec(keyBytes,"DES");                
    }
    private final static Key SECRET_KEY = generateSecretKey();	    	

    
    //Pool (Begin)
    //@Resource(name = "jdbc/local_pg_test")
    private DataSource ds;
    //Pool (End)   

    //JPA (Begin)
    @PersistenceUnit(unitName = "local_pg_test_PersistenceUnit")
    private EntityManagerFactory entityManagerFactory;
   
    @Resource
    private UserTransaction userTransaction;
    //JPA (End)   
    
    
    //*************************
    
    private String getIdFromHTTPRequest() {
        return "1; DROP TABLE AUTH";
    }    
    private int retrieveRowsCountBySimpleJDBC() throws Exception {				
                
	try {			
            Class.forName("org.postgresql.Driver");            
			String url = "jdbc:postgresql://localhost:5432/students";
            String login = "postgres";
            String password = "postgres";			
            Connection con = DriverManager.getConnection(url, login, password);			
            try {
                Statement stmt = con.createStatement();                			
		
                String id = getIdFromHTTPRequest();
                ResultSet rs = stmt.executeQuery("SELECT * FROM \"students\" WHERE id="+id);								
                
		int count = 0;
                while (rs.next()) {                
		    //String str = rs.getString("id") + " " + rs.getString("name");                
		    count++;
                }				
                rs.close();
                stmt.close();				
                
		return count;
            } finally {
                con.close();
            }			
        } 
	catch (Exception e) { 		
            throw new Exception("Error while JDBC operating: " + e.getMessage());
        }
    }  
    @Override
    public String retrieveUserById(String id) throws Exception {				
                
	try {			
            Class.forName("org.postgresql.Driver");            
			String url = "jdbc:postgresql://localhost:5432/students";
            String login = "postgres";
            String password = "postgres";			
            Connection con = DriverManager.getConnection(url, login, password);			
            try {
                Statement stmt = con.createStatement();                			
		
               
                ResultSet rs = stmt.executeQuery("SELECT * FROM \"students\" WHERE id="+id);								
                
		
              	
                String str = rs.getString("id") + " " + rs.getString("name");
                rs.close();
                stmt.close();				
                
		return str;
            } finally {
                con.close();
            }			
        } 
	catch (Exception e) { 		
            throw new Exception("Error while JDBC operating: " + e.getMessage());
        }
    }
    
    private int retrieveRowsCountByPoolConnection() throws Exception {				
		try 
		{			            
		    try {	        
	         InitialContext initialContext = new InitialContext();
             ds = (DataSource) initialContext.lookup("jdbc/local_pg_test");
	        }	
	        catch(Exception e) {	        		      
		      throw new Exception("Error while Data Source initializing: " + e.getMessage());
	        }
			
            Connection con = ds.getConnection();		  
            try 
			{
                Statement stmt = con.createStatement();                			
				ResultSet rs = stmt.executeQuery("SELECT * FROM \"students\"");								
				int count = 0;
                while (rs.next()) {                
					//String str = rs.getString("id") + " " + rs.getString("name");                
					count++;
                }				
                rs.close();
                stmt.close();				
				return count;
            } finally {
                con.close();
            }			
        } 
		catch (Exception e) 
		{
            throw new Exception("Error while JDBC operating: " + e.getMessage());
        }
	}
	
    @Override
    public int retrieveRowsCountByJPA(String id) throws Exception {	   
	   EntityManager entityManager;
	   try {
	      entityManager = entityManagerFactory.createEntityManager();
	   }
       catch (Exception e) {
		  throw new Exception("Error while Entity Manager initializing: " + e.getMessage()); 
	   }	   
	          
       try {
        userTransaction.begin();
        entityManager.joinTransaction();
		String sql = "SELECT u FROM User u WHERE id=" + id; 
                entityManager.createQuery(sql);                                                                                                   		 		 
		 
		 /*
                 Entity_Student studentFind = entityManager.find(Entity_Student.class,new Integer(2));		 
		 studentFind.setStudentName("Student_Find");
		 entityManager.merge(studentFind);
		 
		 Entity_Student studentPersist = new Entity_Student();
		 studentPersist.setStudentID(new Integer(3));
		 studentPersist.setStudentName("Student_Persist");		 
		 entityManager.persist(studentPersist);
		 */
		 
        userTransaction.commit();
		 		 
        return students.size(); 
       }
	   catch (Exception e) {
         throw new Exception("Error while JPA operating: " + e.getMessage());
       }	   
   }
	

    private int retrieveRowsCount() throws Exception {	
	return 1*retrieveRowsCountBySimpleJDBC();
	//return 2*retrieveRowsCountByPoolConnection();
        //return 10*retrieveRowsCountByJPA();
    }	
	
    
    //*************************
    
    
    @Override
    public void run(String userName,List<DTO_Student> students) throws Exception {
	   int count = retrieveRowsCount();	
           students.forEach((student) -> {
               student.setId((student.getId()) + count);
        });
        
        DTO_Student student = new DTO_Student();        
        student.setId(0);
        student.setName(userName);
        students.add(student);
    } 

    @Override
    public boolean authenticate(DTO_User user) {
      String name = user.getName();
      String password = user.getPssword();
      return (name.equals("admin")) && (password.equals("123456"));        
    }

    @Override
    public String issueToken(String userName) {
                
        LocalDateTime expiryPeriod = LocalDateTime.now().plusMinutes(600L);
        Date expirationDateTime = Date.from(expiryPeriod.atZone(ZoneId.systemDefault()).toInstant());
                        
        return  Jwts.builder()
                .setSubject(userName)
                .claim("scope","user")
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .setIssuedAt(new Date())
                .setExpiration(expirationDateTime)
                .compact();                        
    }
    
    @Override
    public String extractUserFromTokenIfValid(String token) throws Exception {              
       Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);            
       return claims.getBody().getSubject();        
    }
}