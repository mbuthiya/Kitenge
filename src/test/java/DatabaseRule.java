import org.junit.rules.ExternalResource;
import org.sql2o.*;
public class DatabaseRule extends ExternalResource{
    @Override
    protected  void before(){
      DB.sql2o=new Sql2o("jdbc:postgresql://localhost:5432/kitenge_test","nombu","nombu");

    }

    @Override
    protected  void after(){
      try(Connection con =DB.sql2o.open()){
        String deleteUserQuery = "DELETE FROM users *;";
        con.createQuery(deleteUserQuery).executeUpdate();
      }
    }
  }
