package dubbo_seata.dubbo_order;


import dubbo_seata.dubbo_common.AccountInterface.AccountService;
import dubbo_seata.dubbo_common.DTO.OrderDTO;
import dubbo_seata.dubbo_common.Exception.CustomException;
import dubbo_seata.dubbo_common.orderInterface.OrderService;
import dubbo_seata.dubbo_order.DO.OrderDO;
import dubbo_seata.dubbo_order.mapper.OrderMapper;
import dubbo_seata.dubbo_order.util.mapStruct.OrderMapStruct;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Yu'S'hui'shen
 */
@DubboService
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    /*引入订单mapper接口*/
    OrderMapper orderMapper;
    OrderMapStruct orderMapStruct;

 @DubboReference(
         interfaceClass = AccountService.class,
         mock = "dubbo_seata.dubbo_common.AccountInterface.AccountServiceMock",
         parameters = {"timeout", "3000"}
 )
    AccountService accountService;

    public OrderServiceImpl(OrderMapStruct orderMapStruct, AccountService accountService, OrderMapper orderMapper){
        this.orderMapStruct = orderMapStruct;
        this.accountService = accountService;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderDTO create(String userId, String commodityCode, int orderCount) throws CustomException{

            /*计算总价*/
            int orderMoney = orderCount * 10;

            /*扣减账户余额*/
            try {
                accountService.debit(userId, orderMoney);
            } catch (CustomException e) {
                String errorMsg = String.format("扣减用户(userId=%s)余额失败，金额=%d，原因：%s",
                        userId, orderMoney, e.getMessage());
                log.error(errorMsg);
                throw new CustomException(errorMsg, e.getErrorCode());
            }
            /*生成订单内容*/
            OrderDO orderDO = new OrderDO();
            log.info("开始生成订单...");
            if(orderMoney > 1000){
                log.info("订单金额过大,创建订单失败");
                throw new CustomException("下单失败,订单金额过大",500);
            }
            long timestamp = System.currentTimeMillis();
            orderDO.setOrderId(String.valueOf(timestamp % 1000000000) +
                    ThreadLocalRandom.current().nextInt(0, 10));
            orderDO.setUserId(userId);
            orderDO.setCommodityCode(commodityCode);
            orderDO.setCount(orderCount);
            orderDO.setMoney(orderMoney);
            /*持久化存储订单*/
            if(orderMapper.createOrder(orderDO) == 0){
                throw new CustomException("订单插入失败",500);
            }
            /*返回订单*/
            return orderMapStruct.toOrderDTO(orderDO);
        }
}
