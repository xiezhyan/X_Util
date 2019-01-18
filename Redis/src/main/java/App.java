import com.sanq.product.redis.service.JedisPoolService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Xiezhyan on 2018/11/22.
 */
public class App {


    public static  void main(String[] args) {

        ApplicationContext act = new ClassPathXmlApplicationContext("spring.xml");
        JedisPoolService jedisPoolService = (JedisPoolService) act.getBean("jedisPoolService");

        jedisPoolService.set("aaa", "111");


    }

}
