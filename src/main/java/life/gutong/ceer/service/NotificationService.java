package life.gutong.ceer.service;

import life.gutong.ceer.dto.NotificationDTO;
import life.gutong.ceer.dto.PaginationDTO;
import life.gutong.ceer.enums.NotificationStatusEnum;
import life.gutong.ceer.enums.NotificationTypeEnum;
import life.gutong.ceer.exception.CustomizeException;
import life.gutong.ceer.exception.CustomizeStatusMessage;
import life.gutong.ceer.mapper.NotificationMapper;
import life.gutong.ceer.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.service
 * @ClassName: NotificationService
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/16 19:58
 */
@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;


    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        Integer totalPage;
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);
        //校验分页查询语句  如果page当前页小于1 page=1 如果大于分页总页数 page=总页数
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        //校验分页查询语句  如果page当前页小于1 page=1 如果大于分页总页数 page=总页数
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage, page);
        /**page size*(page-1)
         * 1    5
         * 2    10
         * 3    15
         */
        //每页显示的数据数量
        Integer offset = size * (page - 1);
        //从数据库中分页查询notification
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        if (notifications.size() == 0) {
            return paginationDTO;
        }

        List<NotificationDTO> notificationDTOS = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            //获取枚举定义的名称信息
            notificationDTO.setTypeName(NotificationTypeEnum.getMessageByType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOS);
        return paginationDTO;
    }
    
    /**
     * @Description:  根据用户id获取当前用户未读消息的数量
     * @return: 
     * @Author: ceer
     * @Date: 2019/11/16
     */ 
    public Long getUnReadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    /**
     * @Description:   将回复的消息从未读改变成已读状态
     * @return:
     * @Author: ceer
     * @Date: 2019/11/16
     */
    public NotificationDTO read(Long id, User user) {
        //通过主键获取对象
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        //是否为空校验
        if (notification == null) {
            throw new CustomizeException(CustomizeStatusMessage.NOTIFICATION_NOT_FOUND);
        }
        //是否为当前用户校验
        if (!Objects.equals(notification.getReceiver(), user.getId())) {
            throw new CustomizeException(CustomizeStatusMessage.READ_NOTIFICATION_FAIL);
        }
        //将当前回复的信息状态改成为已读
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        //将当前信息更新到数据库
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        //获取当前回复的类型名称 回复的是问题还是评论
        notificationDTO.setTypeName(NotificationTypeEnum.getMessageByType(notification.getType()));
        return notificationDTO;
    }
}
