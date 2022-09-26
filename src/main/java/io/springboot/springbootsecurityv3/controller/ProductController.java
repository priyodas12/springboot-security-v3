package io.springboot.springbootsecurityv3.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.springboot.springbootsecurityv3.pojos.Product;
import io.springboot.springbootsecurityv3.service.ProductService;

@RestController
@RequestMapping("/api/v3/products")
public class ProductController {

  public static Logger logger=LoggerFactory.getLogger(ProductController.class);

  @Autowired
  private ProductService productService;

  Map<Integer,Product> cacheProductMap=new LinkedHashMap<>();

  public void loadDefaultProduct(){
      cacheProductMap.put(0, new Product());
  }
  
  @GetMapping("/{id}")
  public Product getProduct(@PathVariable("id") final Integer id){
    loadDefaultProduct();
    if(cacheProductMap.keySet().contains(id)){
      logger.info("Search in cache for product id :{}",id);
      return cacheProductMap.get(id);
    }else{
      logger.info("Search in repository for product id :{}",id);
      cacheProductMap.put(id, productService.getProductInfo(id));
      return cacheProductMap.getOrDefault(id, cacheProductMap.get(0));
    }
  }

}
