package org.lyflexi.amqpclient.exchange_topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.lyflexi.amqpclient.util.ConnectionUtil;

public class Producer {  
  
    public static void main(String[] args) throws Exception {  
  
        Connection connection = ConnectionUtil.getConnection();
  
        Channel channel = connection.createChannel();  
  
        String exchangeName = "test_topic";  
  
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC,true,false,false,null);  
  
        String queue1Name = "test_topic_queue1";  
        String queue2Name = "test_topic_queue2";  
  
        channel.queueDeclare(queue1Name,true,false,false,null);  
        channel.queueDeclare(queue2Name,true,false,false,null);  
  
        // 绑定队列和交换机  
      // 参数1. queue：队列名称  
      // 参数2. exchange：交换机名称  
      // 参数3. routingKey：路由键,绑定规则
        // routing key 由多个.分割
        // routing key表达式语法：#代表多个单词，*代表1个单词
        // 需求： 所有error级别的日志存入数据库,所有order系统的日志存入数据库
        channel.queueBind(queue1Name,exchangeName,"#.error");  
        channel.queueBind(queue1Name,exchangeName,"order.*");  
        channel.queueBind(queue2Name,exchangeName,"*.*");  
  
        // 分别发送消息到队列：order.info、goods.info、goods.error  
        String body = "[所在系统：order][日志级别：info][日志内容：订单生成，保存成功]";  
//        channel.basicPublish(exchangeName,"order.info",null,body.getBytes());
  
//        body = "[所在系统：goods][日志级别：info][日志内容：商品发布成功]";
//        channel.basicPublish(exchangeName,"goods.info",null,body.getBytes());

        body = "[所在系统：goods][日志级别：error][日志内容：商品发布失败]";
        channel.basicPublish(exchangeName,"goods.error",null,body.getBytes());
  
        channel.close();  
        connection.close();  
  
    }  
  
}