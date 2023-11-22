package com.dxdp.service.impl;

import cn.hutool.json.JSONUtil;
import com.dxdp.dto.Result;
import com.dxdp.entity.ShopType;
import com.dxdp.mapper.ShopTypeMapper;
import com.dxdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryByRedis() {
        String keyPrefix = "cache:shopType:";
        List<String> shopTypeListJson =new ArrayList<>();
        // 1. redis查询商铺缓存
        for (int i = 1; i <= 10; i++) {
            String shopTypeJson = stringRedisTemplate .opsForValue() .get(keyPrefix+i);
            if (shopTypeJson!=null)
                shopTypeListJson.add(shopTypeJson);
        }
        // 2.判断是否存在
        if (!shopTypeListJson.isEmpty()) {
            // 3.存在，直接返回
            List<ShopType> shopTypeList=new ArrayList<>();
            for (String s : shopTypeListJson) {
                ShopType shopType = JSONUtil.toBean(s, ShopType.class);
                shopTypeList.add(shopType);
            }
            return Result.ok(shopTypeList) ;
        }
        // 4.不存在，根据id查询数据库
        List<ShopType> shopTypeList = query().orderByAsc("sort").list();
        // 5.不存在，返回错误
        if (shopTypeList.isEmpty()) {
            return Result.fail("店铺种类不存在! ");
        }
        for (ShopType shopType : shopTypeList) {
            String key=keyPrefix+shopType.getId();
            // 6.存在，写入redis
            stringRedisTemplate.opsForValue() . set(key,JSONUtil. toJsonStr (shopType)) ;
        }

        // 7.返回
        return Result.ok(shopTypeList) ;
    }
}
