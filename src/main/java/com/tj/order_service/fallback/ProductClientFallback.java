package com.tj.order_service.fallback;

import com.tj.order_service.service.ProductClient;
import org.springframework.stereotype.Component;

/**
 * 针对商品服务，做降级处理
 */
@Component
public class ProductClientFallback implements ProductClient{
    @Override
    public String findById(int id) {
        //可以做兜底
        System.out.println("feign 调用producet service 异常");
        return null;
    }
}
