package net.shopin.oms.config;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.*;

import java.util.Collections;
import java.util.HashMap;

/**
 * <p>ClassName:  TransactionConfig 事务配置类</p>
 * <p>Description: oms-job事务配置 </p>
 * <p>Company: http://www.shopin.net</p>
 *
 * @author zhangyong@shopin.cn
 * @version 1.0.0
 * @date 2018/7/13 15:55
 */
@Aspect
@Configuration
public class TransactionConfig {

    private static final int TX_METHOD_TIMEOUT = 5;

    //配置切面
    private static final String AOP_POINTCUT_EXPRESSION = "execution (* net.shopin.oms.service.impl.*.*(..))";

    /**
     * @auther zhangyong@shopin.cn
     * @desc
     * @date 2018/7/16 10:06
     * @param transactionManager
     */
    @Autowired
    // private PlatformTransactionManager transactionManager;
    private DataSourceTransactionManager transactionManager;

    /**
     * @param
     * @auther zhangyong@shopin.cn
     * @desc 配置事务切面属性
     * Spring事务属性:
     * 1.传播特性(传播行为):
     * PROPAGATION_NOT_SUPPORTED : 表示当前方法运行的时候 如果有上下文事务,那么上下文事务会被挂起 等该方法执行结束,上下文事务恢复
     * PROPAGATION_REQUIRED : 表示当前方法必须运行在事务中,如果上下文有事务就加入上下文事务;上下文没有事务,就启动一个新事务
     * 2.隔离级别(ISOLATION): 暂未使用到;
     * 3.回滚规则: 异常级别 - Exception.class
     * 4.事务超时:
     * 5.是否只读:
     * @date 2018/7/16  12:30
     * @from JDK 1.8
     */
    @Bean
    public TransactionInterceptor txAdvice() {

        //可以实现对目标对象的每个方法实施不同的事务管理
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();

        //[1] 只读事务,不做更新操作
        RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        //设置只读;
        readOnlyTx.setReadOnly(true);
        //设置事务传播特性。 非事务方式 : 如果存在事务则将这个事务挂起,并使用新的数据库连接.新的数据库连接不使用事务;
        readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);

        //[2] 当前存在事务就使用当前事务,当前不存在事务就创建一个新的事务;
        RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
        //设置回滚规则
        requiredTx.setRollbackRules(
                Collections.singletonList(new RollbackRuleAttribute(Exception.class))
        );
        // 设置事务传播特性. 加入已有事务 : 规定当前的方法必须在事务中，如果没有事务就创建一个新事务，一个新事务和方法一同开始，随着方法的返回或抛出异常而终止
        requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        //设置事务超时 TODO 暂不设置超时;
        // requiredTx.setTimeout(TX_METHOD_TIMEOUT);
        HashMap<String, TransactionAttribute> txMap = new HashMap<>();
        // read-only = true
        txMap.put("get*", readOnlyTx);
        txMap.put("select*", readOnlyTx);
        // Exception 回滚
        txMap.put("save*", requiredTx);
        txMap.put("insert*", requiredTx);
        txMap.put("create*", requiredTx);
        txMap.put("modify*", requiredTx);
        txMap.put("update*", requiredTx);
        txMap.put("cancle*", requiredTx);
        txMap.put("delete*", requiredTx);
        txMap.put("merge*", requiredTx);
        txMap.put("remove*", requiredTx);
        txMap.put("overtime*", requiredTx);
        txMap.put("manualCancle*", requiredTx);
        txMap.put("des*", requiredTx);
        txMap.put("reg*", requiredTx);
        txMap.put("pay*", requiredTx);
        txMap.put("confirm*", requiredTx);
        txMap.put("*", readOnlyTx);

        source.setNameMap(txMap);
        transactionManager.setTransactionSynchronization(1);
        TransactionInterceptor txAdvice = new TransactionInterceptor(transactionManager, source);
        return txAdvice;
    }

    @Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }
}
