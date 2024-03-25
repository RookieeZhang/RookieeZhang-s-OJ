package com.rookiezhang.zhangoj.service;

import com.rookiezhang.zhangoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.rookiezhang.zhangoj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rookiezhang.zhangoj.model.entity.User;

/**
* @author 25020
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2024-03-24 18:28:51
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest 题目创建信息 题目提交信息
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);
}
