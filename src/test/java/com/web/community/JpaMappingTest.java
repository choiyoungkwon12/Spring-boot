package com.web.community;

import com.web.community.domain.Board;
import com.web.community.domain.User;
import com.web.community.domain.enums.BoardType;
import com.web.community.repository.BoardRepository;
import com.web.community.repository.UserRepository;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.*;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

//@ExtendWith(SpringExtension.class)
@DataJpaTest
public class JpaMappingTest {
    private final String boardTestTitle = "테스트";
    private final String email = "test@gmail.com";

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    //@BeforeEach
    public void init() {
        User user = userRepository.save(User.builder()
                .name("havi")
                .password("test")
                .email(email)
                .createdDate(LocalDateTime.now())
                .build());

        boardRepository.save(Board.builder()
                .title(boardTestTitle)
                .subTitle("서브 타이틀")
                .content("콘텐츠")
                .boardType(BoardType.free)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .user(user).build());
    }

    //@Test
    public void createTest() {
        User user = userRepository.findByEmail(email);
//        Assertions.assertEquals(user.getName(), "havi");
//        Assertions.assertEquals(user.getPassword(), "test");
//        Assertions.assertEquals(user.getEmail(), email);
//
//        Board board = boardRepository.findByUser(user);
//        System.out.println("board.getTitle()) : " + board.getTitle());
//        Assertions.assertEquals(board.getTitle(), boardTestTitle);
//        Assertions.assertEquals(board.getSubTitle(), "서브 타이틀");
//        Assertions.assertEquals(board.getContent(), "콘텐츠");
//        Assertions.assertEquals(board.getBoardType(), BoardType.free);
    }

}
