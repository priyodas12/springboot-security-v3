package io.springboot.springbootsecurityv3.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
  @PreAuthorize("hasAuthority('PRODUCT_READ')")
  public ResponseEntity<Product> getProduct(@PathVariable("id") final Integer id){
    logger.info("-----------getProduct() starts ---------");
    loadDefaultProduct();

    if(cacheProductMap.keySet().contains(id)){
      logger.info("Search in cache for product id :{}",id);
      return new ResponseEntity<Product>(cacheProductMap.get(id),HttpStatus.OK);
    }else{
      logger.info("Search in repository for product id :{}",id);
      cacheProductMap.put(id, productService.getProductInfo(id));
      return new ResponseEntity<Product>(cacheProductMap.getOrDefault(id, cacheProductMap.get(0)),HttpStatus.OK);

    }

  }

  @PostMapping("/")
  @PreAuthorize("hasAuthority('PRODUCT_WRITE')")
  public ResponseEntity<Product> addProduct(@RequestBody Product product){
    logger.info("-----------addProduct() starts ---------");
    loadDefaultProduct();
    if(cacheProductMap.keySet().contains(product.getId())){
      logger.info("Product exist cache ,Adding updated :{}",product.getId());
      cacheProductMap.remove(product.getId());
    }else{
      logger.info("Adding Product :{}",product.getId());
    }
    cacheProductMap.put(product.getId(),product);
    return new ResponseEntity<Product>(cacheProductMap.get(product.getId()),HttpStatus.OK);
  }

  @PutMapping("/")
  @PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
  public ResponseEntity<Product> modifyProduct(@RequestBody Product product){
    logger.info("-----------modifyProduct() starts ---------");
    loadDefaultProduct();
    if(cacheProductMap.keySet().contains(product.getId())){
      logger.info("Product exist cache ,Adding updated :{}",product.getId());
      cacheProductMap.remove(product.getId());
      cacheProductMap.put(product.getId(),product);
    }else{
      logger.info("Product Id: {} does not exist!",product.getId());
      throw new RuntimeException("Product Id:"+product.getId()+" does not exist!");
    }
    return new ResponseEntity<Product>(cacheProductMap.get(product.getId()),HttpStatus.OK);
  }


  @DeleteMapping ("/{id}")
  @PreAuthorize("hasAuthority('PRODUCT_DELETE')")
  public ResponseEntity<Boolean> deleteProduct(@PathVariable("id") final Integer id){
    logger.info("-----------deleteProduct() starts ---------");
    if(cacheProductMap.keySet().contains(id)){
      cacheProductMap.remove(id);
      return new ResponseEntity<Boolean>(Boolean.TRUE,HttpStatus.OK);
    }else{
      return new ResponseEntity<Boolean>(Boolean.FALSE,HttpStatus.OK);
    }
  }

  @GetMapping("/cache")
  public ResponseEntity<List<Product>> getAllCache(){
    logger.info("Search in cache for all products");
    List<Product> products= cacheProductMap.values().stream().map(p->p).collect(Collectors.toList());
    return new ResponseEntity<>(products,HttpStatus.OK);
  }

}
