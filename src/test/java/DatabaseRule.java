import org.junit.rules.ExternalResource;
import org.sql2o.*;
public class DatabaseRule extends ExternalResource{
    @Override
    protected  void before(){
      DB.sql2o=new Sql2o("jdbc:postgresql://localhost:5432/kitenge","pauline","pauline");

    }

    @Override
    protected  void after(){
      try(Connection con =DB.sql2o.open()){
      	String deleteClothesQuery="Delete from kitenge *;";
      	con.createQuery(deleteClothesQuery).executeUpdate();


}
    }
   

  }
