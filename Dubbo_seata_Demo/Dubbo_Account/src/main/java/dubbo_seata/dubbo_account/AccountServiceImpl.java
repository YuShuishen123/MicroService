package dubbo_seata.dubbo_account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import dubbo_seata.dubbo_account.DO.AccountDO;
import dubbo_seata.dubbo_account.mapper.AccountMapper;
import dubbo_seata.dubbo_common.AccountInterface.AccountService;
import dubbo_seata.dubbo_common.Exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;


/**
 * @author Yu'S'hui'shen
 */

@DubboService(parameters = {"exceptionToBiz", "true"})
@Slf4j
public class AccountServiceImpl implements AccountService {

    /*引入mapper层更新余额接口*/
    private final AccountMapper accountMapper;
    public AccountServiceImpl(AccountMapper accountMapper){
        this.accountMapper = accountMapper;
    }

    @Override
    public void debit(String userId, int money) throws CustomException{
        QueryWrapper<AccountDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        AccountDO accountDO = accountMapper.selectOne(queryWrapper);
        if(accountDO == null){
            log.info("余额扣除失败,原因:账户不存在");
            throw new CustomException("账户不存在", 500);
        }
        log.info("扣减前余额:{}",accountDO.getMoney());
        if(accountDO.getMoney() < money){
            log.info("余额扣除失败,原因:余额不足");
            throw new CustomException("余额不足", 500);
        }
        /*调用更新余额接口*/
        int result = accountMapper.updateAccount(userId,money);
        AccountDO newAccountDO = accountMapper.selectOne(queryWrapper);
        log.info("余额扣减成功,扣减后余额:{}",newAccountDO.getMoney());
        if(result == 0) {
            log.info("扣减余额失败,抛出异常");
            throw  new CustomException("扣减余额失败", 500);
        }
    }
}