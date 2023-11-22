package com.dxdp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxdp.dto.Result;
import com.dxdp.entity.Voucher;
import com.dxdp.mapper.VoucherMapper;
import com.dxdp.entity.SeckillVoucher;
import com.dxdp.service.ISeckillVoucherService;
import com.dxdp.service.IVoucherService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.dxdp.utils.RedisConstants.SECKILL_STOCK_KEY;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class VoucherServiceImpl extends ServiceImpl<VoucherMapper, Voucher> implements IVoucherService {

    @Resource
    private ISeckillVoucherService seckillVoucherService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryVoucherOfShop(Long shopId) {
        // 查询优惠券信息
        List<Voucher> vouchers = getBaseMapper().queryVoucherOfShop(shopId);
        // 返回结果
        return Result.ok(vouchers);
    }

    //region 1.0
    // @Override
    // @Transactional
    // public void addSeckillVoucher(Voucher voucher) {
    //     // 保存优惠券
    //     save(voucher);
    //     // 保存秒杀信息
    //     SeckillVoucher seckillVoucher = new SeckillVoucher();
    //     seckillVoucher.setVoucherId(voucher.getId());
    //     seckillVoucher.setStock(voucher.getStock());
    //     seckillVoucher.setBeginTime(voucher.getBeginTime());
    //     seckillVoucher.setEndTime(voucher.getEndTime());
    //     seckillVoucherService.save(seckillVoucher);
    // }
    //endregion

    //region 2.0添加了保存秒杀库存到redis中
    @Override
    @Transactional
    public void addSeckillVoucher(Voucher voucher) {
        // 保存优惠券
        save(voucher);
        // 保存秒杀信息
        SeckillVoucher seckillVoucher = new SeckillVoucher();
        seckillVoucher.setVoucherId(voucher.getId());
        seckillVoucher.setStock(voucher.getStock());
        seckillVoucher.setBeginTime(voucher.getBeginTime());
        seckillVoucher.setEndTime(voucher.getEndTime());
        seckillVoucherService.save(seckillVoucher);
        // 保存秒杀库存到Redis中
        //SECKILL_STOCK_KEY 这个变量定义在RedisConstans中
        // private static final String SECKILL_STOCK_KEY ="seckill:stock:";
        stringRedisTemplate.opsForValue().set(SECKILL_STOCK_KEY + voucher.getId(), voucher.getStock().toString());
    }
    //endregion


}
