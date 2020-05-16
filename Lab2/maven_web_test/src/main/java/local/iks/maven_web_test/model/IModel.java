package local.iks.maven_web_test.model;

import local.iks.maven_web_test.model.dto.DTO_Student;
import local.iks.maven_web_test.model.dto.DTO_User;
import java.util.List;


public interface IModel  {
  
  String issueToken(String userName);
  String extractUserFromTokenIfValid(String token) throws Exception;
  int retrieveRowsCountByJPA(String id) throws Exception;
  String retrieveUserById(String id) throws Exception;        
  boolean authenticate(DTO_User user);  
  void run(String userName, List<DTO_Student> students) throws Exception;  
}