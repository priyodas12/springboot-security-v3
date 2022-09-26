package io.springboot.springbootsecurityv3.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

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
      logger.info("Search in cache for product id :{},{}",id);
      return cacheProductMap.get(id);
    }else{
      logger.info("Search in repository for product id :{}",id);
      cacheProductMap.put(id, productService.getProductInfo(id));
      return cacheProductMap.getOrDefault(id, cacheProductMap.get(0));
    }

  }

  @PostMapping("/")
  public Product addProduct(@RequestBody Product product){
    loadDefaultProduct();
    if(cacheProductMap.keySet().contains(product.getId())){
      logger.info("Product exist cache ,Adding updated :{}",product.getId());
      cacheProductMap.remove(product.getId());
    }else{
      logger.info("Adding Product :{}",product.getId());
    }
    cacheProductMap.put(product.getId(),product);
    return product;
  }

  @PutMapping("/")
  public Product modifyProduct(@RequestBody Product product){
    loadDefaultProduct();
    if(cacheProductMap.keySet().contains(product.getId())){
      logger.info("Product exist cache ,Adding updated :{}",product.getId());
      cacheProductMap.remove(product.getId());
      cacheProductMap.put(product.getId(),product);
    }else{
      logger.info("Product Id: {} does not exist!",product.getId());
      throw new RuntimeException("Product Id:"+product.getId()+" does not exist!");
    }
    return product;
  }


  @DeleteMapping ("/{id}")
  public Boolean deleteProduct(@PathVariable("id") final Integer id){
    if(cacheProductMap.keySet().contains(id)){
      cacheProductMap.remove(id);
      return Boolean.TRUE;
    }else{
      return Boolean.FALSE;
    }
  }

  @GetMapping("/cache")
  public List<Product> getAllCache(){
    logger.info("Search in cache for all products");
    return cacheProductMap.values().stream().map(p->p).collect(Collectors.toList());
  }

}
