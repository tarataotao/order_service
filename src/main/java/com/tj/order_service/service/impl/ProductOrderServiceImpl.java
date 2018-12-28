package com.tj.order_service.service.impl;

import com.tj.order_service.domain.ProductOrder;
import com.tj.order_service.service.ProductOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    @Autowired
    private RestTemplate restTemplate;

//    @Autowired
//    private LoadBalancerClient loadBalancerClient;

    @Override
    public ProductOrder save(int userId, int productId) {

        /**
         * 两种调用方式
         * 第一种
         */
       Map<String,Object> productMap=restTemplate.getForObject("http://product-service/api/v1/product/find?id="+productId,Map.class);

        /**
         * 第二种

        ServiceInstance instance=loadBalancerClient.choose("product-service");
        String url=String.format("http://%s:%s/%s",instance.getHost(),instance.getPort(),"api/v1/product/find?id="+productId);
        RestTemplate restTemplate2=new RestTemplate();
        Map<String,Object> productMap=restTemplate2.getForObject(url,Map.class);
         */
        //获取商品详情 todo
        ProductOrder productOrder= ProductOrder.builder().createTime(new Date())
                .userId(userId).tradeNo(UUID.randomUUID().toString())
                .productName(productMap.get("name")+toString())
                .price(Integer.parseInt(productMap.get("price").toString())).build();

        return productOrder;
    }
}
