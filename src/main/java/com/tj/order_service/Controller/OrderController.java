package com.tj.order_service.Controller;

import com.netflix.discovery.converters.Auto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tj.order_service.domain.ProductOrder;
import com.tj.order_service.service.ProductClient;
import com.tj.order_service.service.ProductOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {

    @Autowired
    private ProductOrderService productOrderService;

    @RequestMapping("save")
    @HystrixCommand(fallbackMethod = "saveOrderFail")
    public Object save(@RequestParam("user_id")int userId,@RequestParam("product_id") int productId){
        //第一种方法
        ProductOrder productOrder=productOrderService.save(userId,productId);
        Map<String ,Object> msg=new HashMap<>();
        msg.put("code",0);
        msg.put("data",productOrder);
        return msg;

    }

    @RequestMapping("saveByOpenReign")
    @HystrixCommand(fallbackMethod = "saveOrderFail")
    public Object saveByOpenReign(@RequestParam("user_id")int userId,@RequestParam("product_id") int productId){
        //第二种方法 openreign
        ProductOrder productOrder=productOrderService.saveByOpenReign(userId,productId);
        Map<String ,Object> msg=new HashMap<>();
        msg.put("code",0);
        msg.put("data",productOrder);
        return msg;
    }

    //编写fallback方法实现，方法签名一定要和api(@HystrixCommand(fallbackMethod = "saveOrderFail"))方法签名一致
    private Object saveOrderFail(int userId,int productId){
        Map<String ,Object> msg=new HashMap<>();
        msg.put("code",-1);
        msg.put("msg","抢购人数太多，您被挤出来了，稍等重试");
        return msg;
    }
}
