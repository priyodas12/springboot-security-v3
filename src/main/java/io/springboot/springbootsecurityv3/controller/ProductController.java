package io.springboot.springbootsecurityv3.controller;

import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.javafaker.Faker;

@RestController
@RequestMapping("/api/v3/products")
public class ProductController {

  public static Logger logger=LoggerFactory.getLogger(ProductController.class);
  
  @GetMapping("/{id}")
  public String getProduct(@PathVariable("id") Integer id){
    logger.info("searched product id :{}",id);
    return Faker.instance().book().title().toString();
  }
}
