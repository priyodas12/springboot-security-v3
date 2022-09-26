package io.springboot.springbootsecurityv3.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.javafaker.Faker;

import io.springboot.springbootsecurityv3.pojos.Product;



@Service
public class ProductService {
  
  public static Logger logger=LoggerFactory.getLogger(ProductService.class);
  
  public Product getProductInfo(Integer id){
      Product p=new Product();
      
      p.setId(id);
      p.setName(Faker.instance().beer().name().toString());
      p.setPrice(Faker.instance().random().nextInt(1, 100));

      logger.info("Searched product id: {} , details: {}",id,p);
      return p;
        
  }
}
