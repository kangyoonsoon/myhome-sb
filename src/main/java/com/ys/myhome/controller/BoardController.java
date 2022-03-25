package com.ys.myhome.controller;

import com.ys.myhome.model.Board;
import com.ys.myhome.repository.BoardRepository;
import com.ys.myhome.validator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardValidator boardValidator;


    @GetMapping("/list")
    public String list(Model theModel){

        List<Board> boards = boardRepository.findAll();

        theModel.addAttribute("boards", boards);

        return "board/list";
    }

    @GetMapping("/form")
    public String form(Model theModel, @RequestParam(required = false) Long id) {

        if(id == null) {
            theModel.addAttribute("board", new Board());
        } else {
            Board board = boardRepository.findById(id).orElse(null);
            theModel.addAttribute("board", board);
        }

        return "board/form";
    }

    @PostMapping("/form")
    public String formProcessing(@Valid Board board, BindingResult bindingResult) {

        boardValidator.validate(board, bindingResult);

        if (bindingResult.hasErrors()) {
            System.out.println("inside bindingResult.hasErrors()");
            return "board/form";
        }

        boardRepository.save(board);

        return "redirect:/board/list";
    }

}
