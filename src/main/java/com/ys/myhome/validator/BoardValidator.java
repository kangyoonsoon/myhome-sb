package com.ys.myhome.validator;


import com.ys.myhome.model.Board;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.thymeleaf.util.StringUtils;

@Component
public class BoardValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return Board.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {

        Board board = (Board) obj;

        if(StringUtils.isEmpty(board.getContent())) {
            errors.rejectValue("content", "key", "Please add content.");
        }

    }
}
