import org.junit.rules.ExternalResource;
import org.sql2o.*;
public class DatabaseRule extends ExternalResource{
    @Override
    protected  void before(){
      DB.sql2o=new Sql2o("jdbc:postgresql://localhost:5432/kitenge_test","james","admin");

    }

    @Override
    protected  void after(){
      try(Connection con =DB.sql2o.open()){
        String sqlCart="delete from cart *";
        String sqlDesigner="delete from designer *";

        con.createQuery(sqlCart).executeUpdate();
        con.createQuery(sqlDesigner).executeUpdate();
}
    }
}
