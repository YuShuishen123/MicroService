package dubbo_seata.dubbo_storage;


import dubbo_seata.dubbo_common.Exception.CustomException;
import dubbo_seata.dubbo_common.storageInterface.StorageService;
import dubbo_seata.dubbo_storage.DO.StorageDO;
import dubbo_seata.dubbo_storage.Mapper.StorageMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Yu'S'hui'shen
 */
@DubboService
public class StorageServiceImpl implements StorageService {
    private static final Logger log = LoggerFactory.getLogger(StorageServiceImpl.class);
    /**
     * 注入持久化层接口
     */
    StorageMapper storageMapper;

    public StorageServiceImpl(StorageMapper storageMapper) {
        this.storageMapper = storageMapper;
    }

    @Override
    public void deduct(String commodityCode, int count) throws CustomException {
        log.info("开始扣减库存,商品编码:{},扣减数量:{}", commodityCode, count);
        StorageDO storageDO = storageMapper.selectByCommodityCode(commodityCode);
        log.info("查询到的商品信息:{}", storageDO);
        if (storageDO == null) {
            log.info("商品不存在");
            throw new CustomException("商品不存在", 500);
        }
        if (storageDO.getCount() < count) {
            log.info("库存不足");
            throw new CustomException("库存不足", 500);
        }
        int result = storageMapper.deduct(commodityCode, count);
        StorageDO newStorageDO = storageMapper.selectByCommodityCode(commodityCode);
        log.info("扣减后的库存信息:{}", newStorageDO);

        if (result == 0) {
            log.info("抛出异常");
            throw new CustomException("库存扣减失败", 500);
        }
    }
}
