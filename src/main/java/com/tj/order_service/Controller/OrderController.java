package com.tj.order_service.Controller;

import com.netflix.discovery.converters.Auto;
import com.tj.order_service.domain.ProductOrder;
import com.tj.order_service.service.ProductOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {

    @Autowired
    private ProductOrderService productOrderService;

    @RequestMapping("save")
    public Object save(@RequestParam("user_id")int userId,@RequestParam("product_id") int productId){
        ProductOrder productOrder=productOrderService.save(userId,productId);
        return productOrder;
    }
}
