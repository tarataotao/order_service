package com.tj.order_service.Controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tj.order_service.domain.ProductOrder;
import com.tj.order_service.service.ProductOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/v1/order")
@Slf4j
public class OrderController {

    @Autowired
    private ProductOrderService productOrderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("save")
    @HystrixCommand(fallbackMethod = "saveOrderFail")
    public Object save(@RequestParam("user_id")int userId, @RequestParam("product_id") int productId, HttpServletRequest request){

        String token=request.getHeader("token");
        String cookie=request.getHeader("cookie");
        log.info("token="+token);
        log.info("cookie="+cookie);


        //第一种方法
        ProductOrder productOrder=productOrderService.save(userId,productId);
        Map<String ,Object> msg=new HashMap<>();
        msg.put("code",0);
        msg.put("data",productOrder);
        return msg;

    }

    @RequestMapping("saveByOpenReign")
    @HystrixCommand(fallbackMethod = "saveOrderFail")
    public Object saveByOpenReign(@RequestParam("user_id")int userId,@RequestParam("product_id") int productId,HttpServletRequest request){
        String token=request.getHeader("token");
        String cookie=request.getHeader("cookie");
        log.info("token="+token);
        log.info("cookie="+cookie);

        //第二种方法 openreign
        ProductOrder productOrder=productOrderService.saveByOpenReign(userId,productId);
        Map<String ,Object> msg=new HashMap<>();
        msg.put("code",0);
        msg.put("data",productOrder);
        return msg;
    }

    //编写fallback方法实现，方法签名一定要和api(@HystrixCommand(fallbackMethod = "saveOrderFail"))方法签名一致
    private Object saveOrderFail(int userId,int productId,HttpServletRequest request){

        // 监控报警
        String saveOrderKey="save-order";
        String sendValue= (String) redisTemplate.opsForValue().get(saveOrderKey);
        final String ip=request.getRemoteAddr();
        new Thread(()->{
            if(StringUtils.isEmpty(sendValue)){
                System.out.println("紧急短信，用户下单失败，请离开查找原因，ip地址是："+ip);
                //发送一个http请求，调用短信服务 //TODO
                redisTemplate.opsForValue().set(saveOrderKey,"save-order-fail",20, TimeUnit.SECONDS);
            }else{
                System.out.println("已经发送过短信,20秒内不重复发送");

            }
        }).start();

        Map<String ,Object> msg=new HashMap<>();
        msg.put("code",-1);
        msg.put("msg","抢购人数太多，您被挤出来了，稍等重试");
        return msg;
    }
}
