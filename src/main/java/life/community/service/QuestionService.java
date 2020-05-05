package life.community.service;

import life.community.dto.PageDTO;
import life.community.dto.QuestionDTO;
import life.community.mapper.QuestionMapper;
import life.community.mapper.UserMapper;
import life.community.model.Question;
import life.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {


    @Autowired(required = false)
    private QuestionMapper questionMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    public PageDTO list(Integer page, Integer size) {
        Integer totalCount = questionMapper.count();
        Integer totalPage = 0;

        if (totalCount % size == 0)
            totalPage = totalCount/size;
        else
            totalPage = totalCount/size + 1;
        if ((page < 1)||(page > totalPage))
            return null;


        // 根据页码和size查询当前页question并且封装好。
        Integer offset = (page - 1)*size;
        List<Question> questionList = questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question: questionList) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        // 把页面内容课当前页导航信息封装在一起
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(totalPage,page);
        pageDTO.setQuestions(questionDTOList);

        return pageDTO;
    }

    public PageDTO getQuestionListByUserId(Integer userId, Integer page, Integer size) {
        PageDTO pageDTO = new PageDTO();

        Integer totalCount = questionMapper.count();
        Integer totalPage = 0;
        if (totalCount % size == 0)
            totalPage = totalCount/size;
        else
            totalPage = totalCount/size + 1;

        pageDTO.setTotalPage(totalPage);
        pageDTO.setPage(page);

        Integer offset = (page - 1)*size;
        List<Question> questionList = questionMapper.listByUserId(userId,offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question: questionList) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        pageDTO.setQuestions(questionDTOList);
        pageDTO.setPage(totalPage,page);
        return pageDTO;
    }

    public QuestionDTO getQuestionById(Integer id) {
        Question question = questionMapper.getById(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }
}
