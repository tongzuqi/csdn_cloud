package com.example.servicehi.TestRedis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DemoScheduledTask{

    @Autowired
    private RedisUtils redisUtils;


//    @Scheduled(fixedRate=1)
//    public void test3() {
//        this.getNo("Px","id","20141212",4);
//    }


    /**
     * 生成订单号 PREFIX+date+通过前缀获取的自增id
     * @param PREFIX 前缀
     * @param redis_Key redis的主键
     * @param date 日期格式字符串
     * @param num 前缀自增补全位数
     * @return
     */
    public  String getNo(String PREFIX,String redis_Key,String date ,Integer num){
        StringBuffer orderNO = new StringBuffer();
        //long ext = DateUtil.todayAddOf(1,0,0,1);
        long  no = redisUtils.incr(date + redis_Key);
        String number = StringUtil.lpad(num,(int)no);
        orderNO.append(PREFIX);
        orderNO.append(date);
        orderNO.append(number);
        System.out.println("编号生成："+orderNO.toString());
        return  orderNO.toString();
    }
}