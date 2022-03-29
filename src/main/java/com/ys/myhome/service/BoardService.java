package com.ys.myhome.service;

import com.ys.myhome.model.Board;
import com.ys.myhome.model.User;
import com.ys.myhome.repository.BoardRepository;
import com.ys.myhome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    public Board save(String username, Board board){

        User user = userRepository.findByUsername(username);
        board.setUser(user);
        Board dbBoard = boardRepository.save(board);
        return dbBoard;
    }

}
