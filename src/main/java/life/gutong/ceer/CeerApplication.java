package life.gutong.ceer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @Description: sprinboot项目启动类
 * @return: 
 * @Author: ceer
 * @Date: 2019/11/4
 */ 
@SpringBootApplication
@MapperScan("life.gutong.ceer.mapper")
public class CeerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CeerApplication.class, args);
    }

}
