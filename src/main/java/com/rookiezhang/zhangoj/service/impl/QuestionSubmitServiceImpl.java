package com.rookiezhang.zhangoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rookiezhang.zhangoj.common.ErrorCode;
import com.rookiezhang.zhangoj.exception.BusinessException;
import com.rookiezhang.zhangoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.rookiezhang.zhangoj.model.entity.Question;
import com.rookiezhang.zhangoj.model.entity.QuestionSubmit;
import com.rookiezhang.zhangoj.model.entity.QuestionSubmit;
import com.rookiezhang.zhangoj.model.entity.User;
import com.rookiezhang.zhangoj.model.enums.QuestionSubmitLanguageEnum;
import com.rookiezhang.zhangoj.model.enums.QuestionSubmitStatusEnum;
import com.rookiezhang.zhangoj.service.QuestionService;
import com.rookiezhang.zhangoj.service.QuestionSubmitService;
import com.rookiezhang.zhangoj.service.QuestionSubmitService;
import com.rookiezhang.zhangoj.mapper.QuestionSubmitMapper;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author 25020
* @description 针对表【question_submit(题目提交)】的数据库操作Service实现
* @createDate 2024-03-24 18:28:51
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService{
    @Resource
    private QuestionService questionService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        // 校验编程语言是否合法
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum enumByValue = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (enumByValue == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误！");
        }
        long QuestionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(QuestionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已点赞
        long userId = loginUser.getId();
        // 每个用户串行点赞
        // 锁必须要包裹住事务方法
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(QuestionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(language);
        // 设置初始化状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "提交题目失败");
        }
        return questionSubmit.getId();
    }
}




