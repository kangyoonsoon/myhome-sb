package com.ys.myhome.controller;

import com.ys.myhome.model.Board;
import com.ys.myhome.repository.BoardRepository;
import com.ys.myhome.service.BoardService;
import com.ys.myhome.validator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
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

    @Autowired
    private BoardService boardService;


    @GetMapping("/list")
    public String list(Model theModel, @PageableDefault(size = 5) Pageable pageable,
                       @RequestParam(required = false, defaultValue = "") String searchText){

//        Page<Board> boards = boardRepository.findAll(pageable);
        Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(searchText, searchText, pageable);

        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);

        theModel.addAttribute("boards", boards);
        theModel.addAttribute("startPage", startPage);
        theModel.addAttribute("endPage", endPage);


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
    public String formProcessing(@Valid Board board,
                                 BindingResult bindingResult,
                                 Authentication authentication) {

        boardValidator.validate(board, bindingResult);

        if (bindingResult.hasErrors()) {
            System.out.println("inside bindingResult.hasErrors()");
            return "board/form";
        }

        String username = authentication.getName();


//        boardRepository.save(board);
        boardService.save(username, board);
        return "redirect:/board/list";
    }

}
