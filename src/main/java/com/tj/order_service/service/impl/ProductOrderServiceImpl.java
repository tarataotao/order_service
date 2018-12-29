package com.tj.order_service.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.tj.order_service.domain.ProductOrder;
import com.tj.order_service.service.ProductClient;
import com.tj.order_service.service.ProductOrderService;
import com.tj.order_service.utils.JsonUtils;
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
                .productName(productMap.get("name").toString())
                .price(Integer.parseInt(productMap.get("price").toString())).build();

        return productOrder;
    }


    @Autowired
    private ProductClient productClient;
    @Override
    public ProductOrder saveByOpenReign(int userId, int productId) {
        //调用订单服务
        String response=productClient.findById(productId);

        //调用用户服务，主要是获取用户名称，用户的级别或者积分信息
        //TODO

        JsonNode jsonNode=JsonUtils.str2JsonNode(response);
        ProductOrder productOrder= ProductOrder.builder().createTime(new Date())
                .userId(userId).tradeNo(UUID.randomUUID().toString())
                .productName(jsonNode.get("name").toString())
                .price(Integer.parseInt(jsonNode.get("price").toString())).build();
        return productOrder;
    }


}
