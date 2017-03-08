   /*
 * This Java source file was generated by the Gradle 'init' task.
 */
  import java.util.Map;
  import java.util.HashMap;
  import java.util.List;
  import spark.ModelAndView;
  import spark.template.velocity.VelocityTemplateEngine;
  import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
  staticFileLocation("/public");
  String layout = "templates/layout.vtl";
  String adminLayout = "templates/adminLayout.vtl";
  User adminUser=new User("admin","admin@admin.com","kitenge",User.USER_TYPE[0]);
  adminUser.save();
  String adminLayout="templates/adminLayout";

  get("/", (request,response) ->{
  Map<String, Object> model = new HashMap<String, Object>();
  model.put("session",request.session().attribute("user"));
  model.put("clothes",Clothes.allClothes());
  model.put("template", "templates/index.vtl");
  return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

  get("/login", (request,response) ->{
  Map<String, Object> model = new HashMap<String, Object>();
  if(request.session().attribute("user")==null){
    model.put("template", "templates/login-form.vtl");

  }else{
      String url = "/";
      response.redirect(url);
  }
  return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

  post("/login", (request,response) ->{
  Map<String, Object> model = new HashMap<String, Object>();
  String email = request.queryParams("email");
  String password = request.queryParams("password");
  User logInUser=User.findLogin(password);
  String url;
  if(logInUser==null){
       url = "/login";
  }else{
    request.session().attribute("user",logInUser);
     url = "/";
  }
  response.redirect(url);
  return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

  post("/signup", (request,response) ->{
  Map<String, Object> model = new HashMap<String, Object>();
  String name = request.queryParams("name");
  String email = request.queryParams("email");
  String password = request.queryParams("password");
  User user = new User(name, email, password,User.USER_TYPE[1]);
  user.save();
  request.session().attribute("user", user);
  String url = "/";
  response.redirect(url);
  return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

  get("/clothes", (request,response) ->{
  Map<String, Object> model = new HashMap<String, Object>();
  model.put("session",request.session().attribute("user"));
  model.put("clothes", Clothes.allClothes());
  model.put("template", "templates/clothes.vtl");
  return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

  get("/clothes/:id", (request,response) ->{
  Map<String, Object> model = new HashMap<String, Object>();
  int id=Integer.parseInt(request.params(":id"));
  model.put("session",request.session().attribute("user"));
  Clothes clothes =Clothes.find(id);
  model.put("clothe",clothes);
  model.put("template", "templates/cloth.vtl");
  return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

  //remove sesion
  post("/", (request,response) ->{
  Map<String, Object> model = new HashMap<String, Object>();
  request.session().removeAttribute("user");
  String url="/";
  response.redirect(url);
  return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

  //route to user
  get("/users/:id",(request,response) ->{
  Map<String, Object> model = new HashMap<String, Object>();
  int id=Integer.parseInt(request.params(":id"));
  model.put("session",request.session().attribute("user"));
  User user =User.find(id);
  model.put("user",user);
  model.put("template", "templates/user.vtl");
  return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

  post("/users/update/:id", (request,response) ->{
  Map<String, Object> model = new HashMap<String, Object>();
  int id=Integer.parseInt(request.params(":id"));
  User user =User.find(id);
  String name = request.queryParams("name");
  String email = request.queryParams("email");
  String password = request.queryParams("password");
  user.update(name,email,password);
  String url=String.format("/users/%d",id);
  response.redirect(url);
  return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

  post("/users/delete/:id", (request,response) ->{
  Map<String, Object> model = new HashMap<String, Object>();
  int id=Integer.parseInt(request.params(":id"));
  User user =User.find(id);
  user.delete();
  request.session().removeAttribute("user");
  String url="/";
  response.redirect(url);
  return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

  //route to cart
  get("/cart",(request,response) ->{
  Map<String, Object> model = new HashMap<String, Object>();
  User user=request.session().attribute("user");
  List<Cart> cart=user.getCart();
  model.put("session",request.session().attribute("user"));
  model.put("cart",cart);
  model.put("template", "templates/cart.vtl");
  return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

  // adding items cart from clothes
   post("/clothes/:id/cartAdd", (request,response) ->{
   Map<String, Object> model = new HashMap<String, Object>();
   User user=request.session().attribute("user");
   int userid  = user.getId();
   int kitengeid  = Integer.parseInt(request.params(":id"));
   int quantity= Integer.parseInt(request.queryParams("items"));
   Cart newcart = new Cart (userid,quantity,kitengeid);
   newcart.save();
   String url=String.format("/clothes/%d",kitengeid);
   response.redirect(url);
   return new ModelAndView(model, layout);
   }, new VelocityTemplateEngine());

   // delete items from cart
  post("/cart/:id/delete", (request,response) ->{
  Map<String, Object> model = new HashMap<String, Object>();
  Cart newCart = Cart.find(Integer.parseInt(":id"));
  newCart.delete();
  String url="/cart";
  response.redirect(url);
  return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

   //List of all designers
  get("/designers",(request,response) ->{
  Map<String, Object> model = new HashMap<String, Object>();
  model.put("session",request.session().attribute("user"));
  model.put("designer",Designer.all());
  model.put("template", "templates/designers.vtl");
  return new ModelAndView(model, layout);
  }, new VelocityTemplateEngine());

   //List of all designers
   get("/designers/:id",(request,response) ->{
   Map<String, Object> model = new HashMap<String, Object>();
   int id = Integer.parseInt(request.params(":id"));
   Designer designer = Designer.find(id);
   model.put("session",request.session().attribute("user"));
   model.put("designer",designer);
   model.put("template", "templates/designer.vtl");
   return new ModelAndView(model, layout);
   }, new VelocityTemplateEngine());

  //  Admin Section
  //  get all designers
  get("/admin/designers",(request,response) ->{
  Map<String, Object> model = new HashMap<String, Object>();
  request.session().attribute("user");
  User user=request.session().attribute("user");
  if(user.getType().equals("admin")){
    model.put("session",request.session().attribute("user"));
    model.put("designer",Designer.all());
    model.put("template", "templates/admin-designers.vtl");
  }
  else{
    String url = "/";
    response.redirect(url);
  }
  return new ModelAndView(model, adminLayout);
  }, new VelocityTemplateEngine());

  //adding new designers
  get("admin/designers/new",(request,response) ->{
  Map<String, Object> model = new HashMap<String, Object>();
  User user=request.session().attribute("user");
  if(user.getType().equals("admin")){
    model.put("session",request.session().attribute("user"));
    model.put("template", "templates/admin-designer-form.vtl");
  }
  else{
    String url = "/";
    response.redirect(url);
  }
  return new ModelAndView(model,adminLayout);

  }, new VelocityTemplateEngine());

//.admin individual designer......................
    get("/admin/designers/:id",(request,response) ->{
    Map<String, Object> model = new HashMap<String, Object>();
    User user=request.session().attribute("user");
    if(user.getType().equals("admin")){
      int id = Integer.parseInt(request.params(":id"));
      Designer designer=Designer.find(id);
      model.put("session",request.session().attribute("user"));
      model.put("designer",designer);
      model.put("template", "templates/admin-designer.vtl");
    }
    else{
      String url = "/";
      response.redirect(url);
    }
    return new ModelAndView(model, adminLayout);
    }, new VelocityTemplateEngine());

    //saving new designer
    post("/admin/designers/new",(request,response) ->{
    Map<String, Object> model = new HashMap<String, Object>();
    String url ;
    User user=request.session().attribute("user");

      String name = request.queryParams("name");
      Designer designer = new Designer(name);
      designer.save();
       url = "/admin/designers";

    response.redirect(url);
    return new ModelAndView(model, adminLayout);
    }, new VelocityTemplateEngine());

    // delete a designer
    post("/admin/designers/:id/delete",(request,response) ->{
    Map<String, Object> model = new HashMap<String, Object>();
    String url ;
    User user=request.session().attribute("user");
    if(user.getType().equals("admin")){
       int id = Integer.parseInt(request.params(":id"));
       Designer designer=Designer.find(id);
       designer.delete();
       url = "/admin/designers";
    }
    else{
      url = "/";

    }
    response.redirect(url);
    return new ModelAndView(model,adminLayout);
    }, new VelocityTemplateEngine());

    //route to update designer
    post("/admin/designers/:id/update",(request,response) ->{
    Map<String, Object> model = new HashMap<String, Object>();
    String url;
    User user=request.session().attribute("user");
    if(user.getType().equals("admin")){
       int id = Integer.parseInt(request.params(":id"));
       Designer designer=Designer.find(id);
       String name = request.queryParams("name");
       designer.update(name);
       url = "/admin/designers";
    }
    else{
      url = "/";
    }
    response.redirect(url);
    return new ModelAndView(model, adminLayout);
    }, new VelocityTemplateEngine());

    //Clothes Section
    get("/admin/clothes",(request,response) ->{
    Map<String, Object> model = new HashMap<String, Object>();
    User user=request.session().attribute("user");
    if(user.getType().equals("admin")){
      model.put("session",request.session().attribute("user"));
      model.put("clothes",Clothes.all());
      model.put("template", "templates/admin-clothes.vtl");
    }
    else{
      String url = "/";
      response.redirect(url);
    }
    return new ModelAndView(model, adminLayout);
    }, new VelocityTemplateEngine());

    get("/admin/clothes/:id",(request,response) ->{
    Map<String, Object> model = new HashMap<String, Object>();
    User user=request.session().attribute("user");
      if(user.getType().equals("admin")){
      int id = Integer.parseInt(request.params(":id"));
      Clothes clothes=Clothes.find(id);
      Designer designer=Designer.find(clothes.getDesignerId());
      model.put("designer",designer);
      model.put("session",request.session().attribute("user"));
      model.put("clothes", clothes);
      model.put("template", "templates/admin-cloth.vtl");
    }
    else{
      String url = "/";
      response.redirect(url);
    }

    return new ModelAndView(model,adminLayout);
    }, new VelocityTemplateEngine());

    get("/admin/clothes/new",(request,response) ->{
    Map<String, Object> model = new HashMap<String, Object>();
    User user=request.session().attribute("user");
    if(user.getType().equals("admin")){
      model.put("designers",Designer.all());
      model.put("session",request.session().attribute("user"));
      model.put("template", "templates/admin-cloth-form.vtl");
    }
    else{
      String url = "/";
      response.redirect(url);
    }
    return new ModelAndView(model, adminLayout);
    }, new VelocityTemplateEngine());

    post("/admin/cloth/new",(request,response) ->{
    Map<String, Object> model = new HashMap<String, Object>();
    String url;
    User user=request.session().attribute("user");
    if(user.getType().equals("admin")){
      String name = request.queryParams("name");
      String description = request.queryParams("description");
      int quantity = Integer.parseInt(request.queryParams("quantity"));
      String size = request.queryParams("size");
      int price = Integer.parseInt(request.queryParams("price"));
      Designer designer = Designer.find(Integer.parseInt(request.queryParams("designerId")));
      String imgUrl = request.queryParams("imgUrl");
      Clothes cloth = new Clothes(name, description, quantity, size, price, designer.getId(), imgUrl);
      cloth.save();
      url = "/admin/clothes";
    }
    else{
       url = "/";
    }
    response.redirect(url);
    return new ModelAndView(model, adminLayout);
    }, new VelocityTemplateEngine());

    post("/admin/clothes/:id/delete",(request,response) ->{
    Map<String, Object> model = new HashMap<String, Object>();
    String url;
    User user=request.session().attribute("user");
    if(user.getType().equals("admin")){
       int id = Integer.parseInt(request.params(":id"));
       Clothes clothes = Clothes.find(id);
       clothes.delete();
        url = "/admin/clothes";
    }
    else{
       url = "/";
    }
    response.redirect(url);
    return new ModelAndView(model, adminLayout);
    }, new VelocityTemplateEngine());

    //route to update clothes
    post("/admin/clothes/:id/update",(request,response) ->{
    Map<String, Object> model = new HashMap<String, Object>();
    String url;
    User user=request.session().attribute("user");
    if(user.getType().equals("admin")){
       int id = Integer.parseInt(request.params(":id"));
       Clothes clothes = Clothes.find(id);
       String name = request.queryParams("name");
       String description = request.queryParams("description");
       int quantity = Integer.parseInt(request.queryParams("quantity"));
       String size = request.queryParams("size");
       int price =Integer.parseInt(request.queryParams("price"));
       clothes.update(name, description, quantity, size, price);
       model.put("name", name);
       url = "/admin/clothes";
    }
    else{
       url = "/";

    }
    response.redirect(url);
    return new ModelAndView(model, adminLayout);
    }, new VelocityTemplateEngine());
}
}
